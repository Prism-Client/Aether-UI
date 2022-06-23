package net.prismclient.aether.screens

import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.extensions.build
import net.prismclient.aether.ui.util.extensions.rel

class PrismMainMenu : UIScreen {
    override fun build() {
        build {
            style(UIImageSheet(), "background") {
                size(rel(1f), rel(1f))
            }

            image("menu-bg", "/prism/mainmenu/background.png", "background")
        }
    }
}