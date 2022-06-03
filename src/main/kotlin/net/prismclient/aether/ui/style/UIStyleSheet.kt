package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIMargin
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.util.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIRelativeUnit
import net.prismclient.aether.ui.unit.util.RELATIVE
import net.prismclient.aether.ui.util.extensions.fromProgress
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIStyleSheet] is the superclass of all styles. It holds the general
 * properties: [x], [y], [width], [height], [background], [font], [padding],
 * [margin], and [anchor]. All properties are optional and if not explicitly
 * set, the resulting output will be nothing or 0 (in the case of null units).
 *
 * In essence, [UIStyleSheet] holds properties which translate to [UIComponent]
 * properties when ran. Every component must have an instance of this attached
 * to it. Generally [UIStyleSheet] are (and should) be registered via the [UIProvider]
 * class. <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Styling.md">See how to properly create styles</a>
 *
 * Some components might explicitly state it wants a specific type of
 * [UIStyleSheet]. In that case, that required type of [UIStyleSheet]
 * must be created in order for the [UIComponent] to function properly.
 *
 * @see <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Styling.md">Styles</s>
 * @see <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Styling.md#creating-styles">How to create styles</a>
 */
open class UIStyleSheet : UICopy<UIStyleSheet>, UIAnimatable<UIStyleSheet> {
    var name: String = ""

    open var x: UIUnit? = null
    open var y: UIUnit? = null
    open var width: UIUnit? = null
    open var height: UIUnit? = null

    open var background: UIBackground? = null
    open var font: UIFont? = null

    open var padding: UIPadding? = null
    open var margin: UIMargin? = null
    open var anchor: UIAnchorPoint? = null

    /**
     * When true, any content within the component will be clipped
     */
    var clipContent = true

    /**
     * The [UIEase] associated with the keyframe. Leave null if the [UIStyleSheet] is not an animation sheet.
     */
    var ease: UIEase? = null

    /**
     * The result of an animation. Leave null if the [UIStyleSheet] is not an animation sheet.
     */
    var animationResult: UIAnimationResult? = null

    fun position(x: Number, y: Number) = position(px(x), px(y))

    fun position(x: UIUnit, y: UIUnit) {
        this.x = x
        this.y = y
    }

    fun size(width: Number, height: Number) = size(px(width), px(height))

    fun size(width: UIUnit, height: UIUnit) {
        this.width = width
        this.height = height
    }

    /**
     * Aligns the component's position to the relative point of it's parent
     *
     * @see control Shorthand
     */
    fun align(alignment: UIAlignment) {
        x = x ?: px(0)
        y = y ?: px(0)
        net.prismclient.aether.ui.util.extensions.align(alignment, x!!, y!!)
    }

    /**
     * Shorthand for [align] and anchor. Both values are set to the [alignment]
     *
     * @see align
     * @see anchor
     */
    fun control(alignment: UIAlignment) = control(alignment, alignment)

    /**
     * Shorthand for [align] and [anchor]
     *
     * @see align
     * @see anchor
     */
    fun control(alignment: UIAlignment, anchorAlignment: UIAlignment) {
        align(alignment)
        anchor(anchorAlignment)
    }

    fun anchor(alignment: UIAlignment) {
        anchor = anchor ?: UIAnchorPoint()
        anchor!!.x = anchor!!.x ?: px(0)
        anchor!!.y = anchor!!.y ?: px(0)
        anchor!!.x!!.type = RELATIVE
        anchor!!.y!!.type = RELATIVE

        anchor!!.x!!.value = when (alignment) {
            TOPLEFT, MIDDLELEFT, BOTTOMLEFT -> 0f
            TOPCENTER, CENTER, BOTTOMCENTER -> 0.5f
            TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> 1f
            else -> throw UnsupportedOperationException("Unknown alignment type: $alignment")
        }

        anchor!!.y!!.value = when (alignment) {
            TOPLEFT, TOPCENTER, TOPRIGHT -> 0f
            MIDDLELEFT, CENTER, MIDDLERIGHT -> 0.5f
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> 1f
            else -> throw UnsupportedOperationException("Unknown alignment type: $alignment")
        }
    }

    /** Background **/

    /**
     * Creates a background DSL block. If background is null, an instance of it is created
     */
    inline fun background(block: UIBackground.() -> Unit) {
        background = background ?: UIBackground()
        background!!.block()
    }

    /**
     * Sets the color of the background
     */
    @JvmOverloads
    inline fun background(color: Int, radius: UIRadius? = background?.radius, block: UIBackground.() -> Unit = {}) =
        background { this.backgroundColor = color; this.radius = radius; this.block() }

    /** Font **/

    /**
     * Creates a font DSL block. If font is null, an instance of it is created
     */
    inline fun font(block: UIFont.() -> Unit) {
        font = font ?: UIFont()
        font!!.block()
    }

    /** Plotting **/

    /**
     * Creates a padding DSL block. If padding is null, an instance of it is created
     */
    inline fun padding(block: UIPadding.() -> Unit) {
        padding = padding ?: UIPadding()
        padding!!.block()
    }


    fun padding(value: Float) = padding(value, value, value, value)

    fun padding(unit: UIUnit) = padding(unit, unit, unit, unit)

    fun padding(paddingTop: Float = 0f, paddingRight: Float = 0f, paddingBottom: Float = 0f, paddingLeft: Float = 0f) =
        padding {
            this.paddingTop = px(paddingTop)
            this.paddingRight = px(paddingRight)
            this.paddingBottom = px(paddingBottom)
            this.paddingLeft = px(paddingLeft)
        }

    fun padding(
        paddingTop: UIUnit? = padding?.paddingTop,
        paddingRight: UIUnit? = padding?.paddingRight,
        paddingBottom: UIUnit? = padding?.paddingBottom,
        paddingLeft: UIUnit? = padding?.paddingLeft
    ) = padding {
        this.paddingTop = paddingTop
        this.paddingRight = paddingRight
        this.paddingBottom = paddingBottom
        this.paddingLeft = paddingLeft
    }

    /**
     * Creates a margin DSL block. If margin is null, an instance of it is created
     */
    inline fun margin(block: UIMargin.() -> Unit) {
        margin = margin ?: UIMargin()
        margin!!.block()
    }

    fun margin(value: Float) = margin(value, value, value, value)

    fun margin(unit: UIUnit) = margin(unit, unit, unit, unit)

    fun margin(marginTop: Float = 0f, marginRight: Float = 0f, marginBottom: Float = 0f, marginLeft: Float = 0f) =
        margin {
            this.marginTop = px(marginTop)
            this.marginRight = px(marginRight)
            this.marginBottom = px(marginBottom)
            this.marginLeft = px(marginLeft)
        }

    fun margin(
        marginTop: UIUnit? = margin?.marginTop,
        marginRight: UIUnit? = margin?.marginRight,
        marginBottom: UIUnit? = margin?.marginBottom,
        marginLeft: UIUnit? = margin?.marginLeft
    ) = margin {
        this.marginTop = marginTop
        this.marginRight = marginRight
        this.marginBottom = marginBottom
        this.marginLeft = marginLeft
    }

    override fun copy(): UIStyleSheet = UIStyleSheet().apply(this)

    protected var initialValue: UIStyleSheet.InitialValues? = null

    override fun updateAnimationCache(component: UIComponent<*>) {
        if (initialValue != null) {
            initialValue!!.x = component.x
            initialValue!!.y = component.y
            initialValue!!.width = component.width
            initialValue!!.height = component.height
        }

        anchor?.updateAnimationCache(component)
        padding?.updateAnimationCache(component)
        margin?.updateAnimationCache(component)
        background?.updateAnimationCache(component)
        font?.updateAnimationCache(component)
    }

    override fun clearAnimationCache() {
        initialValue = null
        anchor?.clearAnimationCache()
        padding?.clearAnimationCache()
        margin?.clearAnimationCache()
        background?.clearAnimationCache()
        font?.clearAnimationCache()
    }

    override fun animate(previous: UIStyleSheet?, current: UIStyleSheet?, progress: Float, component: UIComponent<*>) {
        initialValue = initialValue
            ?: InitialValues(
                component.getX(x),
                component.getY(y),
                component.getX(width),
                component.getY(height),
                component.style.background?.copy(),
                component.style.font?.copy()
            )

        // Check if any of the keyframes have non allocated classes
        // if they aren't allocated, create them.
        if (previous?.background != null || current?.background != null)
            background {}
        if (previous?.font != null || current?.font != null)
            font {}
        if (previous?.padding != null || current?.padding != null)
            padding {}
        if (previous?.margin != null || current?.margin != null)
            margin {}
        if (previous?.anchor != null || current?.anchor != null)
            anchor = anchor ?: UIAnchorPoint()

        // Calculate Bounds
        padding?.animate(previous?.padding, current?.padding, progress, component)
        margin?.animate(previous?.margin, current?.margin, progress, component)

        // Update Size
        component.width = fromProgress(component.width(current?.width), component.width(previous?.width), progress)
        component.height = fromProgress(component.height(current?.height), component.height(previous?.height), progress)

        // Update the Anchor
        anchor?.animate(previous?.anchor, current?.anchor, progress, component)

        // Update Position
        if (!component.overridden) {
            component.x = fromProgress(component.x(previous?.x), component.x(current?.x), progress)
            component.y = fromProgress(component.y(previous?.y), component.y(current?.y), progress)

            component.x += component.marginLeft - component.anchorX
            component.y += component.marginTop - component.anchorY
        }

        // Update bounds
        component.updateBounds()

        // Update background and font
        background?.animate(previous?.background, current?.background, progress, component)
        font?.animate(previous?.font, current?.font, progress, component)
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIStyleSheet?, retain: Boolean) {
        anchor?.saveState(component, keyframe?.anchor, retain)

        padding?.saveState(component, keyframe?.padding, retain)
        margin?.saveState(component, keyframe?.margin, retain)
        background?.saveState(component, keyframe?.background, retain)
        font?.saveState(component, keyframe?.font, retain)

        if (retain) {
            if (keyframe?.x != null)
                x = keyframe.x
            if (keyframe?.y != null)
                y = keyframe.y
            if (keyframe?.width != null)
                width = keyframe.width
            if (keyframe?.height != null)
                height = keyframe.height
        }

        component.update()
    }

    fun UIComponent<*>.x(unit: UIUnit?): Float = if (unit == null || unit is UIRelativeUnit) {
        initialValue!!.x
    } else {
        0f
    } + this.getX(unit)

    fun UIComponent<*>.y(unit: UIUnit?): Float = if (unit == null || unit is UIRelativeUnit) {
        initialValue!!.y
    } else {
        0f
    } + this.getY(unit)

    fun UIComponent<*>.width(unit: UIUnit?): Float = if (unit == null || unit is UIRelativeUnit) {
        initialValue!!.width
    } else {
        0f
    } + this.getX(unit)

    fun UIComponent<*>.height(unit: UIUnit?): Float = if (unit == null || unit is UIRelativeUnit) {
        initialValue!!.height
    } else {
        0f
    } + this.getY(unit)

    /**
     * Applies the properties of an existing sheet to this
     */
    open fun apply(sheet: UIStyleSheet): UIStyleSheet {
        this.name = sheet.name

        this.background = sheet.background?.copy()
        this.font = sheet.font?.copy()

        this.ease = sheet.ease?.copy()
        this.animationResult = sheet.animationResult

        this.x = sheet.x?.copy()
        this.y = sheet.y?.copy()
        this.width = sheet.width?.copy()
        this.height = sheet.height?.copy()

        this.padding = sheet.padding?.copy()
        this.margin = sheet.margin?.copy()
        this.anchor = sheet.anchor?.copy()
        this.clipContent = sheet.clipContent

        return this
    }

    /**
     * Used to store the initial values of an animation
     */
    protected inner class InitialValues(
        var x: Float,
        var y: Float,
        var width: Float,
        var height: Float,
        val background: UIBackground?,
        val font: UIFont?
    )
}