package net.prismclient.aether.ui.component.type.input.textfield

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNCENTER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNRIGHT
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL.boundsOf
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.util.input.UIModifierKey
import net.prismclient.aether.ui.util.interfaces.UIFocusable
import net.prismclient.aether.ui.util.warn
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.function.Consumer

/**
 * [UITextField] is a focusable component which accepts an input from
 * the user via the keyboard. [placeholder] is the text which will be
 * rendered when the field is empty. If null, the field will not render.
 * [filter] defines the type of characters which will be accepted, and the
 * length of the overall text.
 *
 * @author sen
 * @since 6/6/2022
 * @see UITextField.filter Pre-made text filters.
 */
open class UITextField(text: String, var placeholder: String? = null, var filter: TextFilter, style: String) : UIButton<UITextFieldSheet>(text, style), UIFocusable<UITextField> {
    override var text: String = text
        set(value) {
            field = value
            textChangedListener?.forEach { it.value.accept(this) }
        }

    /**
     * If true, the placeholder text will be rendered if this is defocused
     */
    var resetOnDefocus: Boolean = false

    /**
     * Invoked when the text within this text field has changed.
     */
    var textChangedListener: HashMap<String, Consumer<UITextField>>? = null
        protected set

    protected var isCtrlHeld = false

    /** Blink **/
    protected var timeSinceLastBlink: Long = 0L
    protected var blink: Boolean = false

    init {
        UICore.onModifierKeyChange(this.toString()) { key, value ->
            if (!this.isFocused()) return@onModifierKeyChange
            if (key == UIModifierKey.LEFT_CTRL || key == UIModifierKey.RIGHT_CTRL) isCtrlHeld = !value
            if (value) return@onModifierKeyChange
            when (key) {
                UIModifierKey.BACKSPACE -> {
                    if (this.text.isNotEmpty()) {
                        if (this.style.font!!.hasSelection()) {
                            clearSelection()
//                            moveCaret(getCaretPosition() - clearSelection())
                        } else if (getCaretPosition() > 0) {
                            this.text = this.text.substring(0, getCaretPosition() - 1) + this.text.substring(getCaretPosition(), this.text.length)
                            moveCaret(getCaretPosition() - 1)
                        }
                    }
                }
                // Arrow keys are already handled by UIFont
                else -> {}
            }
        }
    }

    override fun deallocate() {
        // Deallocate the UICore.onModifierKeyChange listener
        UICore.modifierKeyListeners.remove(this.toString())
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
        val font = style.font!!
        val color = font.fontColor

        font.render(text.ifEmpty {
            font.fontColor = style.placeholderColor
            placeholder ?: ""
        }); font.fontColor = color

        val bounds = boundsOf(text.substring(0, getCaretPosition()))

        style.caret.offsetX = font.cachedX - x + when {
            (font.textAlignment and ALIGNLEFT) != 0 -> bounds[4]
            (font.textAlignment and ALIGNCENTER) != 0 -> bounds[4]
            (font.textAlignment and ALIGNRIGHT) != 0 -> -bounds[4]
            else -> 0f
        }
        style.caret.offsetY = font.cachedY + boundsOf(style.font!!.cachedText)[1] - y

        if (blink && isFocused()) style.caret.render()
        if (timeSinceLastBlink + style.blinkRate <= System.currentTimeMillis()) {
            blink = !blink
            timeSinceLastBlink = System.currentTimeMillis()
        }
    }

    override fun mousePressed(event: UIMouseEvent) {
        super.mousePressed(event)
        if (text.isEmpty()) {
            moveCaret(0)
        }
    }

    override fun keyPressed(character: Char) {
        if (isCtrlHeld) {
            when (character.lowercase()[0]) {
                /* Select all */ 'a' -> select(0, text.length)
                /* Copy */ 'c' -> { warn("Copy not implemented") }
                /* Paste */ 'v' -> { warn("Paste not implemented") }
                /* Cut */ 'x' -> { warn("Cut not implemented") }
                /* Undo */ 'z' -> { warn("Undo not implemented") }
                /* Redo */ 'y' -> { warn("Redo not implemented") }
            }
            return
        }
        if (text.length > filter.maxLength && filter.maxLength != -1) return
        if (filter.accept(character)) {
            if (isSelected()) clearSelection()
            text = text.substring(0, getCaretPosition()) + character + text.substring(getCaretPosition(), text.length)
            style.font!!.updateCaretPosition(getCaretPosition() + 1)
        }
        super.keyPressed(character)
    }

    override fun defocus() {
        super.defocus()
        if (resetOnDefocus) {
            text = ""
            style.font!!.updateCaretPosition(0)
        }
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
        style.font!!.updateCaretPosition(0)
        text = ""
    }

    /**
     * Clears the active selection (if applicable).
     *
     * @return The length of the cleared string
     */
    open fun clearSelection() {
        text = text.substring(0, min(getCaretPosition(), getSelectionPosition())) + text.substring(max(getSelectionPosition(), getCaretPosition()), text.length)
        moveCaret(min(getCaretPosition(), getSelectionPosition()))
    }

    /**
     * Returns true if the font is selected
     */
    open fun isSelected() = style.font!!.hasSelection()

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
     * Adds a listener for when the text is changed
     */
    @JvmOverloads
    open fun onTextChanged(eventName: String = "${textChangedListener?.size ?: 0f}", event: Consumer<UITextField>) {
        textChangedListener = textChangedListener ?: hashMapOf()
        textChangedListener!![eventName] = event
    }

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
        val any = TextFilter("ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^&*()_+-=|,./<>?;':{}[]\"\\ ")

        @JvmStatic
        val alphabet = TextFilter("ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")

        @JvmStatic
        val number = TextFilter("1234567890")

        @JvmStatic
        val hex = TextFilter("#ABCDEFabcdef0123456789")
    }
}