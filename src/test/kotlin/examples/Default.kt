package examples

import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.font.TextAlignment
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.create
import net.prismclient.aether.ui.util.extensions.colorOf
import net.prismclient.aether.ui.util.extensions.px
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
            button("Hello World! I really like trains. Also this text breaks at EXACTLY 500 pixels! It has a line spacing of 10 pixels!!! Don't forget, the initial size is (500x100)! The text will increase the height if it's too small! (Hint: it is too small!! jk its not its 600)") {

            }.style {
                position(20,20)
                font {
                    width = px(500)
                    height = px(600)
                    fontName = "Montserrat-light"
                    fontColor = colorOf(-1)
                    fontSize = px(24)
                    lineHeightSpacing = px(10)
                    verticalAlignment = TextAlignment.LEFT
                    horizontalAlignment = TextAlignment.CENTER
                    textResizing = UIFont.TextResizing.FixedSize
                }
            }
        }
    }
}