package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIButton] is the default implementation of [UIComponent]. It renders the given text to the font.
 *
 * @author sen
 * @since 1.0
 */
open class UIButton(open var text: String) : UIComponent<UIStyleSheet>() {
    override fun renderComponent() {
        style.font?.render(text)
    }

    override fun createsStyle(): UIStyleSheet = UIStyleSheet()
}