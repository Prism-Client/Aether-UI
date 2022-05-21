package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.animation.impl.UIDefaultAnimation
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.type.input.slider.UISliderSheet
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNCENTER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNMIDDLE
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.impl.UITextFieldSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.EM
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

        build {
            // Using the dependsOn feature, we can load styles
            // from these classes to avoid boilerplate code
            dependsOn(::TextStyles)
            dependsOn(::IconStyles)
            dependsOn(::ComponentStyles)
            dependsOn(::AnimationStyles)

            style(UIContainerSheet(), "container") {
                control(UIAlignment.CENTER)
                background(asRGBA(0, 255, 0, 0.3f))

                width = px(400)
                height = px(400)

                overflowX = UIContainerSheet.Overflow.Scroll
                overflowY = UIContainerSheet.Overflow.Scroll

                verticalScrollbar {
                    x = px(10)
                    y = percent(10)
                    width = px(5)
                    height = percent(80)
                    radius = radius(2.5f)

                    color = -1

                    background(asRGBA(0, 0, 0, 0.3f)) {
                        radius = radius(2.5f)
                    }
                }

                horizontalScrollbar {
                    x = percent(10)
                    y = px(10)
                    width = percent(80)
                    height = px(5)
                    radius = radius(2.5f)

                    color = -1

                    background(asRGBA(0, 0, 0, 0.3f)) {
                        radius = radius(2.5f)
                    }
                }
            }

            style(UIStyleSheet(), "tall-btn") {
                background(asRGBA(255, 0, 0, 1f))

                x = px(50)
                y = px(50)
                width = px(1000)
                height = px(1000)
            }

            container("container") {
                button("Really tall button", "tall-btn")
            }

            return

            h1("Header 1") {
                style {
                    control(UIAlignment.TOPLEFT, UIAlignment.TOPLEFT)
                    position(10f, 10f)
                }
            }

            h2("Header 2") {
                style {
                    control(UIAlignment.TOPLEFT, UIAlignment.TOPLEFT)
                    position(10f, 58f)
                }
            }

            h3("Header 3") {
                style {
                    control(UIAlignment.TOPLEFT, UIAlignment.TOPLEFT)
                    position(10f, 90f)
                }
            }

            p("This is a paragraph! Write some paragraphical text here which will convey a message of some sort!") {
                style {
                    control(UIAlignment.TOPLEFT, UIAlignment.TOPLEFT)
                    position(10f, 114f)
                    font {
                        fontRenderType = UIFont.FontRenderType.WRAP
                        lineBreakWidth = 300f
                    }
                }
            }

            //slider(1000f, 0f, 1000f, 100f, "slider")
            button("Hover over me!", "btn") {
                style {
                    background {
                        border {}
                    }
                }
            }.hover("fadeIn", "fadeOut")
        }
    }

    override fun render() {
        super.render()
    }
}