package net.prismclient.aether.ui.renderer.impl.font

import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.font.UITextAlignment.*
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.style.util.UIAnchorPoint
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.calculate
import net.prismclient.aether.ui.util.extensions.renderer
import java.util.regex.Pattern

/**
 * [UIFont] is the core for rendering text on screen with components. It handles everything
 * from line breaking to text selection. The design is based off of Figma's text components.
 * Features such as []
 *
 * To break text by the width, set [lineBreakWidth] to a value.
 *
 * @author sen
 * @since 1.0
 */
open class UIFont : UIShape<UIFont>() {

    /**
     * The actual, unformatted original text provided to the font
     */
    var actualText: String = ""
        set(value) {
            field = value
            updateFont()
        }

    var anchor: UIAnchorPoint? = null

    var fontName: String? = null
    var fontSize: UIUnit? = null
    var fontColor: UIColor? = null
    var fontSpacing: UIUnit? = null

    var textResizing: TextResizing = TextResizing.AutoWidth

    var horizontalAlignment: UITextAlignment = CENTER
    var verticalAlignment: UITextAlignment = CENTER

    /**
     * The spacing between each line break.
     */
    var lineHeightSpacing: UIUnit? = null

    /**
     * The text to be appended to the string when it is truncated
     */
    var truncatedText: String? = null
        set(value) {
            field = value
            if (value != null) textResizing = TextResizing.TruncateText
        }

    // -- Internal properties -- //

    /**
     * The text based on [actualText] except a new line is created for `\n` and line break.
     */
    var text: ArrayList<String> = arrayListOf()

    /**
     * The bounds of the font, at [0, 0]
     */
    lateinit var textBounds: FloatArray
        protected set
    var cachedFontSize: Float = 0f
        protected set
    var cachedFontSpacing: Float = 0f
        protected set
    var fontAscender: Float = 0f
        protected set
    var fontDescender: Float = 0f
        protected set
    var cachedLineHeightSpacing: Float = 0f
        protected set

    var cachedAnchorX: Float = 0f
        protected set
    var cachedAnchorY: Float = 0f
        protected set

    // -- Shorthands -- //

    fun align(horizontal: UITextAlignment, vertical: UITextAlignment) = alignment(horizontal, vertical)

    fun alignment(horizontal: UITextAlignment, vertical: UITextAlignment) {
        horizontalAlignment = horizontal
        verticalAlignment = vertical
    }

    fun anchor(alignment: UIAlignment) {
        anchor = anchor ?: UIAnchorPoint()
        anchor!!.align(alignment)
    }

    // -- Core -- //

    /**
     * Calculates and updates the bounds for this.
     */
    fun updateFont() {
        val component = component ?: return

        // Adjust to the right resize type
        when {
            (textResizing == TextResizing.AutoWidth || textResizing == TextResizing.AutoHeight) && (width != null || height != null) -> textResizing = TextResizing.FixedSize
            textResizing == TextResizing.AutoWidth && actualText.contains(NEWLINE) -> textResizing = TextResizing.AutoHeight
        }

        update(component)
        text.clear()

        // Update initial properties
        cachedFontSize = calculate(fontSize, component, cachedWidth, cachedHeight, false)
        cachedFontSpacing = calculate(fontSpacing, component, cachedWidth, cachedHeight, false)
        cachedLineHeightSpacing = calculate(lineHeightSpacing, component, cachedWidth, cachedHeight, true)

        renderer {
            font(fontName ?: "", cachedFontSize, horizontalAlignment, verticalAlignment, cachedFontSpacing)
            when (textResizing) {
                TextResizing.AutoWidth -> {
                    val (minx, miny, maxx, maxy) = actualText.fontBounds()
                    text.add(actualText)

                    // Update component size
                    cachedWidth = maxx - minx
                    cachedHeight = maxy - miny
                }
                TextResizing.AutoHeight -> {
                    text.addAll(actualText.split(NEWLINE))
                    renderer.calculateText(text, 0f, 0f, cachedLineHeightSpacing)

                    val (minx, miny, maxx, maxy) = fontBounds()

                    // Update component size
                    cachedWidth = maxx - minx
                    cachedHeight = maxy - miny
                }
                TextResizing.FixedSize -> {
                    // TODO: Fixed size newline
                    renderer.calculateText(actualText, 0f, 0f, cachedWidth, cachedLineHeightSpacing, text)

                    val (_, miny, _, maxy) = fontBounds()

                    // Update component size
                    cachedHeight = (maxy - miny).coerceAtLeast(cachedHeight)
                }
                TextResizing.TruncateText -> {}
                TextResizing.Auto -> {
                    textResizing = TextResizing.AutoWidth
                    updateFont()
                    return
                }
            }

            // Update properties
            textBounds = fontBounds()
            fontAscender = fontAscender()
            fontDescender = fontDescender()
        }
        updateAnchor()
    }

    fun updateAnchor() {
        cachedAnchorX = calculate(anchor?.x, null, cachedWidth, cachedHeight, false)
        cachedAnchorY = calculate(anchor?.y, null, cachedWidth, cachedHeight, true)
    }

    override fun render() {
        val x = cachedX + when (horizontalAlignment) {
            CENTER -> cachedWidth / 2f
            RIGHT -> cachedWidth
            else -> 0f
        } - cachedAnchorX
        val y = cachedY + when (verticalAlignment) {
            CENTER -> cachedFontSize / 2f + (cachedHeight - (cachedFontSize * text.size) - (cachedLineHeightSpacing * (text.size - 1))) / 2f
            BOTTOM -> cachedFontSize + (cachedHeight - (cachedFontSize * text.size) - (cachedLineHeightSpacing * (text.size - 1)))
            else -> 0f
        } - cachedAnchorY
        renderer {
            color(fontColor)
            font(fontName ?: "", cachedFontSize, horizontalAlignment, verticalAlignment, cachedFontSpacing)
            when (textResizing) {
                TextResizing.AutoWidth -> actualText.render(x, y)
                TextResizing.AutoHeight -> renderer.renderText(text, x, y, cachedLineHeightSpacing)
                TextResizing.FixedSize -> renderer.renderText(actualText, x, y, cachedWidth, cachedLineHeightSpacing, null)
                TextResizing.TruncateText -> {} // TODO: Truncate text
                TextResizing.Auto -> throw RuntimeException("Auto should not be the text resizing mode when rendering???")
            }
//            color(asRGBA(1f, 1f, 1f, 0.3f))
//            rect(cachedX - cachedAnchorX, cachedY - cachedAnchorY, cachedWidth, cachedHeight)
        }
    }

    override fun copy(): UIFont = UIFont().also {
        it.apply(this)
        it.actualText = actualText
        it.fontName = fontName
        it.fontSize = fontSize?.copy()
        it.fontColor = fontColor?.copy()
        it.fontSpacing = fontSpacing?.copy()
        it.textResizing = textResizing
        it.verticalAlignment = verticalAlignment
        it.horizontalAlignment = horizontalAlignment
        it.lineHeightSpacing = lineHeightSpacing?.copy()
        it.truncatedText = truncatedText
        it.anchor = anchor?.copy()
    }

    /**
     * The equivalent of the Resizing property for fonts in Figma. See the respected enums
     * for information on what it does. Change [UIFont.textResizing] to adjust this property.
     *
     * @author sen
     * @since 1.3
     */
    enum class TextResizing {
        /**
         * Renders the string as a one line text where the width and height are the bounds of it. If
         * the text contains a line break, this will automatically be switched to [AutoHeight].
         */
        AutoWidth,

        /**
         * Like [AutoWidth], however it supports multi-line text. The bounds will adjust to the overall size of the text.
         */
        AutoHeight,

        /**
         * Creates a newline when the width of the text exceeds the size of this. The height
         * is adjusted to be at least the height of the text.
         */
        FixedSize,

        /**
         * Cuts the text off at the point where it exceeds the width of this, and append the string [UIFont.truncatedText]
         */
        TruncateText,

        /**
         * Automatically figures out which one is the best based on the properties of the font.
         */
        Auto
    }

    companion object {
        @JvmStatic
        protected val NEWLINE: Regex = Pattern.compile("\\r?\\n|\\r").toRegex()
    }
}