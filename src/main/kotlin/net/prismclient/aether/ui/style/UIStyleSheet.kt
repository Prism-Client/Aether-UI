package net.prismclient.aether.ui.style

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
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.transition
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
open class UIStyleSheet : UICopy<UIStyleSheet> {
    var name: String = ""

    open var x: UIUnit? = null
    open var y: UIUnit? = null
    open var width: UIUnit? = null
    open var height: UIUnit? = null

    open var background: UIBackground? = null
    open var font: UIFont? = null

    open var padding: UIPadding? = null
    open var margin: UIMargin? = null
    open var anchor: UIAnchorPoint? = null //UIAnchorPoint()

    /**
     * When true, any content within the component will be clipped
     */
    var clipContent = true

    fun position(x: Float, y: Float) = position(px(x), px(y))

    fun position(x: UIUnit, y: UIUnit) {
        this.x = x
        this.y = y
    }

    fun size(width: Float, height: Float) = size(px(width), px(height))

    fun size(width: UIUnit, height: UIUnit) {
        this.width = width
        this.height = height
    }

    /**
     * Aligns the component's position to the relative point of it's parent
     */
    fun align(alignment: UIAlignment) {
        x = x ?: px(0)
        y = y ?: px(0)
        net.prismclient.aether.ui.util.extensions.align(alignment, x!!, y!!)
    }

    /**
     * Shorthand for [align] and anchor. Both values are set to the [alignment]
     */
    fun control(alignment: UIAlignment) = control(alignment, alignment)

    /**
     * Shorthand for [align] and [anchor]
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
            TOPLEFT, TOPCENTER, TOPRIGHT -> 0f
            MIDDLELEFT, CENTER, MIDDLERIGHT -> 0.5f
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> 1f
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
        background { this.color = color; this.radius = radius; this.block() }

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

    /**
     * Applies the properties of an existing sheet to this
     */
    open fun apply(sheet: UIStyleSheet): UIStyleSheet {
        this.name = sheet.name

        this.background = sheet.background?.copy()
        this.font = sheet.font?.copy()

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

    protected var initialValue: UIStyleSheet.InitialValues? = null

    /**
     * Modifies the properties of [component] based on the [progress] by
     * this sheet and the [previous] sheet. For example, if the x of this
     * is 100, and the x of [previous] is 50, and [progress] is 0.5, the
     * difference is 50 and then multiplied by the progress is 25. Add the
     * initial sheet and the output is 75; thus, x should be 75.
     *
     * The [previous] sheet can be expected to be the same instance of this.
     *
     * All sheets should inherit and modify this method if there are unique
     * properties within the sheet. (If there isn't then why do you have a
     * unique sheet lol)
     */
    open fun animate(component: UIComponent<*>, progress: Float, previous: UIStyleSheet) {
        if (initialValue == null)
            initialValue = InitialValues(
                component.x,
                component.y,
                component.width,
                component.height,
                component.style.background?.copy(),
                component.style.font?.copy()
            )

        val style = component.style

        // Position
        component.x = fromProgress(progress, component.x(previous.x), component.x(x))
        component.y = fromProgress(progress, component.y(previous.y), component.y(y))
        component.width = fromProgress(progress, component.width(previous.width), component.width(width))
        component.height = fromProgress(progress, component.height(previous.height), component.height(height))

        // Background
        if (style.background != null) {
            val background = style.background!!

            // Ease the color
            background.color = transition(
                previous.background?.color ?: initialValue!!.background!!.color, 
                this.background?.color ?: initialValue!!.background!!.color,
                progress
            )
            
            // Ease the radius
            if (background.radius != null) {
                val radius = background.radius!!
                
                radius.topLeft = fromProgress(
                    progress,
                    previous.background?.radius?.topLeft ?: initialValue!!.background!!.radius!!.topLeft,
                    this.background?.radius?.topLeft ?: initialValue!!.background!!.radius!!.topLeft
                )
                radius.topRight = fromProgress(
                    progress,
                    previous.background?.radius?.topRight ?: initialValue!!.background!!.radius!!.topRight,
                    this.background?.radius?.topRight ?: initialValue!!.background!!.radius!!.topRight
                )
                radius.bottomRight = fromProgress(
                    progress,
                    previous.background?.radius?.bottomRight ?: initialValue!!.background!!.radius!!.bottomRight,
                    this.background?.radius?.bottomRight ?: initialValue!!.background!!.radius!!.bottomRight
                )
                radius.bottomLeft = fromProgress(
                    progress,
                    previous.background?.radius?.bottomLeft ?: initialValue!!.background!!.radius!!.bottomLeft,
                    this.background?.radius?.bottomLeft ?: initialValue!!.background!!.radius!!.bottomLeft
                )
            }
        }

        // Font
        if (font != null) {

        }

        // Padding
        if (style.padding != null) {

        }

        // Margin
        if (style.margin != null) {

        }
    }

    fun UIComponent<*>.x(unit: UIUnit?): Float =
        if (unit is UIRelativeUnit) {
            initialValue!!.x
        } else {
            0f
        } + this.getX(unit)

    fun UIComponent<*>.y(unit: UIUnit?): Float =
        if (unit is UIRelativeUnit) {
            initialValue!!.y
        } else {
            0f
        } + this.getY(unit)

    fun UIComponent<*>.width(unit: UIUnit?): Float =
        if (unit is UIRelativeUnit) {
            initialValue!!.width
        } else {
            0f
        } + this.getX(unit)

    fun UIComponent<*>.height(unit: UIUnit?): Float =
        if (unit is UIRelativeUnit) {
            initialValue!!.height
        } else {
            0f
        } + this.getY(unit)

    /**
     * Returns the value based on a [start] and [end] relative to the [progress]
     */
    protected fun fromProgress(progress: Float, start: Float, end: Float) = start + ((end - start) * progress)

    /**
     * Used to store the initial values of an animation
     */
    protected inner class InitialValues(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val background: UIBackground?,
        val font: UIFont?
    )
}