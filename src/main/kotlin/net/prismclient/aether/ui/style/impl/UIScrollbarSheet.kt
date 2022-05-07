package net.prismclient.aether.ui.style.impl

import net.prismclient.aether.ui.style.UIStyleSheet

class UIScrollbarSheet : UIStyleSheet() {
    //     var scrollbarColor = 0
    //    var scrollbarRadius = 0f
    //    var scrollbarOutlineColor = 0
    //    var scrollbarOutlineWidth = 0f
    override fun copy(): UIStyleSheet {
        val it = UIScrollbarSheet()

        it.apply(this)

        // TODO

        return it
    }
}