package net.prismclient.aether.ui.component.type.layout.styles

import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit

open class UIFrameSheet(name: String) : UIStyleSheet(name) {
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

    override fun apply(sheet: UIStyleSheet): UIFrameSheet {
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

    override fun copy(): UIFrameSheet = UIFrameSheet(name).also {
        it.apply(this)
        it.frameWidth = frameWidth?.copy()
        it.frameHeight = frameHeight?.copy()
    }
}