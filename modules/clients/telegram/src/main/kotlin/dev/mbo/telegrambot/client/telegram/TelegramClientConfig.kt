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

import dev.mbo.telegrambot.client.telegram.api.TelegramApi
import dev.mbo.telegrambot.client.telegram.infrastructure.ApiClient
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [TelegramClientConfig::class])
class TelegramClientConfig(
    @Value("\${clients.telegram.basePath:https://api.telegram.org}") private val basePath: String,
    @Value("\${clients.telegram.botToken:notoken}") private val telegramBotToken: String
) {

    companion object {
        const val Q_TELEGRAM_API_TOKEN = "telegramApiToken"
    }

    @PostConstruct
    fun checkBotToken() {
        if (telegramBotToken.isBlank() ||
            telegramBotToken.startsWith("notoken") ||
            !telegramBotToken.startsWith("bot")
        ) {
            throw IllegalStateException("CLIENTS_TELEGRAM_BOT_TOKEN not set properly: $telegramBotToken")
        }
    }

    @Bean
    @Qualifier(Q_TELEGRAM_API_TOKEN)
    fun telegramApiToken(): String {
        return telegramBotToken
    }

    @Bean
    fun TelegramApi(): TelegramApi {
        return TelegramApi(
            basePath = basePath,
            client = ApiClient.defaultClient,
        )
    }

}