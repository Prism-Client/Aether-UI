package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.px

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

            container("container") {
                button("Hover over me!", "btn") {
                    style {
                        size(200f, 50f)
                        margin(10f)
                    }
                }.hover("fadeIn", "fadeOut")
                button("Click me!", "btn") {
                    style {
                        x = px(210f)
                        size(200f, 50f)
                        margin(10f)
                    }
                }.hover("fadeIn", "fadeOut").onMousePressed {
                    if (it.isMouseInsideBoundingBox()) {
                        (it as UIButton).text = "Yay! I've been clicked!"
                    }
                }.onMouseLeave {
                    (it as UIButton).text = "Click me!"
                }
                slider(1000f, 0f, 1000f, 100f, "slider") {
                    style {
                        position(0f, 70f)
                        size(200f, 5f)
                        margin(10f)
                    }
                }
                image("kazuha", "/images/kazuha.jpeg", "imag")
            }

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
        }
    }

}