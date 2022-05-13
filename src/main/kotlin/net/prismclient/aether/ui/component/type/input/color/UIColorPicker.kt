package net.prismclient.aether.ui.component.type.input.color

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.renderer

class UIColorPicker(style: String) : UIComponent<UIStyleSheet>(style) {
    init {
        renderer {
            loadImage("color-picker", "/aether/colorpicker/color-space.png")
        }
    }


    override fun renderComponent() {

    }
}