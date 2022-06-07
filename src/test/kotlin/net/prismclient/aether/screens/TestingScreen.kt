package net.prismclient.aether.screens

import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNBASELINE
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNBOTTOM
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNCENTER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNTOP
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.*

class TestingScreen : UIScreen {
    override fun build() {
        build {
            UIFontFamily("Poppins", "/demo/fonts/", "regular", "black", "bold", "light", "thin")
            renderer {
                loadImage("background", "/demo/background.png")
                loadSvg("image", "/demo/icons/game.svg")
            }

            style(UIStyleSheet(), "LargeButton") {
                size(340, 100)
                background(asRGBA(0, 0, 0, 0.1f), radius(16f)) {
                    border {
                        borderDirection = UIRendererDSL.StrokeDirection.INSIDE
                        borderColor = asRGBA(255, 255, 255, 0.5f)
                        borderWidth = 2f
                    }
                }
                font {
                    x = px(24 + 48 + 24)
                    y = px(28)
                    textAlignment = ALIGNTOP or ALIGNLEFT
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Bold
                    fontSize = 14f
                    fontColor = -1
                }
            }

            style(UIImageSheet(), "LargeButtonIcon") {
                control(UIAlignment.MIDDLELEFT)
                size(48f, 48f)
                x += px(28)
                imageColor = asRGBA(214, 214, 216, 1f)
            }

            style(UIStyleSheet(), "LargeButtonDescription") {
                x = px(24 + 48 + 24)
                y = px(28) + px(14f)
                background(asRGBA(0, 0, 255, 0.3f))
                font {
                    textAlignment = ALIGNTOP or ALIGNLEFT
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Regular
                    fontSize = 10f
                    fontColor = -1
                }
            }

            button("Controls", "LargeButton", "image", "LargeButtonIcon") {
                text("This is a short description.", "LargeButtonDescription").parent = this
            }
        }
    }
}