package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIButton] is the default implementation of [UIComponent]. It renders the given text to the font.
 *
 * @author sen
 * @since 1.0
 */
open class UIButton(text: String) : UIComponent<UIStyleSheet>() {
    open var text: String = text
        set(value) {
            field = value
            style.font?.actualText = text
        }

    override fun update() {
        super.update()
        style.font?.actualText = text
    }

    override fun renderComponent() {
        style.font?.render()
    }

    override fun createsStyle(): UIStyleSheet = UIStyleSheet()
}