package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.controller.impl.selection.UISelectableController
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.button.UIImageButton
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
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
                loadImage("logo", "/demo/logo.png")
            }

            style(UIStyleSheet(), "activeCrumb") {
                background(asRGBA(87, 164, 255, 0.1f))
                size(rel(1f), px(40f))
                font {
                    align(UIAlignment.CENTER)
                    x = px(72f)
                    y += descender(0.5f)
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Regular
                    textAlignment = UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE
                    fontSize = 14f
                    fontColor = asRGBA(87, 164, 255)
                }
            }

            style(UIStyleSheet(), "crumb") {
                size(rel(1f), px(40f))

                font {
                    align(UIAlignment.CENTER)
                    x = px(72f)
                    y += descender(0.5f)
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Regular
                    textAlignment = UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE
                    fontSize = 14f
                    fontColor = asRGBA(65, 63, 68)
                }
            }

            style(UIImageSheet(), "activeCrumbImage") {
                control(UIAlignment.MIDDLELEFT)
                size(24f, 24f)
                margin(marginLeft = 24f)
                imageColor = asRGBA(87, 164, 255)
            }

            style(UIImageSheet(), "crumbImage") {
                control(UIAlignment.MIDDLELEFT)
                size(24f, 24f)
                margin(marginLeft = 24f)
                imageColor = asRGBA(65, 63, 68)
            }
            style(UIStyleSheet(), "blueButton") {
                background(asRGBA(87, 164, 255, 0.1f)) {
                    radius(8f)
                }
                font {
                    align(UIAlignment.CENTER)
                    y += descender(0.5f)
                    textAlignment = UIRenderer.ALIGNMIDDLE or UIRenderer.ALIGNCENTER
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Bold
                    fontSize = 16f
                    fontColor = asRGBA(87, 164, 255)
                }
            }

            animation(UIStyleSheet(), "crumbHover") {
                keyframe {  }
                keyframe(UIQuart(250L)) {
                    background {
                        backgroundColor = asRGBA(0, 0, 0, 0.3f)
                    }
                    animationResult = UIAnimationResult.Retain
                }
            }

            animation(UIStyleSheet(), "crumbLeave") {
                keyframe {
                    background {
                        backgroundColor = asRGBA(0, 0, 0, 0.3f)
                    }
                }
                keyframe(UIQuart(250L)) {
                    background {
                        backgroundColor = 0
                    }
                }
            }

            // sidebarEaseIn
            animation(UIContainerSheet(), "sidebarEaseIn") {
                keyframe {
                    x = px(-236f)
                }
                keyframe(UIQuart(1000L)) {
                    x = px(0f)
                }
            }

            // Sidebar
            val c = container("container") {
                style {
                    align(UIAlignment.TOPLEFT)
                    size(px(236f), rel(1f))
                    background(-1) {
                        radius = null
                    }
                    contentRadius = radius(0f)
                    clipContent = true
                }
                image("logo", style = "imag") {
                    style {
                        control(UIAlignment.TOPCENTER)
                        size(44f, 65f)
                        margin(marginTop = 25f)
                    }
                }
                list(UIListLayout.ListDirection.Vertical, UIListLayout.ListOrientation.Forward, "container") {
                    style {
                        y = px(65 + 25 + 32)
                        size(rel(1f), rel(1f) - px(65 + 25 + 32))
                        clipContent = true
                        verticalScrollbar {
                            color = asRGBA(87, 164, 255)
                        }
                    }

                    p("MENU").style {
                        margin(marginLeft = 24f, marginBottom = 10f)
                        height = px(16f) // TODO: Font size calculations
                        font {
                            fontColor = asRGBA(191, 189, 193)
                        }
                    }

                    animation(UIStyleSheet(), "hover") {
                        keyframe {}
                        keyframe(UIQuart(250L)) {
                            background {
                                backgroundColor = asRGBA(0, 0, 0, 0.3f)
                            }
                        }
                    }

                    animation(UIStyleSheet(), "leave") {
                        keyframe {}
                        keyframe(UIQuart(250L)) {
                            background {
                                backgroundColor = asRGBA(0, 0, 0, 0)
                            }
                        }
                    }

                    // TODO: Fix selectable component depth might throw error?
                    // TODO: "ignore" block to ignore certain components but retain the layout
                    selectable<UIImageButton> {
                        onSelection {
                            it.applyStyle("activeCrumb")
                            it.image.applyStyle("activeCrumbImage")
                        }
                        onDeselection {
                            it.applyStyle("crumb")
                            it.image.applyStyle("crumbImage")
                            it.hover("hover", "leave")
                        }


                        button("Mods", "crumb", "note", "crumbImage")
                        button("Settings", "crumb", "setting", "crumbImage")
                        button("Store", "crumb", "bag", "crumbImage")
                        button("Profiles", "crumb", "profile", "crumbImage")

                        this.components.forEach { it.onMousePressed { c -> this.selectComponent(c)} }
                        this.selectComponent(0)
                    }

                    p("SOCIAL").style {
                        margin(20f, 0f, 10f, 24f)
                        height = px(16f) // TODO: Font size calculations
                        font(asRGBA(191, 189, 193))
                    }

                    button("Messages", "crumb","message", "crumbImage")
                    button("Friends", "crumb", "friends", "crumbImage")
                    button("Achievements", "crumb", "trophy", "crumbImage")
                    button("Recordings", "crumb", "recording", "crumbImage")

                    // Support
                    container("container") {
                        style {
                            control(UIAlignment.TOPCENTER)
                            size(189f, 172f)
                            margin(marginTop = 20f, marginBottom = 20f)
                            background(asRGBA(245, 245, 245)) {
                                radius(10f)
                                border {
                                    borderColor = asRGBA(214, 214, 214)
                                    borderWidth = 1f
                                }
                            }
                            clipContent = true
                        }
                        p("Get 3 months of cosmetics for free") {
                            style {
                                size(189, 80)
                                font {
                                    align(UIAlignment.TOPCENTER)
                                    y += px(20f)
                                    textAlignment = UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP
                                    fontRenderType = UIFont.FontRenderType.WRAP
                                    fontColor = asRGBA(0, 0, 0)
                                    lineBreakWidth = 189f
                                    fontSize = 16f
                                }
                            }
                        }
                        p("Support Prism's development by unlocking cosmetics.") {
                            style {
                                size(179, 80)
                                font {
                                    align(UIAlignment.TOPCENTER)
                                    y += px(60f)
                                    textAlignment = UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP
                                    fontRenderType = UIFont.FontRenderType.WRAP
                                    fontColor = asRGBA(65, 63, 68)
                                    lineBreakWidth = 179f
                                    fontSize = 14f
                                }
                            }
                        }
                        button("GET PREMIUM", "blueButton") {
                            style {
                                control(UIAlignment.BOTTOMCENTER)
                                size(161, 41)
                                y -= px(10f)
                            }
                        }.hover("crumbHover", "crumbLeave")
                    }
                }
            }
        }
    }
}