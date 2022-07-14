package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UILabel] is a component which draws a label, or string on screen.
 *
 * @author sen
 * @since 1.0
 */
class UILabel(var text: String) : UIComponent<UIStyleSheet>() {
    override fun renderComponent() {
        style.font?.render(text)
    }

    override fun createsStyle(): UIStyleSheet = UIStyleSheet()
}