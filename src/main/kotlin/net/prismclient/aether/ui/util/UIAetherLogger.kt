package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.UICore

internal fun inform(message: String) {
    if (UICore.debug)
        println("[Aether]: $message")
}

internal fun warn(message: String) {
    if (UICore.debug)
        println("[Aether]: $message")
}

internal fun error(message: String) = println("[Aether]: ERR -> $message")