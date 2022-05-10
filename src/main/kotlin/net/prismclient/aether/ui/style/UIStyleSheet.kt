package net.prismclient.aether.ui.style

import net.prismclient.aether.ui.animation.UIAnimation
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

        return this
    }

    /**
     * A DSL block for [UIBackground]
     */
    fun background(block: UIBackground.() -> Unit) {
        background = UIBackground()
        background!!.block()
    }

    /**
     * A DSL block for [UIFont]
     */
    fun font(block: UIFont.() -> Unit) {
        font = UIFont()
        font!!.block()
    }

    fun padding(block: UIPadding.() -> Unit) {
        padding = UIPadding()
        padding!!.block()
    }

    fun margin(block: UIMargin.() -> Unit) {
        margin = UIMargin()
        margin!!.block()
    }

    fun animation(block: UIAnimation<*>.() -> Unit) {
        TODO("Animation block missing")
    }
}