package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.util.interfaces.UIDependable
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * An example of using [UIDependable] to load assets
 */
class IconStyles : UIDependable() {
    override fun load() {
        renderer {
            loadSvg("bag", "/demo/icons/bag.svg")
            loadSvg("book", "/demo/icons/book.svg")
            loadSvg("note", "/demo/icons/note.svg")
            loadSvg("profile", "/demo/icons/profile.svg")
            loadSvg("settings", "/demo/icons/settings.svg")
            loadImage("background", "/demo/background.png")
        }
    }
}