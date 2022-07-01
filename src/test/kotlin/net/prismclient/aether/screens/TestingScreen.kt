package net.prismclient.aether.screens

import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.create
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.rel
import net.prismclient.aether.ui.util.radiusOf
import net.prismclient.aether.ui.util.style
import net.prismclient.aether.ui.util.styleOf

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
        create {
            styleOf(UIStyleSheet("someComponent")) {
                size(100, 40)
                background(asRGBA(0f, 0f, 0f, 0.3f))
                font("Montserrat", 16f, -1)
            }

            container().style(UIContainerSheet("")) {
                control(UIAlignment.CENTER)
                size(400f, 400f)
                background(asRGBA(0, 0, 0, 0.3f), radiusOf(8f))
                verticalScrollbar {
                    color = -1
                    width = px(10)
                    height = rel(1f)
                }
                useFBO = true
                button("Click me!", "someComponent") {
                    onMousePressed {
                        this.text = Math.random().toString()
                    }
                }
//                for (i in 0 .. 25) {
//                    button("$i", "someComponent")
//                }
            }

//            button("HIIIII").style(UIStyleSheet("")) {
//                control(CENTER)
//                size(400, 40)
//                background(asRGBA(0, 0, 0, 0.3f), radiusOf(8f))
//                font("Montserrat", 20f, textAlignment = middle or center) {
//                    align(CENTER)
//                    y += descender(0.5f)
//                }
//            }
        }
    }
}