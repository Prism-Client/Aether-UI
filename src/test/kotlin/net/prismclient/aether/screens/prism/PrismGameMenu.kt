package net.prismclient.aether.screens.prism

import net.prismclient.aether.screens.prism.styles.LogoStyles
import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.input.button.UIImageButton
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.radiusOf

class PrismGameMenu : UIScreen {
    companion object {
        val primary_color = asRGBA(87, 164, 255)
        val background_color = asRGBA(32, 32, 32)
        val background_color_secondary = asRGBA(36, 37, 37)
    }

    override fun build() {
//        build {
//            UIFontFamily(
//                "Montserrat",
//                "/prism/fonts/montserrat/",
//                "Montserrat-regular",
//                "Montserrat-medium",
//                "Montserrat-black",
//                "Montserrat-bold",
//                "Montserrat-light",
//                "Montserrat-thin"
//            )
//            renderer {
//                loadSvg("home", "/prism/icons/navbar/home.svg")
//                loadSvg("home-active", "/prism/icons/navbar/home-active.svg")
//                loadSvg("setting", "/prism/icons/navbar/setting.svg")
//                loadSvg("setting-active", "/prism/icons/navbar/setting-active.svg")
//                loadSvg("bag", "/prism/icons/navbar/bag.svg")
//                loadSvg("bag-active", "/prism/icons/navbar/bag-active.svg")
//                loadSvg("profile", "/prism/icons/navbar/profile.svg")
//                loadSvg("profile-active", "/prism/icons/navbar/profile-active.svg")
//                loadSvg("sms", "/prism/icons/navbar/sms.svg")
//                loadSvg("sms-active", "/prism/icons/navbar/sms-active.svg")
//
//                loadSvg("ui", "/prism/icons/navbar/ui.svg")
//            }
//            include(::LogoStyles)
//
////            style(UIContainerSheet(), "navbar") {
////                size(px(236), rel(1))
////                background(background_color)
////            }
////
////            style(UIContainerSheet(), "navbar-list") {
////                // For the height take the height of the navbar and
////                // subtract the height of the top part (logo), and subtract
////                // the part of edit hud and the promotion and a 10px padding
////                size(rel(1f), rel(1f) - px(118 + 150 + 85 + 10))
////                background(asRGBA(1f, 0f, 0f, 0.1f))
////                y = px(118)
////            }
////
////            style("nav-button") {
////                control(UIAlignment.CENTER)
////                size(206, 40)
////                margin(marginTop = 5f)
////                font("Montserrat", 14f, -1, UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE, UIFont.FontType.Regular) {
////                    align(UIAlignment.MIDDLELEFT)
////                    x = px(54)
////                    fontColor = -1
////                }
////            }
////
////            style(UIImageSheet(), "nav-button-icon") {
////                control(UIAlignment.MIDDLELEFT)
////                size(24, 24)
////                x = px(8)
////            }
//
//            style("navbar-title") {
//                x = px(24f)
//                font(
//                    "Montserrat",
//                    11f,
//                    asRGBA(85, 85, 85),
//                    UIRenderer.ALIGNTOP or UIRenderer.ALIGNLEFT,
//                    UIFont.FontType.Medium
//                ) {
//                    align(UIAlignment.TOPLEFT)
//                    fontSpacing = 11f * 0.025f
//                }
//                margin(marginTop = 10f, marginBottom = 5f)
//
//                clipContent = false
//            }
//
//            style("icon-button") {
//                size(189, 56)
//                background(background_color_secondary, radiusOf(15f))
//                font("Montserrat", 14f, primary_color, UIRenderer.ALIGNLEFT or UIRenderer.ALIGNMIDDLE, UIFont.FontType.Medium) {
//                    align(UIAlignment.CENTER)
//                    x -= px(9)
//                }
//            }
//
//            style("button") {
//                size(161, 41)
//                background(primary_color, radiusOf(8f))
//                font("Montserrat", 14f, -1, UIRenderer.ALIGNCENTER or UIRenderer.ALIGNMIDDLE, UIFont.FontType.Bold) {
//                    align(UIAlignment.CENTER)
//                    y += descender(0.5f)
//                }
//            }
//
////            style(UIImageSheet(), "button-icon") {
////                size(24f, 24f)
////                control(UIAlignment.MIDDLELEFT)
////                x = px(41)
////                imageColor = primary_color
////            }
////
////            style(UIContainerSheet(), "Promotion") {
////                size(189, 150)
////                background(background_color_secondary, radius(10f))
////                control(UIAlignment.BOTTOMCENTER)
////                y -= px(85f)
////            }
//
//            style("Promotion-Title") {
//                y = px(13)
//                size(189 - 10, 0f)
//                font("Montserrat", 14f, -1, UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP, UIFont.FontType.Bold) {
//                    align(UIAlignment.TOPCENTER)
//                    fontRenderType = UIFont.FontRenderType.WRAP
//                    lineBreakWidth = px(189 - 10)
//                    clipContent = false
//                }
//            }
//
//            style("Promotion-Description") {
//                size(189 - 10, 0f)
//                font("Montserrat", 10f, -1, UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP, UIFont.FontType.Medium) {
//                    align(UIAlignment.TOPCENTER)
//                    fontRenderType = UIFont.FontRenderType.WRAP
//                    lineBreakWidth = px(189 - 10)
//                    clipContent = false
//                }
//            }
//
//            container("navbar") {
//                style { clipContent = false }
//                image("Logo-Big", style = "Logo-Medium").style {
//                    control(UIAlignment.TOPCENTER)
//                    y = px(30)
//                }
//                list(UIListLayout.ListDirection.Vertical, UIListLayout.ListOrientation.Forward, "navbar-list") {
//                    label("MENU", "navbar-title")
//                    selectable<UIImageButton> {
//                        button("Mods", "nav-button", "home", "nav-button-icon")
//                        button("Settings", "nav-button", "setting", "nav-button-icon")
//                        button("Store", "nav-button", "bag", "nav-button-icon")
//                        button("Profiles", "nav-button", "profile", "nav-button-icon")
//
//                        ignore { label("SOCIAL", "navbar-title") }
//
//                        button("Messages", "nav-button", "sms", "nav-button-icon")
//
//                        onSelection {
//                            it.completeAnimation()
//                            it.hover("", "")
//                            it.image.image = it.image.image.replace("-active", "") + "-active"
//                        }
//                        onDeselection {
//                            it.completeAnimation()
//                            it.hover("hover", "leave")
//                            it.image.image = it.image.image.replace("-active", "")
//                            if (it.style.background?.backgroundColor == primary_color) {}
//                        }
//                        this.components.forEach { it.onMousePressed { c -> this.selectComponent(c) } }
//                        this.selectComponent(0)
//                    }
//                }
//
//                container("Promotion") {
//                    val title = text("Get 3 months of cosmetics for free.", "Promotion-Title")
//                    text("Support Prism's development by unlocking cosmetics.", "Promotion-Description").style {
//                        y = dependentUnit v@{
//                            return@v title.relY + title.relHeight + 6f // 6px margin
//                        }
//                    }
//                    button("GET PREMIUM", "button").style {
//                        control(UIAlignment.BOTTOMCENTER)
//                        y -= px(15)
//                    }
//                }
//
//                button("Edit HUD", "icon-button", "ui", "button-icon").style {
//                    control(UIAlignment.BOTTOMCENTER)
//                    y -= px(14f)
//                }
//            }
//        }
    }
}