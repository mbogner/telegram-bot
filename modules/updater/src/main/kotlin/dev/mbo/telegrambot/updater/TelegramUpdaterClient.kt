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

import dev.mbo.telegrambot.client.telegram.TelegramClientConfig
import dev.mbo.telegrambot.client.telegram.api.TelegramApi
import dev.mbo.telegrambot.client.telegram.model.GetUpdateResponseDto
import dev.mbo.telegrambot.logging.logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class TelegramUpdaterClient(
    private val telegramApi: TelegramApi,
    @Qualifier(TelegramClientConfig.Q_TELEGRAM_API_TOKEN) private val telegramBotToken: String,
    private val queues: TelegramUpdaterQueueAppender,
    @Value("\${clients.telegram.requestTimeout:5}") private val requestTimeoutSec: Int
) {

    private val log = logger()

    companion object {
        private var running = true

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
    }

    @Async
    fun run() {
        var offset = 0
        while (running) {
            log.trace("getting updates")
            val updates: GetUpdateResponseDto = telegramApi.getUpdates(
                token = telegramBotToken,
                offset = offset + 1,
                limit = 100,
                timeout = requestTimeoutSec,
                allowedUpdates = null
            )
            if (true == updates.ok) {
                log.trace("received updates: {}", updates)
                offset = readHigherOffsetFromBody(offset, updates)
                queues.appendResponse(updates)
            } else {
                log.warn("bad update response: {}", updates)
            }
        }
    }

}
