package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.interfaces.UIDependable

/** Font Alignments **/
const val left = 1
const val center = 2
const val right = 4
const val top = 8
const val middle = 16
const val bottom = 32
const val baseline = 64

/**
 * Registers a style sheet for the given style, [S].
 */
inline fun <S : UIStyleSheet> styleOf(style: S, block: S.() -> Unit) {
    style.block()
    UIProvider.registerStyle(style)
}

/**
 * Registers a style sheet in the component scope. This is used when no style is provided
 * to the component, and instead, the style is provided at the component level.
 *
 * If the style's name is blank, it will be formatted as "Gen-${component.toString()}".
 */
inline fun <C : UIComponent<S>, S : UIStyleSheet> C.style(style: S, block: S.() -> Unit): C = apply {
    UIComponentDSL.updateState(this)
    if (style.name.isEmpty()) style.name = "Gen-$this"
    styleOf(style, block)
    this.applyStyle("Gen-$this")
    UIComponentDSL.restoreState(this)
}

/**
 * Returns a [UIRadius] with the given radius.
 */
fun radiusOf(radius: Number) = radiusOf(radius, radius, radius, radius)

/**
 * Returns a [UIRadius] with the given radii.
 */
fun radiusOf(topLeft: Number, topRight: Number, bottomRight: Number, bottomLeft: Number): UIRadius =
    UIRadius(topLeft.toFloat(), topRight.toFloat(), bottomRight.toFloat(), bottomLeft.toFloat())

/**
 * Creates a DSL block for creating components. When started and completed, the stacks will be allocated/cleared
 *
 * @see ucreate
 */
inline fun create(block: UIComponentDSL.() -> Unit) {
    UIComponentDSL.begin()
    UIComponentDSL.block()
    UIComponentDSL.complete()
}

/**
 * Unsafe version of [build]. Does not allocate/deallocate the stacks, thus nothing will be reset
 */
inline fun ucreate(block: UIComponentDSL.() -> Unit) = UIComponentDSL.block()

/**
 * Loads the [dependable].
 */
fun include(dependable: UIDependable) = dependable.load()