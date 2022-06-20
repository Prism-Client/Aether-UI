package net.prismclient.aether.screens

import net.prismclient.aether.components.promotion.PromotionStyleSheet
import net.prismclient.aether.ui.component.type.input.textfield.UITextField
import net.prismclient.aether.ui.component.type.input.textfield.UITextFieldSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.impl.border.UIStrokeDirection
import net.prismclient.aether.ui.screen.UIScreen
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

            style(PromotionStyleSheet(), "Promotion") {
                size(189, 172)
                background(asRGBA(214, 214, 216, 0.3f)) {
                    border(asRGBA(255, 255, 255, 0.8f), 1f)
                    radius(10f)
                }

                buttonColor = asRGBA(87, 164, 255)
            }

            //component(PromotionComponent("This is some title of some sort o/", "Some description of some sort idk", "Click me!", "Promotion")) {}

            style(UITextFieldSheet(), "textField") {
                background(asRGBA(0, 0, 0, 0.1f), radius(8f)) {
                    border(asRGBA(214, 214, 216), 1f, UIStrokeDirection.INSIDE)
                }
                font {
                    align(UIAlignment.MIDDLELEFT)
                    x += px(20)
                    fontFamily = "Poppins"
                    fontSize = 14f
                    isSelectable = true
                    selectionColor = asRGBA(0, 120, 200, 0.3f)
                    textAlignment = UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE
                }
                clipContent = false
            }

            component(UITextField("", "Search for anything...", UITextField.any, "textField")) {
                style {
                    control(UIAlignment.TOPRIGHT)
                    size(448, 39)
                    x -= px(30); y = px(30)
                }
                onTextChanged("textListener") {
                    //println("This text has been changed to: ${it.text}")
                }
            }
        }
    }
}