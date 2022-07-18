package examples

import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.font.UITextAlignment
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.create
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.colorOf
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.rel
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
            container {
//                button("Hello World!\nHow is the world doing?").style {
//                    plot(-20, -20, 400, 140)
//                    background(asRGBA(0f, 0f, 0f, 0.3f), 8)
//                    font("Montserrat-light", colorOf(-1), px(24), px(10))
//                    font(UIAlignment.CENTER, rel(0.5), rel(0.5))
//                }
                text("hello\n\nHow are you doing?").style {
                    font("Montserrat-light", colorOf(-1), px(24), px(10))
                    font(UIAlignment.CENTER, rel(0.5), rel(0.5))
                    font(UITextAlignment.LEFT, UITextAlignment.TOP)
                    background(asRGBA(0f, 1f, 0f, 1f))
                    control(UIAlignment.CENTER)
                }
            }.style {
                control(UIAlignment.CENTER)
                size(500, 500)
                useFBO = true
            }
        }
    }
}