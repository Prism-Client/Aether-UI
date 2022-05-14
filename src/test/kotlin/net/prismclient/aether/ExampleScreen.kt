package net.prismclient.aether

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.animation.impl.UIDefaultAnimation
import net.prismclient.aether.ui.component.type.color.UIColorSwatch
import net.prismclient.aether.ui.component.type.color.UIColorSwatchSheet
import net.prismclient.aether.ui.component.type.input.UITextField
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.input.slider.UISliderSheet
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNMIDDLE
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.impl.UITextFieldSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.EM
import net.prismclient.aether.ui.unit.util.HEIGHTANIM
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

        style(UIContainerSheet(), "frame") {
            anchor.align(UIAlignment.CENTER)

            x = percent(50)
            y = percent(50)
            width = px(700)
            height = px(700)

            contentRadius = radius(10f)

            background {
                color = asRGBA(24, 202, 255, 0.16f)
                radius = radius(10f)

                border {
                    borderColor = asRGBA(34, 202, 255, 0.85f)
                    borderWidth = 2f
                }
            }

//            overflowX = UIContainerSheet.Overflow.Scroll

            verticalScrollbar.x = px(10)
            verticalScrollbar.y = percent(10)
            verticalScrollbar.width = px(5)
            verticalScrollbar.height = percent(80)

            verticalScrollbar.radius = radius(2.5f)

            verticalScrollbar.background = UIBackground()
            verticalScrollbar.background!!.color = asRGBA(255, 0, 0)


            clipContent = true
        }

        style(UITextFieldSheet(), "textfield") {
            x = px(10) + px(10)
            y = percent(100) - px(10)
            width = rel(1) - px(10) - px(10)
            height = px(1000)

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
                y += descender(0.5)
            }

            padding {
                paddingLeft = px(10)
            }
        }

        style(UISliderSheet(), "slider") {
            position(px(50), px(50))
            size(px(400), px(5))

            sliderControl.color = -1

            background {
                radius = radius(2.5f)
                color = asRGBA(255, 255, 255, 0.3f)
                border {
                    borderWidth = 1f
                    borderColor = asRGBA(255, 255, 255, 0.75f)
                }
            }
        }

//        val frame = UIContainer<UIContainerSheet>("frame")
//        val swatch = UIColorSwatch("swatch")
//
//        frame.addComponent(swatch)
//
//        frames.add(frame)
//        components.add(frame)

        val slider = UISlider(5f, 0f, 10f, 1f, "slider")

        components.add(slider)

        update()
    }

    override fun render() {
//        renderer {
//            renderImage("background", 0f, 0f, UICore.width, UICore.height)
//        }
        super.render()
        //renderGradient(50f, 50f, 500f, 500f)
    }
}