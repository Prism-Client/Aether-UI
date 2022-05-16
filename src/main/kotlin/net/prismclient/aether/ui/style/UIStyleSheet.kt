package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIMargin
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.style.util.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy

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

    inline fun background(block: UIBackground.() -> Unit) {
        background = UIBackground()
        background!!.block()
    }

    inline fun font(block: UIFont.() -> Unit) {
        font = UIFont()
        font!!.block()
    }

    inline fun padding(block: UIPadding.() -> Unit) {
        padding = UIPadding()
        padding!!.block()
    }

    inline fun margin(block: UIMargin.() -> Unit) {
        margin = UIMargin()
        margin!!.block()
    }

    inline fun animation(block: UIAnimation<*>.() -> Unit) {
        TODO("Animation block missing")
    }
}