package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIButton] is the super class for buttons, and acts just like [UILabel]
 *
 * @author sen
 * @since 5/16/2022
 */
open class UIButton<T : UIStyleSheet>(var text: String, style: String) : UIComponent<T>(style) {
    override fun renderComponent() {
        style.font?.render(text)
    }
}