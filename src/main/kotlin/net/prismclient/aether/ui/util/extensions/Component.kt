package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * Initializes the component builder, and creates a [UIComponentDSL] block.
 */
inline fun create(block: UIComponentDSL.() -> Unit) {
    UIComponentDSL.create()
    UIComponentDSL.block()
}

inline fun style(name: String, block: UIStyleSheet.() -> Unit) =
    UIStyleSheet().also { it.name = name; it.block(); UIProvider.registerStyle(it) }

inline fun <T : UIStyleSheet> style(sheet: T, name: String, block: T.() -> Unit) = sheet.also {
    it.name = name
    it.block()
    UIProvider.registerStyle(it)
}

fun radius(radius: Float): UIRadius =
    UIRadius().also { it.set(radius) }

inline fun <T : UIAnimation<*>> animation(animation: T, block: T.() -> Unit) =
    animation.also(block).also { UIProvider.registerAnimation(it) }
