package net.prismclient.aether.ui.style.impl

import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.component.type.layout.UIFrame

class UIFrameSheet : UIStyleSheet() {
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

        it.clipContent = clipContent
        it.contentRadius = contentRadius

        return it
    }
}