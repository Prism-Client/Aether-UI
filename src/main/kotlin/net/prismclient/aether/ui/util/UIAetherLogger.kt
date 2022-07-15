package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.Aether

enum class LogLevel {
    DEBUG,
    GLOBAL
}

internal inline fun timed(message: String, level: LogLevel = LogLevel.DEBUG, block: () -> Unit) {
    if (level != LogLevel.DEBUG || Aether.debug) {
        println("[Aether]: TIMED -> $message")
        val start = System.currentTimeMillis()
        block()
        println("[Aether]: TIMED -> took ${System.currentTimeMillis() - start}ms")
    }
}

internal fun inform(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (level != LogLevel.DEBUG || Aether.debug)
        println("[Aether]: INFO -> $message")
}

internal fun debug(message: String) {
    if (Aether.debug)
        println("[Aether]: DEBUG -> $message")
}

internal fun warn(message: String, level: LogLevel = LogLevel.DEBUG) {
    if (level != LogLevel.DEBUG || Aether.debug)
        println("[Aether]: WARNING -> $message")
}

internal fun error(message: String) = println("[Aether]: ERR -> $message")