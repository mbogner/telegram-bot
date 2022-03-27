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

package dev.mbo.telegrambot.client.mite

import dev.mbo.telegrambot.client.FeignConfig
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.annotation.PostConstruct

@Configuration
@Import(FeignConfig::class)
class MiteClientConfig(
    @Value("\${clients.mite.apiKey:notoken}") private val miteApiKey: String
) {

    companion object {
        const val Q_MITE_API_KEY = "miteApiKey"
    }

    @PostConstruct
    fun checkBotToken() {
        if (miteApiKey.isBlank() || miteApiKey.startsWith("notoken")) {
            throw IllegalStateException("CLIENTS_MITE_API_KEY not set properly: $miteApiKey")
        }
    }

    @Bean
    @Qualifier(Q_MITE_API_KEY)
    fun miteApiKey(): String {
        return miteApiKey
    }

}