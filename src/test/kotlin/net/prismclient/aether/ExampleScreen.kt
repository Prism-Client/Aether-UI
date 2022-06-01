package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNBASELINE
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNCENTER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNTOP
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.*

/**
 * Here is an example of how to use the Aether UI library.
 */
class ExampleScreen : UIScreen {
    override fun build() {
        // To define a font family, you must first create a UIFontFamily object.
        UIFontFamily("Poppins", "/demo/fonts/", "regular", "black", "bold", "light", "thin")

        build {
            // Dependencies make it easier to organize everything
            // and reduce boilerplate code. It's a simple interface
            // which is invoked immediately. You can store styles
            // and resources there, so they can be reused them late.
            dependsOn(::TextStyles)
            dependsOn(::IconStyles)
            dependsOn(::ComponentStyles)
            dependsOn(::AnimationStyles)

            // The dependencies above are ones that you might use
            // anywhere. Here is a one specific to this screen:
            dependsOn(::ExampleScreenStyles)

            renderer {
                loadImage("background", "/demo/background.png")
            }

            container(style = "container") {
                style {
                    control(UIAlignment.MIDDLELEFT)
                    x = px(10)
                    width = px(180)
                    height = rel(1f) - px(20) // 20 px padding
                    contentRadius = radius(24f)
                    background {
                        radius = radius(24f)
                    }
                    clipContent = true
                }

                list(UIListLayout.ListDirection.Vertical, UIListLayout.ListOrientation.Forward, "container") {
                    style {
                        size(rel(1f), rel(0.8f))
                        overflowX = UIContainerSheet.Overflow.None
//                        overflowY = UIContainerSheet.Overflow.None
                        background = null
                        clipContent = false
                    }

                    h1("PRISM") {
                        style {
                            margin(marginTop = 20f)
                            width = rel(1f)
                            height = em(1f) - descender(1f)

                            font {
                                align(UIAlignment.BOTTOMCENTER)
                                textAlignment = ALIGNBASELINE or ALIGNCENTER
                                fontType = UIFont.FontType.Light
                                fontSize = 24f
                                fontSpacing = 24 * 0.24f
                            }
                        }
                    }

                    button("Mods", "btn") {
                        style {
                            control(UIAlignment.CENTER)
                            margin(marginTop = 20f)
                            size(148f, 57f)
                        }
                        image("note", style = "icon")
                    }.hover("hoverEnter", "hoverLeave")

                    button("Settings", "btn") {
                        style {
                            control(UIAlignment.CENTER)
                            margin(marginTop = 5f)
                            size(148f, 57f)
                        }
                        image("settings", style = "icon")
                    }

                    for (i in 0..10) {
                        h2("Parties") {
                            style {
                                control(UIAlignment.CENTER)
                                margin(marginTop = 5f)

                                width = rel(1f)
                                height = em(1f)

                                background(asRGBA(255, 0, 0, 0.3f))

                                font {
                                    align(UIAlignment.TOPCENTER)
                                    textAlignment = ALIGNTOP or ALIGNCENTER
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}