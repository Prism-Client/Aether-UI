package net.prismclient.aether

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.animation.impl.UIDefaultAnimation
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.input.slider.UISliderSheet
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNMIDDLE
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNTOP
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL.font
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.impl.UITextFieldSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.EM
import net.prismclient.aether.ui.unit.util.WIDTHANIM
import net.prismclient.aether.ui.util.extensions.*

class ExampleScreen : UIScreen() {
    lateinit var slider: UISlider

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
                    fontType = UIFont.FontType.Regular
                    textAlignment = ALIGNTOP or ALIGNLEFT
                    fontSize = 16f
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

            animation(UIDefaultAnimation("animation")) {
                keyframe {
                    x = unit(-1f, WIDTHANIM)
                }
                keyframe(UIQuart(1000L)) {
                    x = px(50)
                }
            }

            container("frame") {
                val label = h1(
                        "The quick brown fox jumps over the lazy dog! Or something" +
                        " like that. Anyway, this is a multiline label which allows" +
                        " for multiple lines, as the name suggests!"
                ) {
                    style {
                        align(UIAlignment.CENTER)
                        anchor(UIAlignment.CENTER)
                        background(asRGBA(255, 255, 255, 0.3f))
                        padding(10f)

                        font {
                            fontRenderType = UIFont.FontRenderType.WRAP
                            appendedString = ".."
                        }
                    }
                }

                slider = slider(1000f, 0f, 1000f, 100f, "slider")
                slider.onValueChanged(true) {
                    label.style.font!!.lineBreakWidth = it.value
                }
            }
        }

        UIProvider.dispatchAnimation("animation", slider)
    }

    override fun render() {
        super.render() // Breakpoint debugging
    }
}