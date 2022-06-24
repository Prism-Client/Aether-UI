package net.prismclient.aether.screens

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.other.progress.UIProgress
import net.prismclient.aether.ui.component.type.other.progress.UIProgressSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.*

class PrismLoadingMenu : UIScreen {
    override fun build() {
        build {
            UIFontFamily("Poppins", "/prism/fonts/poppins/", "regular", "medium", "black", "bold", "light", "thin")

            style(UIImageSheet(), "background") {
                size(rel(1f), rel(1f))
                imageColor = asRGBA(0.9f, 0.9f, 0.9f)
                immutable = true
            }

            style(UIImageSheet(), "logo") {
                control(UIAlignment.CENTER)
                size(108, 118)
                y -= px(19)
            }

            style(UIProgressSheet(), "progress") {
                control(UIAlignment.CENTER)
                size(300, 10)
                y += px(100)
                background(asRGBA(255, 255, 255, 0.31f), radius(5f))
            }

            style("descriptor") {
                control(UIAlignment.CENTER)
                y += px(115)
                font("Poppins", 18f, -1, UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP, UIFont.FontType.Medium)
                clipContent = false
            }

            image("menu-bg", "/prism/mainmenu/background.png", "background")
            image("Logo-Big", "/prism/icons/Logo-Big.png", "logo").onMousePressed {
                UICore.displayScreen(PrismMainMenu())
            }

            component(UIProgress(0.5f, "progress")) {}
            label("Preparing", "descriptor")
        }
    }
}