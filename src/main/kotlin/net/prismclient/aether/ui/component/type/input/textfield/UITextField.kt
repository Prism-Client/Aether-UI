package net.prismclient.aether.ui.component.type.input.textfield

import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.util.input.UIKey
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

    /* Properties */
    var caretPosition = 0
    var caretEndPosition = 0
    var selected = false

    override fun updateStyle() {
        super.updateStyle()
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

    override fun mousePressed(event: UIMouseEvent) {
        if (isMouseInsideBounds()) {
            // Update the caret position

        }
        super.mousePressed(event)
    }

    override fun keyPressed(character: Char, key: UIKey) {
        if (text.length > filter.maxLength && filter.maxLength != -1) return
        if (character != '\u0000') {
            println(character)
            if (filter.accept(character)) {
                if (selected) {
                    clear()
                }

                text = text.substring(0, caretPosition) + character + text.substring(caretPosition, text.length)

                caretPosition++
            }
        } else {
            when (key) {
                UIKey.KEY_BACKSPACE -> {
                    if (text.isNotEmpty() && caretPosition > 0) {
                        text = text.substring(0, caretPosition - 1) + text.substring(caretPosition, text.length)
                        caretPosition--
                    }
                }
                UIKey.KEY_LEFT -> caretPosition = (caretPosition - 1).coerceAtLeast(0)
                UIKey.KEY_RIGHT -> caretPosition = (caretPosition + 1).coerceAtMost(text.length)
                else -> {}
            }
        }
        updateTextField()
        super.keyPressed(character, key)
    }

    /**
     * Updates the caret position and selection of the text field. This should be invoked
     * when the text or caret position has been changed.
     *
     * @see select
     * @see deselect
     * @see clear
     */
    fun updateTextField() {
        // TODO:
    }

    /**
     * Selects the text with the [startingIndex] as the caret position and the [endingIndex] as the place to select
     */
    fun select(startingIndex: Int, endingIndex: Int) {
        caretPosition = startingIndex
        caretEndPosition = endingIndex
        selected = true
        updateTextField()
    }

    /**
     * Deselects the text if avaliable
     */
    fun deselect() {
        caretEndPosition = caretPosition
        selected = false
        updateTextField()
    }

    /**
     * Resets hte selection and clears the text
     */
    fun clear() {
        deselect()
        text = ""
        updateTextField()
    }

    // ontextfield update

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
//
//import net.prismclient.aether.ui.net.prismclient.aether.ui.UICore
//import net.prismclient.aether.ui.component.UIComponent
//import net.prismclient.aether.ui.component.type.input.textfield.UITextField.TextFlavor
//import net.prismclient.aether.ui.component.util.interfaces.UIFocusable
//import net.prismclient.aether.ui.renderer.impl.font.UIFont
//import net.prismclient.aether.ui.style.impl.UITextFieldSheet
//import net.prismclient.aether.ui.util.input.UIKey
//import net.prismclient.aether.ui.util.extensions.renderer
//import java.lang.Integer.max
//import java.lang.Integer.min
//
///**
// * [UITextField] is used for collecting user input. It supports general one-line text inputs which can be used for
// * anything from passwords, to searchbars.
// *
// * @author sen
// * @since 4/27/2022
// * @param text The active input value. If empty and not focused, [placeholder] text will render instead.
// * @param placeholder The text to render if the text field is not active and empty
// * @param inputFlavor The charset to which the text field is limited to. See [TextFlavor] for more info.
// * @param maxLength The maximum length the text box can render. If -1, the length is infinite
// * @param style The style sheet for the component
// */
//open class UITextField @JvmOverloads constructor( // https://chakra-ui.com/docs/components/form/input
//    var text: String,
//    var placeholder: String,
//    var inputFlavor: TextFlavor = any,
//    var maxLength: Int = -1,
//    style: String
//) : UIComponent<UITextFieldSheet>(style), UIFocusable<UITextField> {
//    var focused = false
//    var selected = false
//
//    /**
//     * 0 = was not clicked
//     * 1 = clicked once
//     * 2 = double clicked (select word)
//     * 3 = triple clicked (select line)
//     */
//    var wasClicked: Byte = 1 // TODO: Copy and Paste actions, double click to select all
//
//    /**
//     * If false, when the text field is defocused the text will be cleared if applicable.
//     */
//    var shouldRetain = true
//
//    /**
//     * The time available for a double click to select the entire text, or a specific part
//     */
//    var clickDelay = 500L
//
//    var caretPosition: Int = 0
//        protected set
//
//    var caretX = 0f
//    var caretY = 0f
//    var caretWidth = 0f
//    var caretHeight = 0f
//
//    var hasSelection = false
//        protected set
//
//    var clickTime = 0L
//        protected set
//
//    /**
//     * The position where the caret to this is selected
//     */
//    var selectionPosition: Int = caretPosition
//        protected set
//    var selectionX = 0f
//        protected set
//    var selectionWidth = 0f
//        protected set
//
//    init {
//        // If no font has been created, create one
//        if (this.style.font == null) {
//            this.style.font = UIFont()
//        }
//    }
//
//    override fun update() {
//        super.update()
//        updateCaret()
//    }
//
//    override fun renderComponent() {
//        style.font!!.fontColor = if (text.isEmpty()) style.placeholderColor else style.activeColor
//        style.font!!.render(getActiveText())
//        renderSelection()
//        renderCaret()
//    }
//
//    open fun renderCaret() {
//        if (focused) {
//            if (!hasSelection || caretPosition == selectionPosition) {
//                renderer {
//                    color(style.caretColor)
//                    rect(caretX, caretY, caretWidth, caretHeight, style.caretRadius)
//                }
//            }
//        }
//    }
//
//    open fun updateCaret() {
//        val x = style.font!!.cachedX
//        val y = style.font!!.cachedX
//        caretX = x + +style.caretX
//        caretY = y + -style.caretY
//        caretWidth = +style.caretWidth
//        caretHeight = -style.caretHeight
//        caretX -= caretWidth / 2f
//
//        if (caretPosition > 0 && text.isNotEmpty()) {
//            renderer {
//                caretX += text.substring(0, caretPosition).width()
//            }
//        }
//    }
//
//    open fun renderSelection() {
//        if (hasSelection) {
//            renderer {
//                color(style.selectionColor)
//                rect(selectionX, caretY, selectionWidth, caretHeight)
//            }
//        }
//    }
//
//    open fun updateSelection(mouseX: Float) {
//        // Using the same algorithm to find the character the mouse pressed,
//        // determine where to move [selectionPosition]. Instead of setting
//        // the caret position, set the selection position.
//        renderer {
//            font(style.font!!) // Apply the font (in case its properties have been lost)
//            var x = style.font!!.cachedX
//            var previous = 0f
//            var flag = false // flag = found position
//            for (i in text.indices) {
//                val charWidth = text[i].toString().width() / 2f
//                if (mouseX >= x && mouseX <= x + previous + charWidth) {
//                    // If within the bound of the second half of the previous
//                    // character and the first half of this character, then we
//                    // know this is the right spot.
//                    selectionPosition = i
//                    flag = true
//                    break
//                }
//                x += previous + charWidth
//                previous = charWidth
//            }
//            // If not flagged, check if it's the farthest spot to the right
//            if (!flag && mouseX >= x && mouseX <= this@UITextField.x + this@UITextField.width) {
//                selectionPosition = text.length
//            }
//
//            // Update the variables
//            selectionX = style.font!!.cachedX + text.substring(0, caretPosition).width()
//            selectionWidth = text.substring(0, selectionPosition).width() - text.substring(0, caretPosition).width()
//            hasSelection = true
//        }
//    }
//
//    /**
//     * Creates a selection from the provided points
//     *
//     * @param startArea Moves the caret to the index in the text to start the selection
//     * @param endArea The index in the text to end the selection
//     */
//    open fun select(startArea: Int, endArea: Int) {
//        caretPosition = startArea
//        selectionPosition = endArea
//        hasSelection = true
//        renderer {
//            selectionX = style.font!!.cachedX + text.substring(0, caretPosition).width()
//            selectionWidth = text.substring(0, selectionPosition).width() - text.substring(0, caretPosition).width()
//        }
//        hasSelection = true
//    }
//
//    /**
//     * Invoked when the mouse is double/triple clicked within [clickTime] to select a word or the entire text
//     */
//    open fun selectArea() {
//        if (text.isEmpty()) return
//        val caretPosition = min(this.caretPosition, text.length - 1)
//        if (wasClicked == 2.toByte()) { // Select word
//            if (text[caretPosition].isLetterOrDigit()) {
//                var start = 0
//                var end = 0
//
//                for (i in caretPosition downTo 0) {
//                    if (!text[i].isLetterOrDigit())
//                        break
//                    start = i
//                }
//
//                for (i in caretPosition until text.length) {
//                    if (!text[i].isLetterOrDigit())
//                        break
//                    end = i + 1
//                }
//
//                select(start, end)
//            } else {
//                if (caretPosition == text.length) {
//                    select(text.length - 1, text.length)
//                } else {
//                    select(caretPosition, caretPosition + 1)
//                }
//            }
//        } else if (wasClicked == 3.toByte()) { // Select line
//            select(0, text.length)
//        } else {
//            wasClicked = 1
//        }
//    }
//
//    fun deselect() {
//        selected = false
//        hasSelection = false
//        selectionPosition = caretPosition
//    }
//
//    /**
//     * Inserts the given text into the current caret position. Does not check for input filter
//     */
//    open fun insert(insert: String) {
//        if (selected || hasSelection) {
//            val min = min(caretPosition, selectionPosition)
//            text = text.replace(text.substring(min, max(caretPosition, selectionPosition)), "")
//            text = text.substring(0, min) + insert + text.substring(min, text.length)
//            caretPosition = min
//            deselect()
//        } else {
//            text = text.substring(0, caretPosition) + insert + text.substring(caretPosition, text.length)
//        }
//        caretPosition += insert.length
//        updateCaret()
//    }
//
//    open fun getActiveText(): String = text.ifEmpty { if (focused) text else placeholder }
//
//    /**
//     * Returns the text for the selection
//     */
//    open fun getSelectedText(): String = if (hasSelection) text.substring(
//        min(caretPosition, selectionPosition),
//        max(caretPosition, selectionPosition)
//    ) else ""
//
//    /**
//     * Inserts the given text into the caret position, and omits the selection if applicable
//     */
//    open fun String.pasteText() {
//        for (c in this.indices) {
//            if (this[c].has() && (text.length <= maxLength || maxLength == -1)) {
//                insert(c.toString())
//            }
//        }
//    }
//
//    open fun clear() {
//        text = ""
//        caretPosition = 0
//        selectionPosition = 0
//        hasSelection = false
//    }
//
//    override fun mouseClicked(mouseX: Float, mouseY: Float) {
//        super.mouseClicked(mouseX, mouseY)
//        if (!focused) {
//            deselect()
//            return
//        }
//
//        hasSelection = false
//
//        /* Calculate what spot did the user click in */
//        renderer {
//            var x = style.font!!.cachedX
//            val y = this@UITextField.y
//            var previous = 0f
//            val h = this@UITextField.height
//
//            if (mouseY >= y && mouseY <= y + h) { // Check if inside the y bounds
//                var flag = false // flag = found position
//                for (i in text.indices) {
//                    val charWidth = text[i].toString().width() / 2f
//                    if (mouseX >= x && mouseX <= x + previous + charWidth) {
//                        // If within the bound of the second half of the previous
//                        // character and the first half of this character, then we
//                        // know this is the right spot.
//                        caretPosition = i
//                        flag = true
//                        break
//                    }
//                    x += previous + charWidth
//                    previous = charWidth
//                }
//                // If not flagged, check if it's the farthest spot to the right
//                if (!flag && mouseX >= x && mouseX <= this@UITextField.x + this@UITextField.width) {
//                    caretPosition = text.length
//                }
//            }
//        }
//        updateCaret() // Update the caret position after finding the new spot
//        selected = true // Enable eligibility for selection
//
//        /* Update [wasClicked] */
//        wasClicked++
//        if (System.currentTimeMillis() - clickTime < clickDelay) {
//            selectArea()
//        } else {
//            wasClicked = 1
//        }
//        clickTime = System.currentTimeMillis()
//    }
//
//    override fun mouseReleased(mouseX: Float, mouseY: Float) {
//        super.mouseReleased(mouseX, mouseY)
//        selected = false // Remove eligibility for selection
//    }
//
//    override fun mouseMoved(mouseX: Float, mouseY: Float) {
//        super.mouseMoved(mouseX, mouseY)
//        if (selected) {
//            updateSelection(mouseX)
//        }
//    }
//
//    override fun keyPressed(key: UIKey, character: Char) {
//        super.keyPressed(key, character)
//
//        if (!focused) return
//
//        when (key) {
//            UIKey.BACKSPACE -> if (selected || hasSelection) {
//                insert("")
//            } else {
//                if (caretPosition > 0) {
//                    text = text.substring(0, caretPosition - 1) + text.substring(caretPosition)
//                    caretPosition--
//                }
//            }
//            UIKey.ARROW_LEFT -> {
//                if (net.prismclient.aether.ui.UICore.instance.shiftHeld) {
//                    select(caretPosition, max(0, selectionPosition - 1))
//                } else {
//                    caretPosition = max(0, caretPosition - 1)
//                    deselect()
//                }
//            }
//            UIKey.ARROW_RIGHT -> {
//                if (net.prismclient.aether.ui.UICore.instance.shiftHeld) {
//                    select(caretPosition, min(text.length, selectionPosition + 1))
//                } else {
//                    caretPosition = min(text.length, caretPosition + 1)
//                    deselect()
//                }
//            }
//            UIKey.SPACE -> {
//                text = text.substring(0, caretPosition) + " " + text.substring(caretPosition)
//                caretPosition++
//            }
//            else -> {
//                val ch = if (net.prismclient.aether.ui.UICore.instance.shiftHeld) Character.toUpperCase(character) else character
//
//                if (ch.has() && (text.length <= maxLength || maxLength == -1)) {
//                    insert(ch.toString())
//                }
//            }
//        }
//        updateCaret()
//    }
//
//    override fun focus() {
//        focused = true
//    }
//
//    override fun removeFocus() {
//        focused = false
//        if (!shouldRetain) {
//            clear()
//        }
//    }
//
//    /**
//     * Returns true if the provided [Char] is in the [TextFlavor] pattern
//     */
//    protected fun Char.has(): Boolean = inputFlavor.pattern.contains(this)
//
//    /**
//     * [TextFlavor] holds a pattern which is compared when text is
//     * given. See the companion object of [UITextField] for presets.
//     *
//     * @author sen
//     * @since 4/28/2022
//     */
//    class TextFlavor(val pattern: String)
//
//    companion object {
//        @JvmStatic
//        val any =
//            TextFlavor("ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~`!@#$%^&*()_+-=|,./<>?;':{}[]\"\\ ")
//
//        @JvmStatic
//        val alphabet = TextFlavor("ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
//
//        @JvmStatic
//        val number = TextFlavor("1234567890")
//
//        @JvmStatic
//        val hex = TextFlavor("#ABCDEFabcdef0123456789")
//    }
//}