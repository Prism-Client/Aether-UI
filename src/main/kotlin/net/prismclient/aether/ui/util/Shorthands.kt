package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.impl.background.UIGradientBackground
import net.prismclient.aether.ui.renderer.impl.property.UIMargin
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.px
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
 * Image flags
 */
const val GENERATE_MIPMAPS = 1
const val REPEATX = 2
const val REPEATY = 4
const val FLIPY = 8
const val PREMULTIPLIED = 16
const val NEAREST = 32

/**
 * Creates a DSL block from the given [obj] of type [T].
 */
inline fun <T> blockFrom(obj: T, block: Block<T>) = obj.block()

/**
 * Creates a style sheet [block] from the given style sheet [S], sets the [name] and registers the style.
 */
@JvmName("styleExtension")
inline fun <S : UIStyleSheet> S.style(name: String, block: Block<S>): S = apply {
    this.name = name
    this.block()
    UIProvider.registerStyle(this)
}

/**
 * Creates a style sheet [block] from the given style sheet [S], sets the [name] and registers the style.
 */
inline fun <S : UIStyleSheet> style(sheet: S, name: String, block: Block<S>): S {
    sheet.name = name
    sheet.block()
    UIProvider.registerStyle(sheet)
    return sheet
}

/**
 * Creates a style [block] from the given component [C]. If the style has not been created
 * one is automatically allocated with the name of
 *
 *      "$C.toString()-sheet"
 */
inline fun <C : UIComponent<S>, S : UIStyleSheet> C.style(block: Block<S>): C = this.style("$this-sheet", block)

/**
 * Creates a style [block] from the given component [C]. If the style has not been
 * created, one is automatically allocated with the name of [name].
 */
inline fun <C : UIComponent<S>, S : UIStyleSheet> C.style(name: String, block: Block<S>): C = also {
    if (!this.hasStyle()) {
        this.createsStyle().run {
            this.name = name
            UIProvider.registerStyle(this)
        }
        this.applyStyle("Gen-$this")
    }

    this.style.block()
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
 * Creates a [UIPadding] with a pixel unit of [padding].
 */
fun paddingOf(padding: Number) = paddingOf(padding, padding, padding, padding)

/**
 * Creates a [UIPadding] from four pixel units.
 */
fun paddingOf(top: Number, right: Number, bottom: Number, left: Number): UIPadding =
    paddingOf(px(top), px(right), px(bottom), px(left))

/**
 * Creates a [UIPadding] given four [UIUnit]s.
 */
fun paddingOf(top: UIUnit, right: UIUnit, bottom: UIUnit, left: UIUnit): UIPadding = UIPadding().also {
    it.paddingTop = top
    it.paddingRight = right
    it.paddingBottom = bottom
    it.paddingLeft = left
}

/**
 * Creates a [UIMargin] with a pixel unit of [margin].
 */
fun marginOf(margin: Number) = marginOf(margin, margin, margin, margin)

/**
 * Creates a [UIMargin] from four pixel units.
 */
fun marginOf(top: Number, right: Number, bottom: Number, left: Number): UIMargin =
    marginOf(px(top), px(right), px(bottom), px(left))

/**
 * Creates a [UIMargin] given four [UIUnit]s.
 */
fun marginOf(top: UIUnit, right: UIUnit, bottom: UIUnit, left: UIUnit): UIMargin = UIMargin().also {
    it.marginTop = top
    it.marginRight = right
    it.marginBottom = bottom
    it.marginLeft = left
}

/**
 * Creates a DSL block for creating components. When started and completed, the stacks will be allocated/cleared
 *
 * @see ucreate
 */
inline fun create(block: Block<UIComponentDSL>) {
    UIComponentDSL.begin()
    UIComponentDSL.block()
    UIComponentDSL.complete()
}

/**
 * Unsafe version of [build]. Does not allocate/deallocate the stacks, thus nothing will be reset
 */
inline fun ucreate(block: Block<UIComponentDSL>) = UIComponentDSL.block()

/**
 * Loads the [dependable].
 */
fun include(dependable: UIDependable) = dependable.load()

/**
 * Creates an animation where the component is [C], the style of that component is [S], the animation
 * name is [animationName], and an instance of the component's style is passed as [style]. A block is
 * given to add the keyframes and modify other properties of the [UIAnimation]. The animation is
 * automatically registered under the name given.
 */
inline fun <S : UIStyleSheet> animationOf(animationName: String, style: S, block: UIAnimation<S>.() -> Unit): UIAnimation<S> {
    val animation = UIAnimation<S>(animationName, style)
    animation.block()
    UIProvider.registerAnimation(animationName, animation)
    return animation
}

/**
 * Creates a [UIGradientBackground], applies the given block to it, and returns it.
 */
inline fun gradient(block: Block<UIGradientBackground>): UIGradientBackground = UIGradientBackground().also(block)