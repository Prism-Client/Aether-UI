package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.util.interfaces.UIDependable
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * An example of using [UIDependable] to load assets
 */
class IconStyles : UIDependable {
    override fun load() {
        renderer {
            // Image
            loadImage("background", "/prism/background.png")

            // Icons
            loadSvg("bag", "/demo/icons/bag.svg")
            loadSvg("book", "/demo/icons/book.svg")
            loadSvg("note", "/demo/icons/note.svg")
            loadSvg("profile", "/demo/icons/profile.svg")
            loadSvg("setting", "/demo/icons/setting.svg")
            loadSvg("message", "/demo/icons/message.svg")
            loadSvg("friends", "/demo/icons/friends.svg")
            loadSvg("trophy", "/demo/icons/trophy.svg")
            loadSvg("recording", "/demo/icons/recording.svg")
        }
    }
}