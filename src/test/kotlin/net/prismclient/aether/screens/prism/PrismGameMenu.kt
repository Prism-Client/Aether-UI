package net.prismclient.aether.screens.prism

import examples.deps.Generic
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*

class PrismGameMenu : UIScreen {
    override fun build() {
        UIFontFamily("Poppins", "/prism/fonts/poppins/", "regular", "medium", "black", "bold", "light", "thin")

        create {
            include(Generic())

            renderer {
                UIAssetDSL.image("Logo", "/prism/icons/logo/Logo-Big.png")
                UIAssetDSL.image(
                    "menu-bg",
                    "/prism/backgrounds/main-menu.png"
                )
            }

            styleOf(UIStyleSheet("button")) {
                control(UIAlignment.CENTER)
                size(320, 32)
                background(colorOf(asRGBA(0, 0, 0, 0.1f)), radiusOf(15f))
                font("Poppins", px(18f), colorOf(-1), middle or center) {
                    align(UIAlignment.CENTER)
                }
            }

            styleOf(UIImageSheet("logo")) {
                control(UIAlignment.MIDDLELEFT)
                size(55, 61)
            }

            styleOf(UIStyleSheet("title")) {
                control(UIAlignment.TOPRIGHT)
                font("Poppins", px(50), colorOf(-1), top or right, UIFont.FontType.Light) {
                    fontSpacing = px(50 * 0.385f)
                }
                clipContent = false
            }

            image("menu-bg").style(UIImageSheet("")) {
                size(rel(1), rel(1))
            }

            container {
                image("Logo", style = "logo")
                label("PRISM", "title")
            }.style(UIContainerSheet("titleContainer")) {
                control(UIAlignment.CENTER)
                size(300, 48)
                y -= px(118)
                clipContent = false
            }

            autoLayout(UIAutoLayout(UIListLayout.ListDirection.Vertical, null).style(UIContainerSheet("")) {}) {
                button("Singleplayer", "button")
                button("Multiplayer", "button")

                verticalResizing = UIAutoLayout.ResizingMode.Hug
                horizontalResizing = UIAutoLayout.ResizingMode.Hug
                componentSpacing = px(10)
            }.style {
                control(UIAlignment.CENTER)
            }

        }

    }
}