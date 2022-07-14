package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * Type alias for a function which has a receiver of [T] and accepts, and returns
 * nothing. The block is intended to apply properties to the receiver [T].
 */
typealias Block<T> = T.() -> Unit

fun <T : UIStyleSheet> T.name(styleName: String): T {
    this.name = styleName
    return this
}