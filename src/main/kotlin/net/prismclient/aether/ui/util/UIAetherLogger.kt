package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.Aether

internal fun inform(message: String) {
    if (Aether.debug)
        println("[Aether]: $message")
}

internal fun warn(message: String) {
    if (Aether.debug)
        println("[Aether]: $message")
}

internal fun error(message: String) = println("[Aether]: ERR -> $message")