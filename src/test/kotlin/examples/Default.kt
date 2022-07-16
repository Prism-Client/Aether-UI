package examples

import examples.deps.Generic
import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.layout.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.UIListLayout
import net.prismclient.aether.ui.component.type.layout.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*

class Default : UIScreen {

    val activeNavbarButtonColor = colorOf(87, 164, 255)
    val hoveredNavbarButtonColor = colorOf(41, 41, 41)

    override fun build() {
        create {
            include(Generic())
            UIAssetDSL.image("NavbarLogo", "/prism/logo/Logo.png", flags = REPEATX or REPEATY or GENERATE_MIPMAPS)
            // TODO: test if lods stuff in root directory
            UIAssetDSL.bulkLoad("/prism/icons", imageFlags = REPEATX or REPEATY or GENERATE_MIPMAPS)
            createNavbar()
        }
    }

    private fun createNavbar() {
        ucreate {
            val navbar = container {

                UIImageSheet().style("navbar-logo") {
                    control(UIAlignment.CENTER)
                    size(56, 62)
                }

                // Logo
                container {
                    image("NavbarLogo", "navbar-logo")
                }.style("logo") {
                    size(rel(1), px(118))
                }

                val navbarButtonStyle = UIContainerSheet().style("navbar-button") {
                    control(UIAlignment.CENTER)
                    size(206, 40)
                    margin { marginBottom = px(8) }
                }

                val activeNavbarButtonStyle = UIContainerSheet().style("active-navbar-button") {
                    control(UIAlignment.CENTER)
                    size(206, 40)
                    background(activeNavbarButtonColor, radiusOf(9f))
                    margin { marginBottom = px(8) }
                }

                val hoveredNavbarButtonStyle = UIContainerSheet().style("hovered-navbar-button") {
                    background(colorOf(255, 0, 0), radiusOf(9f))
                }

                // Navbar list
                val navList = list(UIListLayout.ListDirection.Vertical) {
                    val layout = UIAutoLayout(UIListLayout.ListDirection.Horizontal).apply {
                        componentAlignment = UIAlignment.MIDDLELEFT
                        componentSpacing = px(24)
                        layoutPadding = paddingOf(8, 9, 8, 9)
                    }

                    // Navbar section style
                    UIStyleSheet().style("navbar-title") {
                        x = px(24)
                        font("Montserrat", px(11), colorOf(191, 189, 193), top or left)
                        margin {
                            marginTop = descender(1f)
                            marginBottom = px(8)
                        }
                    }

                    animationOf("navbar-button", UIContainerSheet()) {
//                        keyframe(UIQuart(1000L), navbarButtonStyle)
                        kf {}
                        kf {
                            background {
                                background(colorOf(0))
                            }
                        }
                    }

//                    animationOf("active-navbar-button", UIContainerSheet()) {
//                        kf {}
//                        keyframe(UIQuart(1000L), activeNavbarButtonStyle.copy())
//                    }

                    animationOf("hovered-navbar-button", UIContainerSheet()) {
                        keyframe(UIQuart(1000L), hoveredNavbarButtonStyle.copy())
                    }

                    // Navbar button styles
                    UIImageSheet().style("navbar-icon") {
                        size(24, 24)
                    }

                    UIStyleSheet().style("navbar-text") {
                        font("Montserrat", px(14), colorOf(-1), left or top)
                    }

                    selectable<UIAutoLayout> {
                        label("MENU", "navbar-title")
                        navButton(layout, "Dashboard", "gradient/home")
                        navButton(layout, "Mods", "gradient/folder")
                        navButton(layout, "Settings", "gradient/setting")
                        navButton(layout, "Store", "gradient/bag")
                        navButton(layout, "Profiles", "gradient/profile")

                        label("SOCIAL", "navbar-title")
                        navButton(layout, "Messages", "gradient/mail")
                        navButton(layout, "Friends", "gradient/people")
                        navButton(layout, "Achievements", "gradient/medal")
                        navButton(layout, "Recordings", "gradient/video")

                        onSelection {
                            UIProvider.dispatchAnimation("active-navbar-button", it)
                            //it.applyStyle("active-navbar-button")
                            val image = (it.components[0] as UIImage)
                            image.image = "solid/" + image.image.substring(image.image.indexOf('/') + 1)
                        }

                        onDeselection {
                            UIProvider.dispatchAnimation("navbar-button", it)
                            val image = (it.components[0] as UIImage)
                            image.image = "gradient/" + image.image.substring(image.image.indexOf('/') + 1)
                            it.applyStyle("navbar-button")
                        }

                        this.components.forEach { component ->
                            component.onMousePressed {
                                println("This was selected: ${((it as UIAutoLayout).components[1] as UILabel).text}")
                                //selectComponent(component)
                            }

                            component.onMouseEnter {
                                if (!isSelected(it)) {
                                    println("enter")
                                    UIProvider.dispatchAnimation("hovered-navbar-button", it)
                                    //it.applyStyle("hovered-navbar-button")
                                }
                            }

                            component.onMouseLeave {
                                if (!isSelected(it)) {
                                    println("leave")
                                    UIProvider.dispatchAnimation("navbar-button", it)
                                    //it.applyStyle("navbar-button")
                                }
                            }
                        }

                        // TODO: Style does not get initialized before this call.
                        //selectComponent(0)
                    }

                }.style("navbar-list") {
                    y = px(118)
                    size(rel(1), rel(1) - px(118 + 235)) // 118 = top area, 235 = bottom area
                    verticalScrollbar {
                        x = rel(1) - px(10)
                        y = rel(0.1)
                        width = px(5)
                        height = rel(0.8)
                        radius = radiusOf(2.5f)
                        color = colorOf(207, 207, 207)
                        background {
                            backgroundColor = colorOf(1f, 1f, 1f, 0.3f)
                            radius = radiusOf(2.5)
                        }
                    }
                    clipContent = true
                }

                // Footer
                val footer = autoLayout(UIListLayout.ListDirection.Vertical) {
                    verticalResizing = UIAutoLayout.ResizingMode.Hug
                    horizontalResizing = UIAutoLayout.ResizingMode.Hug
                    componentAlignment = UIAlignment.CENTER
                    componentSpacing = px(6)

                    // Promotion
                    // TODO: Promotion component

                    val editHud = autoLayout(UIListLayout.ListDirection.Horizontal) {
                        image("outline/ui", "navbar-icon").style {
                            imageColor = colorOf(87, 164, 255)
                        }
                        text("Edit HUD", "navbar-text").style {
                            font {
                                fontColor = colorOf(-1) //colorOf(87, 164, 255)
                            }
                        }

                        componentAlignment = UIAlignment.CENTER
                        componentSpacing = px(20)
                        layoutPadding = paddingOf(15, 37, 15, 37)

                        verticalResizing = UIAutoLayout.ResizingMode.Hug
                        horizontalResizing = UIAutoLayout.ResizingMode.Hug
                    }.style("edit-hud-layout") {
                        control(UIAlignment.BOTTOMCENTER)
                        background(colorOf(36, 37, 37), radiusOf(15))
                    }

                }.style("navbar-footer-layout") {
                    control(UIAlignment.BOTTOMCENTER)
                    y -= px(16 + 8)
                }

                label("Running Prism Client v1.0.0-Beta").style("nav-prism-version") {
                    font("Montserrat", px(8), colorOf(1f, 1f, 1f, 0.8f), left or top, UIFont.FontType.Light)
                    control(UIAlignment.BOTTOMCENTER)
                    y -= px(8)
                }
            }.style("navbar-container") {
                size(px(236), rel(1))
                background(colorOf(32, 32, 32))
                useFBO = true
            }

            // Navbar animation
            animationOf("navbar-enter", UIContainerSheet()) {
                kf {
                    x = px(-236)
                }
                UIQuart(1000L) to {
                    x = px(0)
                }
            }

            animationOf("navbar-exit", UIContainerSheet()) {
                kf {
                    x = px(0)
                }
                UIQuart(500L) to {
                    x = px(-236)
                }
            }

            UIProvider.dispatchAnimation("navbar-enter", navbar)
        }
    }

    /**
     * Creates a navbar button
     */
    private fun navButton(layout: UIAutoLayout, buttonText: String, imageName: String): UIAutoLayout {
        ucreate {
            return autoLayout(layout) {
                image(imageName, "navbar-icon")
                text(buttonText, "navbar-text")
            }.style {
                control(UIAlignment.CENTER)
                size(206, 40)
                margin { marginBottom = px(8) }
            }
        }
        return throw RuntimeException()
    }
}