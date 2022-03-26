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
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class TelegramUpdaterWorker(
    private val telegramApi: TelegramApi,
    @Qualifier(TelegramClientConfig.BOT_TOKEN_BEAN) private val telegramBotToken: String,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private var running = true
    }

    @EventListener(ContextClosedEvent::class)
    fun shutdown() {
        log.info("stopping worker")
        running = false
    }

    @Async
    fun run() {
        val me = telegramApi.getMe(telegramBotToken)
        log.debug("me: {}", me)
        while (running) {
            log.debug("getting updates")
            try {
                Thread.sleep(1000)
            } catch (exc: InterruptedException) {
                if (running) {
                    log.warn("sleep interrupted", exc)
                }
            }
        }
    }

}
