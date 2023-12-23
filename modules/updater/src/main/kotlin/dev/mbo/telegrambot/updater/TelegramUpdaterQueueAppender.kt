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

import dev.mbo.telegrambot.client.telegram.model.GetUpdateResponseDto
import dev.mbo.telegrambot.client.telegram.model.UpdateDto
import dev.mbo.telegrambot.logging.logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.LinkedBlockingQueue

@Component
class TelegramUpdaterQueueAppender(
    @Qualifier("messages") private val messagesQueue: LinkedBlockingQueue<UpdateDto>
) {

    private val log = logger()

    @Async
    fun appendResponse(response: GetUpdateResponseDto) {
        if (null != response.result) {
            append(response.result!!)
        }
    }

    fun append(updates: List<UpdateDto>) {
        for (update in updates) {
            if (appendMessage(update)) continue
        }
    }

    fun appendMessage(update: UpdateDto): Boolean {
        if (null != update.message) {
            messagesQueue.put(update)
            return true
        }
        return false
    }

    @Scheduled(cron = "*/5 * * * * *")
    fun printQueueSizes() {
        log.trace("queue sizes: messages={}", messagesQueue.size)
    }

}