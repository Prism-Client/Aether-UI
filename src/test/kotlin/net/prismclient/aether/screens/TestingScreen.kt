package net.prismclient.aether.screens

import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.*

class TestingScreen : UIScreen {
    override fun build() {
        build {
            UIFontFamily("Poppins", "/demo/fonts/", "regular", "black", "bold", "light", "thin")
            renderer {
                loadImage("background", "/demo/background.png")
            }

            style("style") {
                font("Poppins", 64f, -1, UIRenderer.ALIGNTOP or UIRenderer.ALIGNLEFT)
            }
            label("Hello\nWorld\nHow\nare\nyou?", "style").style {
                position(150f, 150f)
                background(asRGBA(0, 0, 0, 0.1f))
                font {
                    textAlignment = UIRenderer.ALIGNTOP or UIRenderer.ALIGNCENTER
                    isSelectable = true
                    fontRenderType = UIFont.FontRenderType.NEWLINE
                    selectionColor = asRGBA(0, 120, 200, 0.3f)
                    lineHeight = px(25)
                }
                clipContent = false
            }
        }
    }
}