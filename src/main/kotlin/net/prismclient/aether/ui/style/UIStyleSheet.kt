package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.UIFrameSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.UITextAlignment
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIMargin
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.util.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.Block
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy
import net.prismclient.aether.ui.util.name
import net.prismclient.aether.ui.util.radiusOf

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
 * When creating a style sheet a copy method must be explicitly created to ensure no errors
 * are thrown when used. If the style sheet is intended on being inheritable, the apply method
 * should also be overridden. See [UIFrameSheet] for an example.
 *
 * @since 1.0
 * @see <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Styling.md">Styles</s>
 * @see <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Styling.md#creating-styles">How to create styles</a>
 */
open class UIStyleSheet : UICopy<UIStyleSheet>, UIAnimatable<UIStyleSheet> {
    var name: String = ""

    /**
     * When true, the property will not be cleared when Aether cleans up styles.
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
    var clipContent = false

    override fun animate(
        animation: UIAnimation<*>, previous: UIStyleSheet?, current: UIStyleSheet?, progress: Float
    ) {
        val component = animation.component

        if (!component.overridden) {
            component.x = previous?.x.lerp(current?.x, component, x, progress, false) + component.getParentX()
            component.y = previous?.y.lerp(current?.y, component, y, progress, true) + component.getParentY()
            component.width = previous?.width.lerp(current?.width, component, width, progress, false)
            component.height = previous?.height.lerp(current?.height, component, height, progress, true)
        }

        if (previous?.background != null || current?.background != null) {
            background = background ?: UIBackground()
            background!!.animate(animation, previous?.background, current?.background, progress)
        }
        if (previous?.font != null || current?.font != null) {
            font = font ?: UIFont()
            font!!.animate(animation, previous?.font, current?.font, progress)
        }
        if (previous?.padding != null || current?.padding != null) {
            padding = padding ?: UIPadding()
            padding!!.animate(animation, previous?.padding, current?.padding, progress)
        }
        if (previous?.margin != null || current?.margin != null) {
            margin = margin ?: UIMargin()
            margin!!.animate(animation, previous?.margin, current?.margin, progress)
        }
        if (previous?.anchor != null || current?.anchor != null) {
            anchor = anchor ?: UIAnchorPoint()
            anchor!!.animate(animation, previous?.anchor, current?.anchor, progress)
        }
        if (!component.overridden) {
            component.x += component.getParentX() + component.marginLeft - component.anchorX
            component.y += component.getParentY() + component.marginTop - component.anchorY
        }
        component.updateBounds()
        component.updateStyle()
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIStyleSheet?) {
        x = keyframe?.x ?: x?.copy()
        y = keyframe?.y ?: y?.copy()
        width = keyframe?.width ?: width?.copy()
        height = keyframe?.height ?: height?.copy()

        background?.save(animation, keyframe?.background)
        font?.save(animation, keyframe?.font)
        padding?.save(animation, keyframe?.padding)
        margin?.save(animation, keyframe?.margin)
        anchor?.save(animation, keyframe?.anchor)
    }

    // -- Control Shorthands -- //

    fun plot(controlAlignment: UIAlignment, width: Number, height: Number) {
        control(controlAlignment)
        size(width, height)
    }

    @JvmOverloads
    fun plot(controlAlignment: UIAlignment, width: UIUnit? = null, height: UIUnit? = null) {
        control(controlAlignment)
        size(width, height)
    }

    fun plot(x: Number, y: Number, width: Number, height: Number) {
        position(x, y)
        size(width, height)
    }

    fun plot(x: UIUnit?, y: UIUnit?, width: UIUnit?, height: UIUnit?) {
        position(x, y)
        size(width, height)
    }

    fun position(x: Number, y: Number) = position(px(x), px(y))

    fun position(x: UIUnit?, y: UIUnit?) {
        this.x = x
        this.y = y
    }

    fun size(width: Number, height: Number) = size(px(width), px(height))

    fun size(width: UIUnit?, height: UIUnit?) {
        this.width = width
        this.height = height
    }

    fun align(alignment: UIAlignment) {
        x = x ?: px(0)
        y = y ?: px(0)
        align(alignment, x!!, y!!)
    }


    fun control(alignment: UIAlignment) = control(alignment, alignment)

    fun control(alignment: UIAlignment, anchorAlignment: UIAlignment) {
        align(alignment)
        anchor(anchorAlignment)
    }

    fun anchor(alignment: UIAlignment) {
        anchor = anchor ?: UIAnchorPoint()
        anchor!!.align(alignment)
    }

    // -- Background Shorthands -- //

    inline fun background(block: Block<UIBackground>) {
        background = background ?: UIBackground()
        background!!.block()
    }

    inline fun background(color: Int, radius: Number = 0, block: Block<UIBackground> = {}) = background {
        this.backgroundColor = colorOf(color)
        if (radius != 0 || this.radius != null) this.radius = radiusOf(radius)
        block()
    }

    inline fun background(color: UIColor, radius: UIRadius? = background?.radius, block: Block<UIBackground> = {}) =
        background { this.backgroundColor = color; this.radius = radius; this.block() }

    // -- Font Shorthands -- //

    inline fun font(block: Block<UIFont>) {
        font = font ?: UIFont()
        font!!.block()
    }

    inline fun font(
        fontName: String,
        fontColor: UIColor,
        fontSize: UIUnit?,
        lineHeightSpacing: UIUnit? = null,
        block: Block<UIFont> = {}
    ) = font {
        this.fontName = fontName
        this.fontSize = fontSize
        this.fontColor = fontColor
        this.lineHeightSpacing = lineHeightSpacing
        this.block()
    }

    inline fun font(
        alignment: UIAlignment,
        x: UIUnit? = null,
        y: UIUnit? = null,
        width: UIUnit? = null,
        height: UIUnit? = null,
        block: Block<UIFont> = {}
    ) = font {
        this.anchor(alignment)
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.block()
    }

    inline fun font(
        x: UIUnit? = null,
        y: UIUnit? = null,
        width: UIUnit? = null,
        height: UIUnit? = null,
        block: Block<UIFont> = {}
    ) = font {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.block()
    }

    inline fun font(
        verticalAlignment: UITextAlignment,
        horizontalAlignment: UITextAlignment,
        textResizing: UIFont.TextResizing = UIFont.TextResizing.Auto,
        block: Block<UIFont> = {}
    ) = font {
        this.textResizing = textResizing
        this.verticalAlignment = verticalAlignment
        this.horizontalAlignment = horizontalAlignment
        this.block()
    }

    inline fun fontSize(width: UIUnit, height: UIUnit, block: Block<UIFont>) = font {
        this.size(width, height)
        this.block()
    }

    // -- Padding and Margin Shorthands -- //

    inline fun padding(block: Block<UIPadding>) {
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
    inline fun margin(block: Block<UIMargin>) {
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

    override fun copy(): UIStyleSheet = UIStyleSheet().name(name).apply(this)

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