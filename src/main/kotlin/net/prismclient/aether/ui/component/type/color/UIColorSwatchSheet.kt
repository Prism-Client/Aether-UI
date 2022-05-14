package net.prismclient.aether.ui.component.type.color

import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.setAlpha

class UIColorSwatchSheet : UIStyleSheet() {
    var swatchColor = -1
        set(value) {
            field = value.setAlpha(255)
        }

    override fun copy(): UIStyleSheet {
        val it = UIColorSwatchSheet()
        it.apply(this)
        it.swatchColor = swatchColor
        return it
    }
}