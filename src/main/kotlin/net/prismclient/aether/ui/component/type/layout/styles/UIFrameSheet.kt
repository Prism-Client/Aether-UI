package net.prismclient.aether.ui.component.type.layout.styles

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

    /**
     * When true, any content within the [UIFrame] will be clipped
     */
    var clipContent = true

    /**
     * The corner radius of the content
     */
    var contentRadius: UIRadius? = null

    override fun copy(): UIStyleSheet {
        val it = UIFrameSheet()

        it.apply(this)

        it.frameWidth = frameWidth?.copy()
        it.frameHeight = frameHeight?.copy()

        it.clipContent = clipContent
        it.contentRadius = contentRadius

        return it
    }
}