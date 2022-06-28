package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIMargin
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.util.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.RELATIVE
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.rel
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
open class UIStyleSheet(var name: String) : UICopy<UIStyleSheet>, UIAnimatable<UIStyleSheet> {
    /**
     * When true, the property will not be cleared when the screen is closed
     */
    var immutableStyle: Boolean = false

    open var x: UIUnit? = null
    open var y: UIUnit? = null
    open var width: UIUnit? = null
    open var height: UIUnit? = null

    open var background: UIBackground? = null
        set(value) {
            field = value

            // For the background to be rendered normally
            // the width and height have to be 100%, so if
            // they are not set, set them to 100%
            if (value != null) {
                value.width = value.width ?: rel(1)
                value.height = value.height ?: rel(1)
            }
        }
    open var font: UIFont? = null

    open var padding: UIPadding? = null
    open var margin: UIMargin? = null
    open var anchor: UIAnchorPoint? = null

    /**
     * When true, any content within the component will be clipped
     */
    var clipContent = true

    override fun updateAnimationCache(component: UIComponent<*>) {
        TODO("Not yet implemented")
    }

    override fun clearAnimationCache() {
        TODO("Not yet implemented")
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIStyleSheet?, retain: Boolean) {
        TODO("Not yet implemented")
    }

    override fun animate(previous: UIStyleSheet?, current: UIStyleSheet?, progress: Float, component: UIComponent<*>) {
        TODO("Not yet implemented")
    }

    /** Shorthands **/

    /**
     * Shorthand for setting the position and size as pixels.
     */
    fun plot(x: Float, y: Float, width: Float, height: Float) {
        position(x, y)
        size(width, height)
    }

    /**
     * Shorthand for setting the position and size as units.
     */
    fun plot(x: UIUnit, y: UIUnit, width: UIUnit, height: UIUnit) {
        position(x, y)
        size(width, height)
    }

    /**
     * Positions the component at the given [x], and [y] value as pixels.
     */
    fun position(x: Number, y: Number) = position(px(x), px(y))

    /**
     * Positions the component at the given [x], and [y] values as the given units.
     */
    fun position(x: UIUnit, y: UIUnit) {
        this.x = x
        this.y = y
    }

    /**
     * Sizes the component with the given [width], and height as pixels.
     */
    fun size(width: Number, height: Number) = size(px(width), px(height))

    /**
     * Sizes the component with the given [width], and height as the given units.
     */
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

    /**
     * Anchors the component to the given [alignment].
     */
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

    /**
     * Creates a font DSL block which optionally accepts a size, color, text alignment, font family, and font type.
     */
    @JvmOverloads
    inline fun font(
            fontFamily: String = font?.fontFamily ?: UIDefaults.instance.fontFamily,
            fontSize: Float = font?.fontSize ?: UIDefaults.instance.fontSize,
            fontColor: Int = font?.fontColor ?: UIDefaults.instance.fontColor,
            textAlignment: Int = font?.textAlignment ?: UIDefaults.instance.textAlignment,
            fontType: UIFont.FontType = font?.fontType ?: UIDefaults.instance.fontType,
            block: UIFont.() -> Unit = {}
    ) = font {
        this.fontSize = fontSize
        this.fontColor = fontColor
        this.textAlignment = textAlignment
        this.fontFamily = fontFamily
        this.fontType = fontType
        this.block()
    }

    /** Plotting **/

    /**
     * Creates a padding DSL block. If padding is null, an instance of it is created
     */
    inline fun padding(block: UIPadding.() -> Unit) {
        padding = padding ?: UIPadding()
        padding!!.block()
    }

    /**
     * Sets the padding to the given [value] as pixels.
     */
    fun padding(value: Float) = padding(value, value, value, value)

    /**
     * Sets the padding to the given [unit] as the unit.
     */
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

    override fun copy(): UIStyleSheet = UIStyleSheet(name).apply(this)

    /**
     * Applies the properties of an existing sheet to this
     */
    open fun apply(sheet: UIStyleSheet): UIStyleSheet {
        this.immutableStyle = sheet.immutableStyle
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
}