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

import dev.mbo.telegrambot.logging.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class TelegramUpdater(
    private val worker: TelegramUpdaterClient,
    @Value("\${application.updater.enabled:true}") private val enabled: Boolean,
) : CommandLineRunner {

    private val log = logger()

    override fun run(vararg args: String?) {
        log.info("starting update worker")
        if (enabled) worker.run()
    }

}
