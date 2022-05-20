package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.util.UIDependable
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * An example of using [UIDependable] to load assets
 */
class IconStyles : UIDependable() {
    override fun load() {
        renderer {
            loadImage("background", "/images/background.png")
            loadSvg("svg", "/aether/svg/message-text.svg", 4f)
        }
    }
}