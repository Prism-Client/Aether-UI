package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet

inline fun style(block: UIStyleSheet.() -> Unit) = UIStyleSheet().also(block).also(block)

inline fun <T : UIStyleSheet> style(sheet: T, name: String, block: T.() -> Unit) = sheet.also {
    it.name = name
    it.block()
    UIProvider.addStyle(it)
}

fun radius(radius: Float): UIRadius =
        UIRadius().also { it.set(radius) }

inline fun <T : UIAnimation<*>> animation(animation: T, block: T.() -> Unit) =
        animation.also(block).also { UIProvider.registerAnimation(it) }
