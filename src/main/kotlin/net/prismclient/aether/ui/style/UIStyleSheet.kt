package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIMargin
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.style.util.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.unit.util.RELATIVE

open class UIStyleSheet : UICopy<UIStyleSheet> {
    var name: String = ""

    open var background: UIBackground? = null
    open var font: UIFont? = null

    open var x: UIUnit? = null
    open var y: UIUnit? = null
    open var width: UIUnit? = null
    open var height: UIUnit? = null

    open var padding: UIPadding? = null
    open var margin: UIMargin? = null
    open var anchor: UIAnchorPoint = UIAnchorPoint()

    /**
     * When true, any content within the component will be clipped
     */
    var clipContent = true

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
        this.anchor = sheet.anchor.copy()
        this.clipContent = sheet.clipContent

        return this
    }

    fun position(x: UIUnit, y: UIUnit) {
        this.x = x
        this.y = y
    }

    fun size(width: UIUnit, height: UIUnit) {
        this.width = width
        this.height = height
    }

    fun align(alignment: UIAlignment) {
        x = x ?: px(0)
        y = y ?: px(0)
        net.prismclient.aether.ui.util.extensions.align(alignment, x!!, y!!)
    }

    fun anchor(alignment: UIAlignment) {
        anchor.x = anchor.x ?: px(0)
        anchor.y = anchor.y ?: px(0)
        anchor.x!!.type = RELATIVE
        anchor.y!!.type = RELATIVE

        anchor.x!!.value = when (alignment) {
            TOPLEFT, TOPCENTER, TOPRIGHT -> 0f
            MIDDLELEFT, CENTER, MIDDLERIGHT -> 0.5f
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> 1f
            else -> throw UnsupportedOperationException("Unkown alignment type: $alignment")
        }

        anchor.y!!.value = when (alignment) {
            TOPLEFT, TOPCENTER, TOPRIGHT -> 0f
            MIDDLELEFT, CENTER, MIDDLERIGHT -> 0.5f
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> 1f
            else -> throw UnsupportedOperationException("Unkown alignment type: $alignment")
        }
    }

    /** Background **/

    inline fun background(block: UIBackground.() -> Unit) {
        background = background ?: UIBackground()
        background!!.block()
    }

    /**
     * Sets the color of the background
     */
    fun background(color: Int) = background { this.color = color }

    /** Font **/

    inline fun font(block: UIFont.() -> Unit) {
        font = font ?: UIFont()
        font!!.block()
    }

    /** Plotting **/

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

    inline fun animation(block: UIAnimation<*>.() -> Unit) {
        TODO("Animation block missing")
    }
}