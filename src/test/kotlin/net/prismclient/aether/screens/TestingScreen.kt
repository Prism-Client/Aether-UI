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

            val navbarLayout: UIAutoLayout =
                UIAutoLayout(UIListLayout.ListDirection.Horizontal, null).style(UIAutoLayoutSheet("navlayout")) {
                    control(UIAlignment.CENTER)
                    size(206, 40)
                    background(colorOf(asRGBA(87, 164, 255)), radiusOf(9f))
                    margin { marginBottom = px(8) }
                }.apply {
                    componentAlignment = UIAlignment.MIDDLELEFT
                    componentSpacing = px(24)
                    layoutPadding = paddingOf(8, 9, 8, 9)
                }

            val navbar: UIContainer<UIContainerSheet> = component(UIContainer<UIContainerSheet>(null)) {
                list(UIListLayout.ListDirection.Vertical, UIListLayout.ListOrder.Forward) {
                    label("MODS", "navbar-title")
                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Dashboard", "generic-font")
                    }

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Mods", "generic-font")
                    }

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Settings", "generic-font")
                    }

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Store", "generic-font")
                    }

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Profiles", "generic-font")
                    }

                    label("SOCIAL", "navbar-title")

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Messages", "generic-font")
                    }

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Achievements", "generic-font")
                    }

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Friends", "generic-font")
                    }

                    autoLayout(navbarLayout) {
                        image("ui", "icon24x")
                        text("Recordings", "generic-font")
                    }
                }.style(UIContainerSheet("")) {
                    y = px(118)
                    size(rel(1), rel(0.8))
                }

            }.style(UIContainerSheet("navbar")) {
                size(px(236), rel(1))
                background(colorOf(36, 37, 37))
            }
        }
    }
}