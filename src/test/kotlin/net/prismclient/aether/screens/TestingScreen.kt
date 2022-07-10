package net.prismclient.aether.screens

import examples.deps.Generic
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayoutSheet
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*

class TestingScreen : UIScreen {
    var navbarButtonLayout: UIAutoLayout? = null

    override fun build() {
        create {
            include(Generic())

            style(UIStyleSheet("navbar-title")) {
                x = px(24)
                font("Montserrat", px(11), colorOf(191, 189, 193), top or left)
                margin {
                    marginTop = descender(1f)
                    marginBottom = px(8)
                }
            }

            styleOf(UIAutoLayoutSheet("active-navbar-button")) {
                control(UIAlignment.CENTER)
                size(206, 40)
                background(colorOf(asRGBA(87, 164, 255)), radiusOf(9f))
                margin { marginBottom = px(8) }
            }

            styleOf(UIAutoLayoutSheet("navbar-button")) {
                control(UIAlignment.CENTER)
                size(206, 40)
                background(colorOf(0), radiusOf(9f))
                margin { marginBottom = px(8) }
            }

            navbarButtonLayout = UIAutoLayout(UIListLayout.ListDirection.Horizontal, "navbar-button").apply {
                componentAlignment = UIAlignment.MIDDLELEFT
                componentSpacing = px(24)
                layoutPadding = paddingOf(8, 9, 8, 9)
            }

            val navbar: UIContainer<UIContainerSheet> = component(UIContainer<UIContainerSheet>(null)) {
                list(UIListLayout.ListDirection.Vertical, UIListLayout.ListOrder.Forward) {
                    selectable<UIAutoLayout> {
                        ignore { label("MENU", "navbar-title") }
                        navButton("Dashboard", "ui")
                        navButton("Settings", "ui")
                        navButton("Store", "ui")
                        navButton("Profiles", "ui")

                        ignore { label("SOCIAL", "navbar-title") }
                        navButton("Messages", "ui")
                        navButton("Achievements", "ui")
                        navButton("Friends", "ui")
                        navButton("Recordings", "ui")

                        onSelection {
                            it.applyStyle("active-navbar-button")
                        }

                        onDeselection {
                            it.applyStyle("navbar-button")
                        }
                        this.components.forEach { component ->
                            component.onMousePressed {
                                selectComponent(it as UIAutoLayout)
                            }
                        }
                        this.selectComponent(0)
                    }
                }.style(UIContainerSheet("")) {
                    y = px(118)
                    size(rel(1), rel(0.8))
                }
                navbarButtonLayout = null
            }.style(UIContainerSheet("navbar")) {
                size(px(236), rel(1))
                background(colorOf(36, 37, 37))
            }
        }
    }

    fun navButton(buttonText: String, imageName: String) {
        ucreate {
            autoLayout(navbarButtonLayout!!) {
                ignore {
                    image(imageName, "icon24x")
                    text(buttonText, "generic-font")
                }
            }
        }
    }
}