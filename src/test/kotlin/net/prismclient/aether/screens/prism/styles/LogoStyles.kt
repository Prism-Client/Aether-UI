package net.prismclient.aether.screens.prism.styles

import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.util.extensions.style
import net.prismclient.aether.ui.util.interfaces.UIDependable

class LogoStyles : UIDependable {
    override fun load() {
        style(UIImageSheet(), "Logo-Medium") {
            size(56, 62)
        }
    }
}