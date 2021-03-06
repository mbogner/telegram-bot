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

package dev.mbo.telegrambot.api

import dev.mbo.telegrambot.model.ArrayMetaDto
import dev.mbo.telegrambot.model.MessageDto
import dev.mbo.telegrambot.model.MessagePageDto
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class MessagesApiServiceImpl : MessagesApiService {

    override suspend fun getMessageByBid(id: UUID, name: String): MessageDto {
        return MessageDto(
            text = "test",
            id = UUID.randomUUID(),
            createdAt = Instant.now()
        )
    }

    override suspend fun getMessages(): MessagePageDto {
        return MessagePageDto(
            meta = ArrayMetaDto(),
            content = emptyList()
        )
    }

}
