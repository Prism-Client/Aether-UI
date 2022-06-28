package net.prismclient.aether.screens

import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.descender
import net.prismclient.aether.ui.util.extensions.plus

class TestingScreen : UIScreen {
    override fun build() {
        UIFontFamily(
            "Montserrat",
            "/prism/fonts/montserrat/",
            "Montserrat-regular",
            "Montserrat-medium",
            "Montserrat-black",
            "Montserrat-bold",
            "Montserrat-light",
            "Montserrat-thin"
        )
        buildScreen {
            button("HIIIII").style(UIStyleSheet("")) {
                control(CENTER)
                size(400, 40)
                background(asRGBA(0, 0, 0, 0.3f), radiusOf(8f))
                font("Montserrat", 20f, textAlignment = middle or center) {
                    align(CENTER)
                    y += descender(0.5f)
                }
            }
        }
    }
}