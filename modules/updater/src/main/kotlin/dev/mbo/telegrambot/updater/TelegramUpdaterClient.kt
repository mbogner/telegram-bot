/*
 * Copyright 2022 mbodev @ https://mbo.dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.mbo.telegrambot.updater

import dev.mbo.telegrambot.client.retry.RetryDisabledException
import dev.mbo.telegrambot.client.telegram.TelegramClientConfig
import dev.mbo.telegrambot.client.telegram.api.TelegramApi
import dev.mbo.telegrambot.client.telegram.model.GetUpdateResponseDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.net.SocketException
import java.net.SocketTimeoutException

@Component
class TelegramUpdaterClient(
    private val telegramApi: TelegramApi,
    @Qualifier(TelegramClientConfig.Q_TELEGRAM_API_TOKEN) private val telegramBotToken: String,
    private val queues: TelegramUpdaterQueueAppender,
    @Value("\${feign.client.config.telegramApi.requestTimeoutSec}")
    private val requestTimeoutSec: Int
) {

    private val log = LoggerFactory.getLogger(javaClass)
    private val minExpectedDuration = requestTimeoutSec * 1000 - 100

    companion object {
        private var running = true

        fun isResponseValid(updates: ResponseEntity<GetUpdateResponseDto>): Boolean =
            null != updates.body && null != updates.body!!.result && updates.body!!.result!!.isNotEmpty()

        fun readHigherOffsetFromBody(offset: Int, updates: GetUpdateResponseDto): Int {
            if (null != updates.result) {
                for (update in updates.result!!) {
                    if (null != update.updateId && offset < update.updateId!!) {
                        return update.updateId!!
                    }
                }
            }
            return offset
        }

        fun isExpectedTimeout(
            start: Long,
            exc: RetryDisabledException,
            minExpectedDuration: Int
        ): Boolean {
            val duration = System.currentTimeMillis() - start
            return null != exc.cause &&
                    exc.cause is SocketTimeoutException &&
                    duration > minExpectedDuration
        }

        fun isSocketClosedException(exc: RetryDisabledException): Boolean =
            null != exc.cause && exc.cause is SocketException &&
                    exc.cause!!.message.equals("Socket Closed", ignoreCase = true)
    }

    @Async
    fun run() {
        var rqStartTimeMs = 0L
        var offset = 0
        while (running) {
            log.trace("getting updates")
            try {
                rqStartTimeMs = System.currentTimeMillis()
                val updates = telegramApi.getUpdates(
                    token = telegramBotToken,
                    offset = offset + 1,
                    limit = 100,
                    timeout = requestTimeoutSec,
                    allowedUpdates = null
                )
                if (updates.statusCode == HttpStatus.OK) {
                    log.trace("received updates: {}", updates.body)
                    if (isResponseValid(updates)) {
                        offset = readHigherOffsetFromBody(offset, updates.body!!)
                        queues.appendResponse(updates.body!!)
                    }
                } else {
                    log.warn("bad update response: {}", updates)
                }
            } catch (exc: RetryDisabledException) {
                if (isExpectedTimeout(rqStartTimeMs, exc, minExpectedDuration)) continue
                if (isSocketClosedException(exc)) continue
                log.error("error: {}", exc.message, exc)
            }
        }
    }

}
