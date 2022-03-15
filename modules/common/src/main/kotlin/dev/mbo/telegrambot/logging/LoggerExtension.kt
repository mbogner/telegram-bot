package dev.mbo.telegrambot.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
