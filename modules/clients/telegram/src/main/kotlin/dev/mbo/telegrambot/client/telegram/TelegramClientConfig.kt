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

package dev.mbo.telegrambot.client.telegram

import dev.mbo.telegrambot.client.FeignConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@Configuration
@Import(FeignConfig::class)
class TelegramClientConfig(
    @Value("\${clients.telegram.botToken:notoken}") private val telegramBotToken: String
) {

    companion object {
        const val BOT_TOKEN_BEAN = "telegramBotToken"
    }

    @PostConstruct
    fun checkBotToken() {
        if (telegramBotToken.startsWith("notoken") || !telegramBotToken.startsWith("bot")) {
            throw IllegalStateException("CLIENTS_TELEGRAM_BOT_TOKEN not set properly: $telegramBotToken")
        }
    }

    @Bean
    @Qualifier(BOT_TOKEN_BEAN)
    fun telegramBotToken(): String {
        return telegramBotToken
    }

}