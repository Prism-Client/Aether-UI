package net.prismclient.aether

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.animation.impl.UIDefaultAnimation
import net.prismclient.aether.ui.component.type.input.UITextField
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNMIDDLE
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.impl.UIFrameSheet
import net.prismclient.aether.ui.style.impl.UITextFieldSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.unit.util.*
import net.prismclient.aether.ui.util.extensions.*

class ExampleScreen : UIScreen() {

    override fun initialize() {
        UIFontFamily(
                "Poppins",
                "/fonts/",
                "regular",
                "black",
                "bold",
                "light",
                "thin"
        )

        renderer {
            loadImage("background", "/images/background.png")
        }

        style(UIFrameSheet(), "frame") {
            anchor.align(UIAlignment.CENTER)

            x = percent(50)
            y = percent(50)
            width = px(350)
            height = px(350)

            contentRadius = radius(10f)

            background {
                color = asRGBA(24, 202, 255, 0.16f)
                radius = radius(10f)

                border {
                    borderColor = asRGBA(34, 202, 255, 0.85f)
                    borderWidth = 2f
                }
            }

            clipContent = true
        }

        style(UITextFieldSheet(), "textfield") {
            x = px(15)
            y = px(5)
            width = px(250)
            height = px(30)

            caretY = UIUnit(-0.5f, EM)
            caretWidth = px(1)
            caretColor = asRGBA(255, 255, 255, 0.6f)
            selectionColor = asRGBA(137, 207, 240, 0.3f)

            background {
                color = asRGBA(1f, 1f, 1f, 0.15f)
                radius = radius(10f)
            }

            font {
                align(UIAlignment.MIDDLELEFT)
                fontFamily = "Poppins"
                fontSize = 16f
                fontColor = -1
                fontType = UIFont.FontType.Regular
                textAlignment = ALIGNLEFT or ALIGNMIDDLE

                // Adjust the font to be the center of the text
                y = UIOperationUnit(0.5f, RELATIVE, UIUnit(0.5f, DESCENDER), UIOperation.ADD)
            }

            padding {
                paddingLeft = px(10)
            }
        }

        val frame = UIFrame("frame")
        val textfield = UITextField("", "text here", UITextField.Companion.number, style = "textfield")

        animation(UIDefaultAnimation("test")) {
            keyframe {
                y = unit(-1, HEIGHTANIM)
            }
            keyframe(UIQuart(1000))
        }

        animation(UIDefaultAnimation("other-animation")) {
            keyframe {
                width = px(0)
            }
            keyframe(UIQuart(1000), 1000L)
        }

        UIProvider.dispatchAnimation("test", frame)
        UIProvider.dispatchAnimation("other-animation", textfield)


        frame.addComponent(textfield)

        frames.add(frame)
        components.add(frame)

        update()
    }

    override fun render() {
//        renderer {
//            renderImage("background", 0f, 0f, UICore.width, UICore.height)
//        }
        super.render()
    }
}