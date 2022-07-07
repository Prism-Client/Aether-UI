package net.prismclient.aether.screens

import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*

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
            renderer {
                loadSvg("cog", "/prism/icons/navbar/setting.svg")
            }

            styleOf(UIStyleSheet("someComponent")) {
                size(100, 40)
                background(colorOf(asRGBA(0f, 0f, 0f, 0.3f)))
                font("Montserrat", px(16f), colorOf(-1))
            }

            component(UIAutoLayout(UIListLayout.ListDirection.Horizontal, null)) {
                componentAlignment = UIAlignment.TOPCENTER
                horizontalResizing = UIAutoLayout.ResizingMode.Hug
                verticalResizing = UIAutoLayout.ResizingMode.Hug
                componentSpacing = px(10)
                layoutPadding = paddingOf(10)

                image("cog").style(UIImageSheet("")) {
                    size(24, 24)
                }
                label("Settings").style(UIStyleSheet("")) {
                    font("Montserrat", px(16f), colorOf(-1), left or top)
                    clipContent = false
                }
            }.style(UIContainerSheet("")) {
                position(100, 100)
                background(colorOf(asRGBA(59, 145, 255)), radiusOf(9f))
            }

            container {

            }.style(UIContainerSheet("")) {
                control(UIAlignment.CENTER)
                size(400f, 400f)
                background(asRGBA(0, 0, 0, 0.3f), radiusOf(8f))
            }

//
//            container().style(UIContainerSheet("")) {
//                control(UIAlignment.CENTER)
//                size(400f, 400f)
//                background(asRGBA(0, 0, 0, 0.3f), radiusOf(8f))
//                verticalScrollbar {
//                    color = -1
//                    width = px(10)
//                    height = rel(1f)
//                }
//                useFBO = true
//                button("Click me!", "someComponent") {
//                    onMousePressed {
//                        this.text = Math.random().toString()
//                    }
//                }
////                for (i in 0 .. 25) {
////                    button("$i", "someComponent")
////                }
//            }

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