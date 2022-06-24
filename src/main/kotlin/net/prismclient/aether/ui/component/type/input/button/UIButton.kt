package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.colorToString

/**
 * [UIButton] is a simple class, which is used to create a button.
 *
 * @author sen
 * @since 5/16/2022
 * @param T The stylesheet (used for inheritance) leave as UIStyleSheet.
 */
open class UIButton<T : UIStyleSheet>(open var text: String, style: String) : UIComponent<T>(style) {
    override fun renderComponent() {
        style.font?.render(text)
    }
}