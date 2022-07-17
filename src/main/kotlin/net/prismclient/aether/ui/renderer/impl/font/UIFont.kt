package net.prismclient.aether.ui.renderer.impl.font

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.TextAlignment.*
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.calculate
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.middle
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

    var fontName: String = ""
    var fontSize: UIUnit? = null
    var fontColor: UIColor? = null
    var fontSpacing: UIUnit? = null

    var textResizing: TextResizing = TextResizing.AutoWidth

    var horizontalAlignment: TextAlignment = CENTER
    var verticalAlignment: TextAlignment = CENTER

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

    var textBounds: FloatArray = floatArrayOf()
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

    // -- Shorthands -- //

    fun size(width: UIUnit?, height: UIUnit?) {
        this.width = width
        this.height = height
    }

    fun size(width: Number, height: Number) = size(px(width), px(height))

    fun align(horizontal: TextAlignment, vertical: TextAlignment) = alignment(horizontal, vertical)

    fun alignment(horizontal: TextAlignment, vertical: TextAlignment) {
        horizontalAlignment = horizontal
        verticalAlignment = vertical
    }

    // -- Core -- //

    override fun update(component: UIComponent<*>?) {
        super.update(component)
        updateFont()
    }

    /**
     * Calculates and updates the bounds for this.
     */
    fun updateFont() {
        val component = component ?: throw RuntimeException("Component cannot be null for UIFont")
        text.clear()

        // Update initial properties
        cachedFontSize = calculate(fontSize, component, cachedWidth, cachedHeight, false)
        cachedFontSpacing = calculate(fontSpacing, component, cachedWidth, cachedHeight, false)
        cachedLineHeightSpacing = calculate(lineHeightSpacing, component, cachedWidth, cachedHeight, true)

        renderer {
            font(fontName, cachedFontSize, horizontalAlignment, verticalAlignment, cachedFontSpacing)
            when (textResizing) {
                TextResizing.AutoWidth -> {
                    if (actualText.contains(NEWLINE)) {
                        textResizing = TextResizing.AutoHeight
                        updateFont()
                        return
                    }
                    text.add(actualText)

                    val (minx, miny, maxx, maxy) = actualText.fontBounds()

                    // Update component size
                    cachedWidth = maxx - minx
                    cachedHeight = maxy - miny
                }
                TextResizing.AutoHeight -> {
                    if (width != null || height != null) {
                        textResizing = TextResizing.FixedSize
                        updateFont()
                        return
                    }
                    text.addAll(actualText.split(NEWLINE))

                    renderer.calculateText(text, 0f, 0f, cachedLineHeightSpacing)

                    val (minx, miny, maxx, maxy) = fontBounds()

                    // Update component size
                    cachedWidth = maxx - minx
                    cachedHeight = maxy - miny
                }
                TextResizing.FixedSize -> {
                    renderer.calculateText(actualText, 0f, 0f, cachedWidth, cachedLineHeightSpacing, text)

                    val (minx, miny, maxx, maxy) = fontBounds()

                    // Update component size
                    cachedHeight = (maxy - miny).coerceAtLeast(cachedHeight)
                }
                TextResizing.TruncateText -> {}
            }

            // Update properties
            fontAscender = fontAscender()
            fontDescender = fontDescender()
        }
    }

    /**
     * Splits the active text into [activeText]
     */
    fun splitText() {

    }

    override fun render() {
        val x = cachedX + when (verticalAlignment) {
            CENTER -> cachedWidth / 2f
            RIGHT -> cachedWidth
            else -> 0f
        }
        val y = cachedY + when (horizontalAlignment) {
            CENTER -> cachedFontSize / 2f + (cachedHeight - (cachedFontSize * text.size) - (cachedLineHeightSpacing * (text.size - 1))) / 2f
            BOTTOM -> cachedFontSize + (cachedHeight - (cachedFontSize * text.size) - (cachedLineHeightSpacing * (text.size - 1)))
            else -> 0f
        }
        renderer {
            color(fontColor)
            font(fontName, cachedFontSize, verticalAlignment, horizontalAlignment, cachedFontSpacing)
            when (textResizing) {
                TextResizing.AutoWidth -> actualText.render(x, y)
                TextResizing.AutoHeight -> renderer.renderText(text, x, y, cachedLineHeightSpacing)
                TextResizing.FixedSize -> renderer.renderText(actualText, x, y, cachedWidth, cachedLineHeightSpacing, null)
                TextResizing.TruncateText -> {} // TODO: Truncate text
            }
            color(asRGBA(1f, 0f, 0f, 0.3f))
            rect(cachedX, cachedY, cachedWidth, cachedHeight)
        }
    }

    override fun copy(): UIFont = UIFont().also {
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
        TruncateText
    }

    companion object {
        @JvmStatic
        protected val NEWLINE: Regex = Pattern.compile("\\r?\\n|\\r").toRegex()
    }
}