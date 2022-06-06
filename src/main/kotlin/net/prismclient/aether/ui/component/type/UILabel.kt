package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.colorToString

/**
 * [UILabel] is a component which draws a label, or string on screen.
 *
 * @author sen
 * @since 5/15/2022
 */
class UILabel(var text: String, style: String) : UIComponent<UIStyleSheet>(style) {
    override fun renderComponent() {
        style.font?.render(text)
    }
}