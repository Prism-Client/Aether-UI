package net.prismclient.aether.ui.component.type.layout.styles

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit

open class UIFrameSheet : UIStyleSheet() {
    override var width: UIUnit? = null
        set(value) {
            if (frameWidth == width)
                frameWidth = value
            field = value
        }
    override var height: UIUnit? = null
        set(value) {
            if (frameHeight == height)
                frameHeight = value
            field = value
        }

    var frameWidth: UIUnit? = width
    var frameHeight: UIUnit? = height

    override fun animate(previous: UIStyleSheet?, current: UIStyleSheet?, progress: Float, component: UIComponent<*>) {
        super.animate(previous, current, progress, component)
        (component as UIFrame).updateFrame()
    }

    override fun apply(sheet: UIStyleSheet): UIFrameSheet {
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

        this.frameWidth = frameWidth?.copy()
        this.frameHeight = frameHeight?.copy()

        return this
    }

    override fun copy(): UIFrameSheet = UIFrameSheet().also {
        it.apply(this)
    }
}