package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.animation.UIAnimationPriority
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.impl.background.UIGradientBackground
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

inline fun style(name: String, block: UIStyleSheet.() -> Unit) = UIStyleSheet().also { it.name = name; it.block(); UIProvider.registerStyle(it) }

inline fun <T : UIStyleSheet> style(sheet: T, name: String, block: T.() -> Unit) = sheet.also {
    it.name = name
    it.block()
    UIProvider.registerStyle(it)
}

/**
 * Creates a DSL block for creating components. When started and completed, the stacks will be allocated/cleared
 *
 * @see ubuild
 */
inline fun build(block: UIComponentDSL.() -> Unit) = create(block)

/**
 * Unsafe version of [build]. Does not allocate/deallocate the stacks, thus nothing will be reset
 */
inline fun ubuild(block: UIComponentDSL.() -> Unit) = UIComponentDSL.block()

fun radius(radius: Float): UIRadius = UIRadius().also { it.set(radius) }

@JvmOverloads
inline fun <T : UIStyleSheet> animation(sheet: T, name: String, priority: UIAnimationPriority = UIAnimationPriority.NORMAL, block: UIAnimation<T>.() -> Unit) = UIAnimation(sheet, name, priority).also(block).also { UIProvider.registerAnimation(it) }

inline fun gradient(block: UIGradientBackground.() -> Unit): UIGradientBackground = UIGradientBackground().also(block)

/**
 * Creates a DSL style block of the given component's type and returns the component
 */
inline fun <T : UIComponent<S>, S : UIStyleSheet> T.style(block: S.() -> Unit): T = also { style.block() }