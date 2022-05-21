package net.prismclient.aether.ui.component.type.image

import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet

class UIImageSheet : UIStyleSheet() {
    var imageRadius: UIRadius? = null

    override fun copy(): UIImageSheet {
        val sheet = UIImageSheet()
        sheet.apply(this)
        sheet.imageRadius = imageRadius?.copy()
        return sheet
    }
}