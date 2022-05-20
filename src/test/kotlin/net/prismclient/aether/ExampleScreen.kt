package net.prismclient.aether

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.animation.impl.UIDefaultAnimation
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.input.slider.UISliderSheet
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNCENTER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNMIDDLE
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNTOP
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.impl.UITextFieldSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.EM
import net.prismclient.aether.ui.unit.util.WIDTHANIM
import net.prismclient.aether.ui.util.extensions.*

class ExampleScreen : UIScreen() {
    override fun initialize() {
        // TODO: dependsOn(StyleClass.class)
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
            loadSvg("svg", "/aether/svg/message-text.svg", 4f)
        }


        style(UIContainerSheet(), "frame") {
            anchor.align(UIAlignment.CENTER)

            x = percent(50)
            y = percent(50)
            width = px(700)
            height = px(500)

            contentRadius = radius(10f)

            background {
                color = asRGBA(24, 202, 255, 0.16f)
                radius = radius(10f)

                border {
                    borderColor = asRGBA(34, 202, 255, 0.85f)
                    borderWidth = 2f
                }
            }

            verticalScrollbar {
                x = px(10)
                y = percent(10)
                width = px(5)
                height = percent(80)
                radius = radius(2.5f)

                background {
                    color = asRGBA(255, 255, 0)
                }
            }
        }

        style(UIContainerSheet(), "frame") {
            anchor.align(UIAlignment.CENTER)

            x = percent(50)
            y = percent(50)
            width = px(700)
            height = px(500)

            contentRadius = radius(10f)

            background {
                color = asRGBA(24, 202, 255, 0.16f)
                radius = radius(10f)

                border {
                    borderColor = asRGBA(34, 202, 255, 0.85f)
                    borderWidth = 2f
                }
            }

            verticalScrollbar {
                x = px(10)
                y = percent(10)
                width = px(5)
                height = percent(80)
                radius = radius(2.5f)

                background {
                    color = asRGBA(255, 255, 0)
                }
            }
        }

        build {
            style("h1") {
                font {
                    align(UIAlignment.TOPLEFT)
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Bold
                    textAlignment = ALIGNTOP or ALIGNLEFT
                    fontSize = 48f
                    fontColor = -1
                }

                clipContent = false
            }

            style("h2") {
                font {
                    align(UIAlignment.TOPLEFT)
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Bold
                    textAlignment = ALIGNTOP or ALIGNLEFT
                    fontSize = 32f
                    fontColor = -1
                }

                clipContent = false
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

            style(UIStyleSheet(), "btn") {
                background(asRGBA(0, 0, 0, 0.3f)) {
                    radius = radius(15f)
                }
                x = px(200)
                y = px(200)
                width = px(150)
                height = px(50)

                font {
                    align(UIAlignment.CENTER)
                    textAlignment = ALIGNMIDDLE or ALIGNCENTER
                    fontFamily = "Poppins"
                    fontSize = 16f
                    fontType = UIFont.FontType.Light
                }
            }

            animation(UIDefaultAnimation("fadeIn")) {
                keyframe(UIQuart(250L), true) {
                    background {
                        color = asRGBA(0f, 0f, 0f, 0.5f)
                    }
                    animationResult = UIAnimationResult.Retain
                    keep() // Same thing as above, and in the method params of the block function
                }
            }

            animation(UIDefaultAnimation("fadeOut")) {
                keyframe(UIQuart(1000L), true) {
                    background {
                        color = asRGBA(0f, 0f, 0f, 0.3f)
                    }
                }
            }

            val label = h2("Example Screen") {
                style {
                    align(UIAlignment.TOPLEFT)
                    anchor(UIAlignment.TOPLEFT)
                    position(10f, 10f)
                }
            }

            slider(1000f, 0f, 1000f, 100f, "slider")

            button("Hover over me!", "btn") {

            }.onMouseEnter {
                UIProvider.dispatchAnimation("fadeIn", it)
            }.onMouseLeave {
                UIProvider.dispatchAnimation("fadeOut", it)
            }
        }

//        UIProvider.dispatchAnimation("animation", slider)
    }
}