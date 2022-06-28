package net.prismclient.aether.screens

import net.prismclient.aether.ui.screen.UIScreen

/**
 * Here is an example of how to use the Aether UI library.
 */
class ExampleScreen : UIScreen {
    override fun build() {
        //UIFontFamily("Poppins", "/demo/fonts/", "regular", "medium", "black", "bold", "light", "thin")

//        build {
//            dependsOn(::TextStyles)
//            dependsOn(::IconStyles)
//            dependsOn(::ComponentStyles)
//
//            renderer {
//                loadImage("background", "/prism/background.png")
//                loadImage("logo", "/demo/logo.png")
//            }
//
////            style(UIStyleSheet(), "activeCrumb") {
////                background(asRGBA(87, 164, 255, 0.1f))
////                size(rel(1f), px(40f))
////                font {
////                    align(UIAlignment.CENTER)
////                    x = px(72f)
////                    y += descender(0.5f)
////                    fontFamily = "Poppins"
////                    fontType = UIFont.FontType.Regular
////                    textAlignment = UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE
////                    fontSize = 14f
////                    fontColor = asRGBA(87, 164, 255)
////                }
////            }
////
////            style(UIStyleSheet(), "crumb") {
////                size(rel(1f), px(40f))
////
////                font {
////                    align(UIAlignment.CENTER)
////                    x = px(72f)
////                    y += descender(0.5f)
////                    fontFamily = "Poppins"
////                    fontType = UIFont.FontType.Regular
////                    textAlignment = UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE
////                    fontSize = 14f
////                    fontColor = asRGBA(65, 63, 68)
////                }
////            }
////
////            style(UIImageSheet(), "activeCrumbImage") {
////                control(UIAlignment.MIDDLELEFT)
////                size(24f, 24f)
////                margin(marginLeft = 24f)
////                imageColor = asRGBA(87, 164, 255)
////            }
////
////            style(UIImageSheet(), "crumbImage") {
////                control(UIAlignment.MIDDLELEFT)
////                size(24f, 24f)
////                margin(marginLeft = 24f)
////                imageColor = asRGBA(65, 63, 68)
////            }
////
////            style(UIStyleSheet(), "blueButton") {
////                background(asRGBA(87, 164, 255, 0.1f)) {
////                    radius(8f)
////                }
////                font {
////                    align(UIAlignment.CENTER)
////                    y += descender(0.5f)
////                    textAlignment = UIRenderer.ALIGNMIDDLE or UIRenderer.ALIGNCENTER
////                    fontFamily = "Poppins"
////                    fontType = UIFont.FontType.Bold
////                    fontSize = 16f
////                    fontColor = asRGBA(87, 164, 255)
////                }
////            }
//
//            // Sidebar
//            container("container") {
//                style {
//                    align(UIAlignment.TOPLEFT)
//                    size(px(236f), rel(1f))
//                    background(-1)
//                    clipContent = true
//                }
//                image("logo", style = "imag") {
//                    style {
//                        control(UIAlignment.TOPCENTER)
//                        size(44f, 65f)
//                        margin(marginTop = 25f)
//                    }
//                }
//                list(UIListLayout.ListDirection.Vertical, UIListLayout.ListOrientation.Forward, "container") {
//                    style {
//                        y = px(65 + 25 + 32)
//                        size(rel(1f), rel(1f) - px(65 + 25 + 32))
//                        clipContent = true
//                        verticalScrollbar {
//                            color = asRGBA(87, 164, 255)
//                        }
//                    }
//
//                    p("MENU").style {
//                        margin(marginLeft = 24f, marginBottom = 10f)
//                        font {
//                            fontColor = asRGBA(191, 189, 193)
//                        }
//                    }
//
//                    selectable<UIImageButton> {
//                        onSelection {
//                            it.hover("", "")
//                            it.applyStyle("activeCrumb")
//                            it.image.applyStyle("activeCrumbImage")
//                        }
//                        onDeselection {
//                            it.applyStyle("crumb")
//                            it.image.applyStyle("crumbImage")
//                            it.hover("hover", "leave")
//                        }
//
//                        button("Mods", "crumb", "note", "crumbImage")
//                        button("Settings", "crumb", "setting", "crumbImage")
//                        button("Store", "crumb", "bag", "crumbImage")
//                        button("Profiles", "crumb", "profile", "crumbImage")
//
//                        ignore {
//                            p("SOCIAL").style {
//                                margin(20f, 0f, 10f, 24f)
//                                font(fontColor = asRGBA(191, 189, 193))
//                            }
//                        }
//
//                        button("Messages", "crumb", "message", "crumbImage")
//                        button("Friends", "crumb", "friends", "crumbImage")
//                        button("Achievements", "crumb", "trophy", "crumbImage")
//                        button("Recordings", "crumb", "recording", "crumbImage")
//
//                        this.components.forEach { it.onMousePressed { c -> this.selectComponent(c) } }
//                        this.selectComponent(0)
//                    }
//
//                    // Support
//                    container("container") {
//                        style {
//                            control(UIAlignment.TOPCENTER)
//                            size(189f, 172f)
//                            margin(marginTop = 20f, marginBottom = 20f)
//                            background(asRGBA(245, 245, 245)) {
//                                radius(10f)
//                                border {
//                                    borderColor = asRGBA(214, 214, 214)
//                                    borderWidth = 1f
//                                }
//                            }
//                            clipContent = false
//                        }
//                        p("Get 3 months of cosmetics for free").style {
//                            control(UIAlignment.TOPCENTER)
//                            y = px(20f)
//                            font(fontSize = 16f, fontColor = asRGBA(0, 0, 0), textAlignment = UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP) {
//                                align(UIAlignment.TOPCENTER)
//                                fontRenderType = UIFont.FontRenderType.WRAP
//                                lineBreakWidth = px(189)
//                            }
//                        }
//                        p("Support Prism's development by unlocking cosmetics.").style {
//                            control(UIAlignment.TOPCENTER)
//                            y = px(52)
//                            font(fontSize = 14f, fontColor = asRGBA(65, 63, 68), textAlignment = UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP) {
//                                align(UIAlignment.TOPCENTER)
//                                fontRenderType = UIFont.FontRenderType.WRAP
//                                lineBreakWidth = px(179)
//                            }
//                        }
//                        button("GET PREMIUM", "blueButton").style {
//                            control(UIAlignment.BOTTOMCENTER)
//                            size(161, 41)
//                            y -= px(10f)
//                        }.hover("crumbHover", "crumbLeave")
//                    }
//                }
//            }
//
//            val title = h3("SETTINGS").style {
//                position(277f, 30f)
//                font {
//                    align(UIAlignment.MIDDLELEFT)
//                    fontSpacing = fontSize * 0.025f
//                }
//            }
//
//            p("Configure Prism Client's settings to improve and optimize your experience!").style {
//                background(asRGBA(255, 0, 0, 0.3f))
//                size(rel(1f) - px(277 + 448 + 30), px(font!!.fontSize))
//                position(px(277f), dependentUnit value@{
//                    return@value title.y + title.height
//                })
//                font {
//                    overrideParent = false
//                }
//                clipContent = true
//            }

//            style(UITextFieldSheet(), "textField") {
//                background(asRGBA(0, 0, 0, 0.1f), radius(8f)) {
//                    border(asRGBA(214, 214, 216), 1f, UIStrokeDirection.INSIDE)
//                }
//                font {
//                    align(UIAlignment.MIDDLELEFT)
//                    x += px(20)
//                    fontFamily = "Poppins"
//                    fontSize = 14f
//                    isSelectable = true
//                    selectionColor = asRGBA(0, 120, 200, 0.3f)
//                    textAlignment = UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE
//                }
//                clipContent = false
//            }

//            component(UITextField("", "Search for anything...", UITextField.any, "textField")) {
//                style {
//                    control(UIAlignment.TOPRIGHT)
//                    size(448, 39)
//                    x -= px(30); y = px(30)
//                }
//                onTextChanged("textListener") {
//                    //println("This text has been changed to: ${it.text}")
//                }
//            }
//        }
    }
}