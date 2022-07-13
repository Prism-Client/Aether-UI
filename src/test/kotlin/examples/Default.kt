package examples

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.colorOf
import net.prismclient.aether.ui.util.extensions.minus
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.rel

class Default : UIScreen {
    override fun build() {
        create {
            UIAssetDSL.bulkLoad("/prism/icons", imageFlags = REPEATX or REPEATY or GENERATE_MIPMAPS)
            createNavbar()
        }
    }

    private fun createNavbar() {
        ucreate {
            val navbar = container {
                // Logo
                container {
                    image("logo/Logo").style(UIImageSheet("navbar-logo")) {
                        control(UIAlignment.CENTER)
                        size(56, 62)
                    }
                }.style(UIContainerSheet("logo")) {
                    size(rel(1), px(118))
                }

                onMousePressed {

                    UIProvider.dispatchAnimation("navbar-enter", this)
                }

                // Navbar list
                list(UIListLayout.ListDirection.Vertical) {
                    val layout = UIAutoLayout(UIListLayout.ListDirection.Horizontal, null).apply {
                        componentAlignment = UIAlignment.MIDDLELEFT
                        componentSpacing = px(24)
                        layoutPadding = paddingOf(8, 9, 8, 9)
                    }.style(UIContainerSheet("navbar-button")) {
                        control(UIAlignment.CENTER)
                        size(206, 40)
                        background(colorOf(0f, 1f, 0f, 0.3f), radiusOf(9f))
                        margin { marginBottom = px(8) }
                    }

                    // Navbar button styles
                    styleOf(UIImageSheet("navbar-icon")) {
                        size(24, 24)
                    }

                    styleOf(UIStyleSheet("navbar-text")) {
                        font("Montserrat", px(14), colorOf(-1), left or top)
                    }

                    button("aaaa").style(UIStyleSheet()) {
                        size(200, 100)
                        background = gradient {
                            gradientWidth = rel(1)
                            gradientHeight = rel(1)
                            gradientStartColor = colorOf(1f, 0f, 0f, 1f)
                            gradientEndColor = colorOf(0, 255, 0)
                        }
                    }

//                    navButton(layout, "Text", "gradient/bag")
//                    navButton(layout, "Text", "gradient/bag")
//                    navButton(layout, "Text", "gradient/bag")
//                    navButton(layout, "Text", "gradient/bag")
//                    navButton(layout, "Text", "gradient/bag")
//                    navButton(layout, "Text", "gradient/bag")


                }.style(UIContainerSheet("navbar-list")) {
                    y = px(118)
                    size(rel(1), rel(1) - px(118 + 235)) // 118 = top area, 235 = bottom area
                    background(colorOf(0f, 0f, 1f, 0.3f))
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
                    useFBO = true
                }

                // Footer

            }.style(UIContainerSheet("navbar-container")) {
                size(px(236), rel(1))
                background(colorOf(36, 36, 37))
//                useFBO = true
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

//            UIProvider.dispatchAnimation("navbar-enter", navbar)
        }
    }

    /**
     * Creates a navbar button
     */
    private fun navButton(layout: UIAutoLayout, buttonText: String, imageName: String) {
        ucreate {
            autoLayout(layout) {
                ignore {
                    image(imageName, "navbar-icon")
                    text(buttonText, "navbar-text")
                }
            }
        }
    }
}