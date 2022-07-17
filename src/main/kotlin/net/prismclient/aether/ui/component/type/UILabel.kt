package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UILabel] is a component which draws a label, or string on screen.
 *
 * @author sen
 * @since 1.0
 */
class UILabel(text: String) : UIComponent<UIStyleSheet>() {
    var text: String = text
        set(value) {
            field = value
            style.font?.actualText = text
        }

    override fun renderComponent() {
        style.font?.render()
    }

    override fun createsStyle(): UIStyleSheet = UIStyleSheet()
}