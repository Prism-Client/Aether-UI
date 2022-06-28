package net.prismclient.aether.screens.prism

import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNCENTER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNMIDDLE
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNRIGHT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNTOP
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.radiusOf

class PrismMainMenu : UIScreen {
    override fun build() {
//        build {
//            renderer {
////                loadImage("Logo-Tiny", "/prism/icons/Logo-Big.png")
////                loadSvg("bag", "/prism/icons/bag.svg")
////                loadSvg("setting", "/prism/icons/setting.svg")
////                loadSvg("world", "/prism/icons/world.svg")
//            }
//
//            style("button") {
//                control(UIAlignment.CENTER)
//                size(600, 55)
//                background(asRGBA(0, 0, 0, 0.1f), radiusOf(15f))
//                font("Poppins", 18f, -1, ALIGNMIDDLE or ALIGNCENTER) {
//                    align(UIAlignment.CENTER)
//                }
//            }
////            style(UIContainerSheet(), "titleContainer") {
////                control(UIAlignment.CENTER)
////                size(300, 48)
////                y -= px(118)
////                clipContent = false
////            }
////
////            style(UIImageSheet(), "logo") {
////                control(UIAlignment.MIDDLELEFT)
////                size(55, 61)
////            }
//
//            style("title") {
//                control(UIAlignment.TOPRIGHT)
//                font("Poppins", 50f, -1, ALIGNTOP or ALIGNRIGHT, UIFont.FontType.Light) {
//                    fontSpacing = fontSize * 0.385f
//                }
//                clipContent = false
//            }
//
////            style(UIImageSheet(), "bottom-icon") {
////                control(UIAlignment.BOTTOMCENTER)
////                size(24f, 24f)
////                y -= px(24)
////            }
//
//            image("menu-bg", "/prism/backgrounds/main-menu.png", "background")
//
//            container("titleContainer") {
//                image("Logo-Big", style = "logo")
//                label("PRISM", "title")
//            }
//
//            button("Singleplayer", "button")
//            button("Multiplayer", "button").style {
//                y += px(80)
//            }
//
//            image("bag", style = "bottom-icon").style {
//                x -= px(48)
//            }
//            image("setting", style = "bottom-icon")
//            image("world", style = "bottom-icon").style {
//                x += px(48)
//            }
//        }
    }
}