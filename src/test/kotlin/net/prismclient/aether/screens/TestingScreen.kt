package net.prismclient.aether.screens

import examples.deps.Generic
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*

class TestingScreen : UIScreen {
    override fun build() {
        create {
            include(Generic())

            container {
                button("Hi").style(UIStyleSheet("")) {
                    background(colorOf(asRGBA(255, 0, 0, 0.3f)))
                }
                label("Hello world!").style(UIStyleSheet("")) {
                    position(0, 0)
                    font("Montserrat", px(24f), colorOf(-1), left or top)
                    size(100, 100)
                }
            }.style(UIContainerSheet("")) {
                control(UIAlignment.CENTER)
                // align anchor
                size(300, 300)
                background(colorOf(asRGBA(0f, 0f, 0f, 0.3f)), radiusOf(9f))
                useFBO = true
            }
        }
    }
}