package net.prismclient.aether.ui.renderer.impl.font

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL.bounds
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL.boundsOf
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL.indexOffset
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.input.UIModifierKey
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.regex.Pattern

/**
 * [UIFont] controls the rendering of text on string. It is a default part
 * of [UIStyleSheet]. Because this is a shape, it has its own position
 * relative to the component and can be transformed your desire.
 *
 * Multiline text is an available feature. By default, it is impossible to render
 * multiline text. However, it can be enabled by changing [fontRenderType] to a multiline
 * supported type. Backend a stored variable, [cachedText] is used to store the text which
 * is then used to calculate selections and other things. If it is not a multiline supported
 * type, the text will always be a size of 1.
 *
 * An important feature pertain to this class is selection. The mouse and keyboard
 * are able to select a portion of singular, and multiline text. In the case of multiline
 * text, the length of each line is increased by 1 because of the ability to have the caret
 * at the end of a selection.
 *
 * Note: Properties width, and height are ignored. The [fontRenderType] property is null for
 * animations however if a string is attempted to be rendered while it is null, it is
 * automatically set to [FontRenderType.NORMAL].
 *
 * @author sen
 * @since 4/26/2022
 */
@Suppress("MemberVisibilityCanBePrivate")
open class UIFont : UIShape<UIFont>(), UIAnimatable<UIFont> {
    protected val newline: Pattern = Pattern.compile("\\r?\\n|\\r")

    /**
     * When true, the component will be ensured to be at
     * least the size of the font width and height. It does
     * not update the x and y positions of the component if
     * the text spans to the left or up
     */
    var overrideParent: Boolean = true

    /**
     * Instructs [UIFont] how to render text. The default value is [FontRenderType.NORMAL].
     *
     * @see FontRenderType
     * @see appendedString
     */
    var fontRenderType: FontRenderType? = null

    /**
     * The alignment of the text
     *
     * @see UIRenderer Text Alignment
     */
    var textAlignment = 0

    /**
     * Read-only property set by  [fontStyle], [fontType] and [fontFamily]. If when the font was loaded had a
     * special name, use [overwriteFontName] to set it to that font name.
     */
    var fontName: String = ""
        protected set

    /**
     * Specifies if the style is Normal or Italic
     *
     * @see FontStyle
     */
    var fontStyle: FontStyle? = null
        set(value) {
            field = value; updateFontName()
        }

    /**
     * The color of this font, represent in [UIColor].
     */
    var fontColor: UIColor? = null

    /**
     * The name of the font family.
     */
    var fontFamily = ""
        set(value) {
            field = value; updateFontName()
        }

    /**
     * Specifies the font type e.g. Regular, Bold, Thin etc..
     *
     * Default is [FontType.Regular]
     *
     * @see FontType
     */
    var fontType: FontType? = null
        set(value) {
            field = value; updateFontName()
        }

    var fontSize: UIUnit? = null

    /**
     * The spacing between each character.
     */
    var fontSpacing: UIUnit? = null

    /**
     * If the [fontRenderType] is WRAP, the text will wrap at the given width.
     */
    var lineBreakWidth: UIUnit? = null

    /**
     * If the [fontRenderType] is multiline, the next line of text will be the font
     * size plus the given value, or 0 if null
     */
    var lineHeight: UIUnit? = null

    /**
     * When applicable, the given text will be appended to the string.
     *
     * @see FontRenderType.APPEND
     */
    var appendedString: String? = null

    /**
     * Instructs Aether that this is a selectable font. Selectable fonts are highlighted
     * when the mouse clicks and drags over the font. It also allows for functions such
     * as copying the text.
     */
    var isSelectable = false

    /**
     * The color of the selection if [isSelectable]
     */
    var selectionColor: UIColor? = null

    /**
     * The amount of lines in the text. 1 by default if it is a single line render type
     *
     * @see fontRenderType
     */
    var lineCount = 1
        protected set

    /**
     * The bounds of the most recently rendered text. It represents the x, y
     * and the ending x, y positions of the text. The fifth value represents
     * the next position that the text would be rendered at.
     *
     * @see UIRenderer.textBounds
     */
    var textBounds: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f)

    /**
     * When true, the text is actively selected.
     *
     * @see isSelectable Must be true.
     */
    var selected = false
        protected set

    /**
     * The holder of the current position of the caret/cursor and the ending selection position (if applicable).
     * This is null if [isSelectable] is false
     */
    var position: Positions? = null

    /**
     * If true, fontName will not update automatically. It will only update when [overwriteFontName] is called
     *
     * @see overwriteFontName
     */
    var isOverridden = false
        protected set

    /**
     * If true, the font will not be updated. Used internally to avoid recursive calls.
     */
    protected var ignore = false

    var cachedFontSize: Float = 0f
        protected set
    var cachedFontSpacing: Float = 0f
        protected set
    var cachedLineBreakWidth: Float = 0f
        protected set
    var cachedLineHeight: Float = 0f
        protected set

    /**
     * An internal variable which holds the text in lines split if necessary.
     */
    var cachedText: ArrayList<String> = arrayListOf()
        protected set
    protected var isShiftHeld = false

    /**
     * Aligns the text to the given [alignment]
     */
    open fun align(alignment: UIAlignment) {
        x ?: run { x = px(0) }
        y ?: run { y = px(0) }
        align(alignment, x!!, y!!)
    }

    override fun update(component: UIComponent<*>?) {
        if (ignore) return
        super.update(component!!)

        cachedFontSize = calculate(fontSize, component, component.width, component.height, false)
        cachedFontSpacing = calculate(fontSpacing, component, component.width, component.height, false)

        // Selection handling
        if (isSelectable) {
            // Allocate, and add listeners for when the mouse is moved, pressed and released
            position = Positions(0, 0)
            val pos = position!!
            Aether.onMousePressed("$this-SelectionListener") { // TODO: function for bounds
                val mousePosition = getClosestTextIndex(component.getMouseX(), component.getMouseY())

                if (mousePosition != position!!.caretPosition) {
                    updateCaretPosition(mousePosition)
                    selected = true
                }
            }
            Aether.onMouseMove("$this-MoveListener") {
                if (selected) {
                    select(getClosestTextIndex(component.getMouseX(), component.getMouseY()), pos.selectionPosition)
                }
            }
            Aether.onMousePressed("$this-DeselectionListener") {
                if (component.getMouseY() <= cachedY + textBounds[1] && component.getMouseY() <= cachedY + textBounds[1] + cachedFontSize) {
                    deselect()
                }
            }
            Aether.onMouseReleased("$this-DeselectionListener") { selected = false }
            Aether.onModifierKeyChange("$this-ModifierListener") { key, value ->
                if (key == UIModifierKey.LEFT_SHIFT || key == UIModifierKey.RIGHT_SHIFT) isShiftHeld = !value
                if (value) return@onModifierKeyChange
                when (key) {
                    UIModifierKey.ARROW_LEFT -> {
                        if (hasSelection() && !isShiftHeld) updateCaretPosition(min(pos.caretPosition, pos.selectionPosition))
                        else if (isShiftHeld) select(getPreviousPosition(pos.caretPosition), pos.selectionPosition)
                        else updateCaretPosition(getPreviousPosition(pos.caretPosition))
                    }
                    UIModifierKey.ARROW_RIGHT -> {
                        if (hasSelection() && !isShiftHeld) updateCaretPosition(max(pos.caretPosition, pos.selectionPosition))
                        else if (isShiftHeld) select(getNextPosition(pos.caretPosition), pos.selectionPosition)
                        else updateCaretPosition(getNextPosition(pos.caretPosition))
                    }
                    else -> {} // TODO: Up and down keys
                }
            }
            Aether.onDeallocation("$this-DeallocationListener") {
                // Deallocate all previous event calls
                Aether.mousePressedListeners?.remove("$this-MoveListener")
                Aether.mousePressedListeners?.remove("$this-DeselectionListener")
                Aether.mousePressedListeners?.remove("$this-DeselectionListener")
            }
        } else {
            // Deallocate and remove the listener if it is not selectable
            position = null
            component.mousePressedListeners?.remove("UIFontSelectionListener")
            Aether.mousePressedListeners?.remove("$this-DeselectionListener")
        }

        if (component is UILabel) {
            render(component.text)
            val bounds = this.textBounds
            // Updates the component to ensure that the width, and
            // height are at least the size of the text rendered
            if (overrideParent && (textBounds[2] - cachedX > component.width || textBounds[3] - cachedY > component.height)) {
                component.width = (textBounds[2] - cachedX)
                component.height = (textBounds[3] - cachedY)
                component.calculateBounds()
                component.updateAnchorPoint()
                component.updatePosition()
                component.updateBounds()
                ignore = true
                component.updateStyle()
                ignore = false
            }
        }

        cachedLineBreakWidth = calculate(lineBreakWidth, component, component.width, component.height, false)
        cachedLineHeight = calculate(lineHeight, component, component.width, component.height, false)
    }

    @Deprecated("Use render(text: String) instead", ReplaceWith("render(text: String)"))
    override fun render() = throw IllegalStateException("Use render(text: String) instead")

    open fun render(text: String) {
        fontRenderType = fontRenderType ?: FontRenderType.NORMAL
        // TODO: Multiline
        lineCount = 1
        renderer {
            font(this@UIFont)
            when (fontRenderType) {
                FontRenderType.NORMAL -> {
                    cachedText.clear()
                    cachedText.add(0, text)
                    text.render(cachedX, cachedY)
                    setBounds()
                } // TODO: \n available by default
                FontRenderType.NEWLINE -> {
                    cachedText.clear()
                    val lines = text.split(newline)
                    var minx = Float.MAX_VALUE
                    var miny = Float.MAX_VALUE
                    var maxx = 0f
                    var maxy = 0f

                    for (i in lines.indices) {
                        val line = lines[i]
                        cachedText.add(line)
                        line.render(cachedX, cachedY + i * (cachedLineHeight + cachedFontSize))
                        minx = bounds()[0].coerceAtMost(minx)
                        miny = bounds()[1].coerceAtMost(miny)
                        maxx = bounds()[2].coerceAtLeast(maxx)
                        maxy = bounds()[3].coerceAtLeast(maxy)
                    }
                    textBounds[0] = minx
                    textBounds[1] = miny
                    textBounds[2] = maxx
                    textBounds[3] = maxy
                    textBounds[4] = maxx
                }
                FontRenderType.WRAP -> {
                    cachedText.clear()
                    lineCount = render.wrapString(text, cachedX, cachedY, cachedLineBreakWidth, cachedLineHeight, cachedText)
                    setBounds()
                } // TODO: Clip & Append
                FontRenderType.CLIP -> {}
                FontRenderType.APPEND -> {}
                else -> {}
            }
        }
        renderSelection()
    }

    /**
     * As the name suggests, the selection is rendered over the selected text.
     */
    open fun renderSelection() {
        if (position != null) {
            renderer {
                color(selectionColor)
                val caretLine = getLine(position!!.caretPosition)
                val selectionLine = getLine(position!!.selectionPosition)

                if (caretLine == selectionLine) {
                    val caretX = getXOffset(position!!.caretPosition)
                    rect(cachedX + caretX, cachedY + getYOffset(position!!.caretPosition), getXOffset(position!!.selectionPosition) - caretX, cachedFontSize)
                } else {
                    val larger = max(position!!.caretPosition, position!!.selectionPosition)
                    val smaller = min(position!!.caretPosition, position!!.selectionPosition)
                    val largerLine = getLine(larger)
                    val smallerLine = getLine(smaller)

                    val smallBounds = boundsOf(cachedText[smallerLine])
                    val smallerX = getXOffset(smaller)
                    val smallerY = getYOffset(smaller)

                    // Start selection
                    rect(cachedX + smallerX, cachedY + smallerY, smallBounds[4] + smallBounds[0] - smallerX, cachedFontSize)

                    var y = cachedY + ((cachedFontSize + cachedLineHeight) * (smallerLine + 1)) - boundsOf(cachedText[0])[1]

                    // Fully selected lines
                    for (i in smallerLine + 1 until (largerLine - smallerLine) + smallerLine) {
                        val bounds = boundsOf(cachedText[i])
                        rect(cachedX + bounds[0],  bounds[1] + y, bounds[4], bounds[3] - bounds[1])
                        y += cachedLineHeight + cachedFontSize
                    }

                    val bounds = boundsOf(cachedText[largerLine])

                    // End selection
                    rect(cachedX + bounds[0], bounds[1] + y, getXOffset(larger) - bounds[0], cachedFontSize)
                }
            }
        }
    }

    /**
     * Selects the text with the [startingIndex] as the caret position and the [endingIndex]
     * as the place to select. Does nothing if this is not [isSelectable].
     */
    open fun select(startingIndex: Int, endingIndex: Int) {
        if (position != null) {
            position!!.caretPosition = startingIndex
            position!!.selectionPosition = endingIndex
        }
    }

    /**
     * Deselects the text. It does nothing if this is not [isSelectable].
     */
    open fun deselect() {
        if (position != null) {
            position!!.selectionPosition = position!!.caretPosition
        }
    }

    /**
     * Returns the next position of the given [index]. Because there is an extra
     * position add per line, this will return the previous position of the line.
     */
    open fun getNextPosition(index: Int): Int {
        var i = 0
        for (line in cachedText) {
            if (index == i + line.length) return (index + 2).coerceAtMost(getTextLength())
            i += line.length + 1
        }
        return (index + 1).coerceAtMost(getTextLength())
    }

    /**
     * Returns the previous position of the given [index]. Because there is an extra
     * position add per line, this will return the previous position of the line.
     */
    open fun getPreviousPosition(index: Int): Int {
        var i = 0
        for (line in cachedText) {
            if (index == i) return (index - 2).coerceAtLeast(0)
            i += line.length + 1
        }
        return (index - 1).coerceAtLeast(0)
    }

    /**
     * Changes the caret position to [index]. This does nothing if this is not [isSelectable]
     */
    open fun updateCaretPosition(index: Int) {
        if (position != null) {
            deselect()
            position!!.caretPosition = index
            position!!.selectionPosition = index
        }
    }

    /**
     * Returns the closest index of text relative to the [mouseX] and
     * [mouseY]. If out of bounds, returns the caret position.
     */
    open fun getClosestTextIndex(mouseX: Float, mouseY: Float): Int {
        var yOffset = boundsOf(cachedText[0])[1]
        var i = 0
        for (row in cachedText) {
            val rowBounds = boundsOf(row)
            val y = cachedY + yOffset

            yOffset += cachedFontSize + cachedLineHeight

            // Row check
            if (mouseY < y || mouseY > y + cachedFontSize + cachedLineHeight) {
                // Caret can be at the end spot
                i += row.length + 1
                continue
            }
            // Vertical row check. If the mouse is at
            // the end or start, return the start, or end index
            if (mouseX <= cachedX + rowBounds[0]) {
                return i
            } else if (mouseX >= cachedX + rowBounds[2]) {
                return i + row.length
            }

            var x = cachedX + rowBounds[0]
            var previous = 0f

            // Iterate through the row. Return the character at the index closest
            // to the mouse for the first portion of it, and the last portion of
            // the previous character.
            for (j in row.indices) {
                val width = boundsOf(row[j].toString())[4] / 2f

                if (mouseX >= x && mouseX <= x + width + previous) return i

                x += width + previous
                previous = width

                i++
                // Check if the last half of the last character
                // in this row is the closest to the mouse
                if (j == row.length - 1 && mouseX >= width) return i
            }
            i++ // Add one for each row because the caret can be at the end of the row
        }
        return position!!.caretPosition
    }

    /**
     * Returns the x offset of the given [index] in the text. Returns 0f if index is out of bounds.
     */
    open fun getXOffset(index: Int): Float {
        var i = 0
        for (row in cachedText) {
            if (index <= i + row.length) return row.indexOffset(index - i)
            i += row.length + 1
            if (index == i - 1) return 0f
        }
        return 0f
    }

    /**
     * Returns the y offset of the caret position. Returns 0f if [index] is out of bounds.
     */
    open fun getYOffset(index: Int): Float {
        val line = getLine(index)
        return boundsOf(cachedText[getLine(index)])[1] + line * (cachedFontSize + cachedLineHeight)
    }

    /**
     * Returns true if the [Positions.caretPosition] and [Positions.selectionPosition] aren't equal.
     */
    open fun hasSelection() = position!!.caretPosition != position!!.selectionPosition

    /**
     * Returns the line index of the given index. Returns the first line if the index is out of bounds.
     */
    open fun getLine(index: Int): Int {
        var j = 0
        for (i in cachedText.indices) {
            if (index <= cachedText[i].length + j) return i
            j += cachedText[i].length + 1
        }
        return 0
    }

    /**
     * Returns the length of [cachedText]
     */
    open fun getTextLength() = cachedText.sumOf { it.length + 1 } - 1

    /**
     * Returns the position of the ascender line in the current font
     */
    open fun getAscend(): Float {
        UIRendererDSL.font(this)
        return UIRendererDSL.ascender()
    }

    /**
     * Returns the position of the descender line in the current font
     */
    open fun getDescend(): Float {
        UIRendererDSL.font(this)
        return UIRendererDSL.descender()
    }

    /**
     * If the font was loaded with a special name, use this function to override the default formatting
     */
    open fun overwriteFontName(name: String) {
        fontName = name
        isOverridden = true
    }

    /**
     * Updates [fontName] based on the [fontStyle], [fontType] and [fontFamily]
     */
    protected open fun updateFontName() {
        if (isOverridden) return
        fontName = fontFamily + "-" + (fontType?.name?.lowercase() ?: "regular") + if (fontStyle == FontStyle.Italic) "-italic" else ""
    }

    private fun setBounds() {
        val bounds = bounds()
        textBounds[0] = bounds[0]
        textBounds[1] = bounds[1]
        textBounds[2] = bounds[2]
        textBounds[3] = bounds[3]
        textBounds[4] = bounds[4]
    }

    override fun animate(animation: UIAnimation<*, *>, previous: UIFont?, current: UIFont?, progress: Float) {
        super.animate(animation, previous, current, progress)
        fontColor?.rgba = previous?.fontColor.mix(current?.fontColor, progress)
        cachedFontSize = previous?.fontSize.lerp(current?.fontSize, animation.component, progress, false)
        cachedLineBreakWidth = previous?.lineBreakWidth.lerp(current?.lineBreakWidth, animation.component, progress, false)
        cachedLineHeight = previous?.lineHeight.lerp(current?.lineHeight, animation.component, progress, true)
        selectionColor?.rgba = previous?.selectionColor.mix(current?.selectionColor, progress)
    }

    override fun save(animation: UIAnimation<*, *>, keyframe: UIFont?) {
        super.save(animation, keyframe)
        overrideParent = keyframe?.overrideParent ?: overrideParent
        fontRenderType = keyframe?.fontRenderType ?: fontRenderType
        textAlignment = keyframe?.textAlignment ?: textAlignment
        fontName = keyframe?.fontName ?: fontName
        fontStyle = keyframe?.fontStyle ?: fontStyle
        fontColor = keyframe?.fontColor ?: fontColor
        fontStyle = keyframe?.fontStyle ?: fontStyle
        fontFamily = keyframe?.fontFamily ?: fontFamily
        fontType = keyframe?.fontType ?: fontType
        fontSize = keyframe?.fontSize ?: fontSize
        fontSpacing = keyframe?.fontSpacing ?: fontSpacing
        lineBreakWidth = keyframe?.lineBreakWidth ?: lineBreakWidth
        lineHeight = keyframe?.lineHeight ?: lineHeight
        appendedString = keyframe?.appendedString ?: appendedString
        isSelectable = keyframe?.isSelectable ?: isSelectable
        selectionColor = keyframe?.selectionColor ?: selectionColor
    }

    /**
     * Instructs [UIFont] on how to render the text. See the enums for details.
     *
     * @author sen
     * @since 1.0
     */
    enum class FontRenderType {
        /**
         * Renders the string normally, as a one line string.
         */
        NORMAL,

        /**
         * Creates a new line when \n is appended to a string
         */
        NEWLINE,

        /**
         * Omits any text that exceeds the bounds of [lineBreakWidth].
         */
        CLIP,

        /**
         * If the string exceeds the bounds, the text will be wrapped
         */
        WRAP,

        /**
         * Renders a string until the given width, where then the string is
         * truncated to the point, and the appended string is added. For example,
         * if the text is "Hello", the appended string is ".." and "Hel.." width
         * is greater than width, then the string rendered is "Hel..". If appended
         * string is blank, the string is cut off at the given width point, like normal
         * clipped text.
         */
        APPEND
    }

    /**
     * The style of the font: Normal or Italic
     *
     * @author sen
     * @since 1.0
     */
    enum class FontStyle {
        Normal, Italic
    }

    /**
     * The type of the font: Regular, Bold, Thin etc..
     *
     * @author sen
     * @since 1.0
     */
    enum class FontType {
        Regular, Medium, Black, Bold, Light, Thin
    }

    /**
     * [Positions] is automatically allocated when the property [isSelectable] is true. It describes
     * the position which the caret is at; the position where the mouse clicked relative to the font. It
     * also holds the ending point if a selection in the font is active.
     *
     * @author sen
     * @since 1.0
     */
    class Positions(var caretPosition: Int, var selectionPosition: Int)

    override fun copy(): UIFont = UIFont().also {
        it.apply(this)

        it.overrideParent = overrideParent
        it.fontRenderType = fontRenderType
        it.textAlignment = textAlignment
        it.fontStyle = fontStyle
        it.fontType = fontType
        it.fontColor = fontColor?.copy()
        it.fontFamily = fontFamily
        it.fontSize = fontSize?.copy()
        it.fontSpacing = fontSpacing?.copy()
        it.lineBreakWidth = lineBreakWidth?.copy()
        it.lineHeight = lineHeight?.copy()
        it.appendedString = appendedString
        it.isSelectable = isSelectable
        it.selectionColor = selectionColor

        if (isOverridden) it.overwriteFontName(fontName)
    }
}