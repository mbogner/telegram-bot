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

package dev.mbo.telegrambot.config

import dev.mbo.telegrambot.logging.logger
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.beans.factory.BeanCreationNotAllowedException
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.lang.reflect.Method

@EnableAsync
@Configuration
class AsyncConfig : AsyncConfigurer {

    private val log = logger()

    companion object {
        private var closed = false

        fun isBeanCreationNotAllowedOnShutdown(throwable: Throwable): Boolean =
            closed && throwable is BeanCreationNotAllowedException
    }

    @EventListener(ContextClosedEvent::class)
    fun shutdown() {
        closed = true
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return AsyncUncaughtExceptionHandler { throwable: Throwable, method: Method, params: Array<Any> ->
            if (!isBeanCreationNotAllowedOnShutdown(throwable)) {
                log.error(
                    "async method {}.{}({}) failed",
                    method.declaringClass,
                    method.name,
                    params,
                    throwable
                )
            }
        }
    }
}