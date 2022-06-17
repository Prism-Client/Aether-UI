package net.prismclient.aether.ui.component.type.input.textfield

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.util.input.UIModifierKey
import net.prismclient.aether.ui.util.interfaces.UIFocusable

/**
 * [UITextField] is a focusable component which accepts an input from
 * the user via the keyboard. [placeholder] is the text which will be
 * rendered when the field is empty. If null, the field will not render.
 * [filter] defines the type of characters which will be accepted, and the
 * length of the overall text.
 *
 * @author sen
 * @since 5/6/2022
 * @see UITextField.filter Pre-made text filters.
 */
open class UITextField(
        text: String,
        var placeholder: String? = null,
        var filter: TextFilter,
        style: String
) : UIButton<UITextFieldSheet>(text, style), UIFocusable<UITextField> {
    /**
     * If true, the placeholder text will be rendered if this is defocused
     */
    var resetOnDefocus: Boolean = false

    init {
        UICore.onModifierKeyChange(this.toString()) { key, value ->
            if (!value) return@onModifierKeyChange
            when (key) {
                UIModifierKey.BACKSPACE -> {
                    if (text.isNotEmpty() && getCaretPosition() > 0) {
                        this.text = this.text.substring(0, getCaretPosition() - 1) + this.text.substring(getCaretPosition(), this.text.length)
                        moveCaret(getCaretPosition() - 1)
                    }
                }
                UIModifierKey.ARROW_LEFT -> moveCaret((getCaretPosition() - 1).coerceAtLeast(0))
                UIModifierKey.ARROW_RIGHT -> moveCaret((getCaretPosition() + 1).coerceAtMost(text.length))
                else -> {}
            }
        }
    }

    override fun updateStyle() {
        super.updateStyle()

        // Allocate (if needed), and force the font to be selectable
        style.font = style.font ?: UIFont()
        style.font!!.isSelectable = true

        // Update the caret
        style.caret.update(this)
    }

    override fun renderComponent() {
        if (style.font != null) {
            val temp = style.font!!.fontColor
            if (text.isEmpty())
                style.font!!.fontColor = style.placeholderColor
            style.font!!.render(if (text.isEmpty() && !isFocused()) placeholder ?: "" else text)
            style.font!!.fontColor = temp
            style.caret.render()
        }
    }

    override fun keyPressed(character: Char) {
        if (text.length > filter.maxLength && filter.maxLength != -1) return
        if (filter.accept(character)) {
            if (isSelected())
                clear()
            text = text.substring(0, getCaretPosition()) + character + text.substring(getCaretPosition(), text.length)
            style.font!!.updateCaretPosition(getCaretPosition() + 1)
        }
        super.keyPressed(character)
    }

    /**
     * Moves the caret/cursor to the given position
     */
    open fun moveCaret(index: Int) = style.font!!.updateCaretPosition(index)

    /**
     * Selects the text with the [startingIndex] as the caret position and the [endingIndex] as the place to select
     */
    open fun select(startingIndex: Int, endingIndex: Int) {
        style.font!!.select(startingIndex, endingIndex)
    }

    /**
     * Deselects the text if available
     */
    open fun deselect() {
        style.font!!.deselect()
    }

    /**
     * Resets the selection and clears the text
     */
    open fun clear() {
        deselect()
        text = ""
    }

    /**
     * Returns true if the font is selected
     */
    open fun isSelected() = style.font!!.selected

    /**
     * Returns the position of the caret/cursor
     */
    open fun getCaretPosition() = style.font!!.position!!.caretPosition

    /**
     * Returns the ending position of the selection
     *
     * @see getCaretPosition
     */
    open fun getSelectionPosition() = style.font!!.position!!.selectionPosition

    /**
     * [TextFilter] holds a string which is compared to the input character. If the character
     * is found within the string, it will be added to the text field, else it will not. Furthermore,
     * [maxLength] determines the max length of the input text.
     *
     * @author sen
     * @since 4/28/2022
     * @see UITextField.Filters Pre-made filters
     *
     */
    class TextFilter(val pattern: String, val maxLength: Int = -1) {
        fun accept(char: Char) = pattern.contains(char)
    }

    companion object Filters {
        @JvmStatic
        val any =
                TextFilter("ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^&*()_+-=|,./<>?;':{}[]\"\\ ")

        @JvmStatic
        val alphabet = TextFilter("ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")

        @JvmStatic
        val number = TextFilter("1234567890")

        @JvmStatic
        val hex = TextFilter("#ABCDEFabcdef0123456789")
    }
}