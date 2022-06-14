package net.prismclient.aether.ui.component.type.input.textfield

import net.prismclient.aether.ui.component.type.input.textfield.caret.UICaret
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.asRGBA

/**
 * The corresponding style sheet for text fields. It contains basic styling information
 * such as the placeholder color. It also contains caret controls.
 *
 * @author sen
 * @since 5/11/2022
 */
class UITextFieldSheet : UIStyleSheet() {
    /**
     * The caret shape which is drawn to display the caret.
     */
    var caret: UICaret = UICaret()

    /**
     * The rate at which the caret blinks at. 0 = never
     */
    var blinkRate: Long = 500L

    /**
     * The color of the text when the text field is not focused
     */
    var placeholderColor = asRGBA(200, 200, 200)

    override fun copy(): UITextFieldSheet = UITextFieldSheet().also {
        it.apply(this)
        it.caret = caret.copy()
        it.blinkRate = blinkRate
        it.placeholderColor = placeholderColor
    }
}