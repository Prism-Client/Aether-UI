package net.prismclient.aether.ui.component.type.color

import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.setAlpha

class UIColorSwatchSheet(name: String) : UIStyleSheet(name) {
    var swatchColor = -1
        set(value) {
            field = value.setAlpha(255)
        }

    override fun copy(): UIColorSwatchSheet = UIColorSwatchSheet(name).also {
        it.apply(this)
        it.swatchColor = swatchColor
    }
}