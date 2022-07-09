package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.util.blockFrom
import net.prismclient.aether.ui.util.interfaces.UIDependable
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * An example of using [UIDependable] to load assets
 */
class IconStyles : UIDependable {
    override fun load() {
        blockFrom(UIAssetDSL) {
            // Image
            image("background", "/prism/background.png")

            // Icons
            image("bag", "/demo/icons/bag.svg")
            image("book", "/demo/icons/book.svg")
            image("note", "/demo/icons/note.svg")
            image("profile", "/demo/icons/profile.svg")
            image("setting", "/demo/icons/setting.svg")
            image("message", "/demo/icons/message.svg")
            image("friends", "/demo/icons/friends.svg")
            image("trophy", "/demo/icons/trophy.svg")
            image("recording", "/demo/icons/recording.svg")
        }
    }
}