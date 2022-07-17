package examples

import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.font.TextAlignment
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.create
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.left
import net.prismclient.aether.ui.util.style

class Default : UIScreen {
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
            button("Hello\nWorld!") {

            }.style {
                position(20,20)
                size(400, 140)
                background(colorOf(asRGBA(0f, 0f, 0f, 0.3f)))
                font {
                    anchor(UIAlignment.CENTER)
                    x = rel(0.5)
                    y = rel(0.5)
                    size(rel(1), rel(1))
                    fontName = "Montserrat-light"
                    fontColor = colorOf(-1)
                    fontSize = px(24)
                    lineHeightSpacing = px(10)
                }
            }
        }
    }
}