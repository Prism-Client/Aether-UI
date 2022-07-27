package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel

/**
 * [UIButton] is the default implementation of [UIComponent]. It renders the given text to the font.
 *
 * @author sen
 * @since 1.0
 */
open class UIButton(text: String) : UILabel(text)