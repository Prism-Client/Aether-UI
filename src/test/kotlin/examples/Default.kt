package examples

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.type.layout.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.UILayoutDirection
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.animationOf
import net.prismclient.aether.ui.util.create
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.colorOf
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.rel
import net.prismclient.aether.ui.util.style

class Default : UIScreen {
    override fun build() {
        UIFontFamily(
            "Montserrat",
            "/prism/fonts/montserrat/",
            "Montserrat-regular",
            "Montserrat-medium",
            "Montserrat-black",
            "Montserrat-bold",
            "Montserrat-light",
            "Montserrat-thin"
        )

        create {
            style(UIStyleSheet(), "button") {
                size(260, 50)
                background(-1, 12)
                font("Montserrat-medium", colorOf(32, 32, 32), px(18)) {
                    font(UIAlignment.CENTER, rel(0.5), rel(0.5))
                }
            }

            animationOf("blue-hover", UIStyleSheet()) {
                UIQuart(1000L) to {
                    background(colorOf(asRGBA(87, 164, 255, 0.8f)))
                }
            }

            animationOf("blue-unhover", UIStyleSheet()) {
                keyframe(UIQuart(1000L), UIProvider.getStyle("button", true))
            }

            autoLayout(UILayoutDirection.Vertical) {
                hug() space 70
                autoLayout(UILayoutDirection.Vertical) {
                    space(10)
                    componentAlignment = UIAlignment.CENTER
                    horizontalResizing = UIAutoLayout.ResizingMode.Hug
                    verticalResizing = UIAutoLayout.ResizingMode.Hug

                    label("Welcome to your dashboard!").style {
                        font("Montserrat-bold", colorOf(-1), px(24))
                    }
                    label("Customize the dashboard to show your statistics from Bedwars to Skyblock, or even Minemen statistics!").style {
                        width = rel(1)
                        height = px(20)
                        font("Montserrat-regular", colorOf(1f, 1f, 1f, 0.8f), px(14)) {
                            width = rel(1)
                        }
                    }
                }.style {
                    width = rel(1)
                }
                autoLayout(UILayoutDirection.Horizontal) {
                    hug() space 10

                    button("Show me how!", "button").style {
                        font {
                            fontColor = colorOf(-1)
                        }
                        background(asRGBA(87, 164, 255))
                    }
                    button("Maybe later...", "button")
                }
            }.style {
                control(UIAlignment.CENTER)
            }
        }


//            container {
//                text("How are you doing? I REALLY love line breaks! They make lines have breakings!").style {
//                    font("Montserrat-light", colorOf( 37, 39, 51), px(24), px(10)) {
//                        width = px(150)
//                        anchor(UIAlignment.CENTER)
//                        align(UITextAlignment.LEFT, UITextAlignment.TOP)
//                        x = rel(0.5)
//                        y = rel(0.5)
//                    }
//                    background(-1, 8)
//                    control(UIAlignment.CENTER)
//                    padding(50)
//                }
//            }.style {
//                control(UIAlignment.CENTER)
//                size(500, 500)
//                useFBO = true
//            }
    }
}