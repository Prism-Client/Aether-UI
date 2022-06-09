package net.prismclient.aether.ui.renderer.impl.font

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.interfaces.UIAnimatable

/**
 * [UIFont] controls the rendering of text on string. It is a default part
 * of [UIStyleSheet]. Because this is a shape, it has its own position
 * relative to the component and can be transformed your desire.
 *
 * Note: Properties width, and height are ignored.
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIFont : UIShape(), UIAnimatable<UIFont> {
    /**
     * When true, the component will be ensured to be at
     * least the size of the font width and height. It does
     * not update the x and y positions of the component if
     * the text spans to the left or up
     */
    var overrideParent: Boolean = true

    /**
     * Instructs [UIFont] how to render text.
     *
     * @see FontRenderType
     * @see appendedString
     */
    var fontRenderType: FontRenderType = FontRenderType.NORMAL

    /**
     * The alignment of the text
     *
     * @see UIRenderer Text Alignment
     */
    var textAlignment = UIDefaults.instance.textAlignment

    /**
     * Read-only property set by  [fontStyle], [fontType] and [fontFamily]. If when the font was loaded had a
     * special name, use [overwriteFontName] to set it to that font name.
     */
    var fontName: String = UIDefaults.instance.fontName
        private set

    /**
     * Specifies if the style is Normal or Italic
     *
     * @see FontStyle
     */
    var fontStyle = UIDefaults.instance.fontStyle
        set(value) {
            field = value; updateFontName()
        }

    var fontColor = UIDefaults.instance.fontColor

    /**
     * The name of the font family.
     */
    var fontFamily = UIDefaults.instance.fontFamily
        set(value) {
            field = value; updateFontName()
        }

    /**
     * Specifies the font type e.g. Regular, Bold, Thin etc..
     *
     * @see FontType
     */
    var fontType = UIDefaults.instance.fontType
        set(value) {
            field = value; updateFontName()
        }

    var fontSize = UIDefaults.instance.fontSize

    /**
     * The spacing between each character.
     */
    var fontSpacing = UIDefaults.instance.fontSpacing

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
     * The amount of lines in the text. 1 by default if it is a single line render type
     *
     * @see fontRenderType
     */
    var lineCount = 1
        protected set

    /**
     * The width of the text
     */
    var textWidth = 0f
        protected set

    /**
     * The height of the text
     */
    var textHeight = 0f
        protected set

    /**
     * If true, fontName will not update automatically. It will only update when [overwriteFontName] is called
     *
     * @see overwriteFontName
     */
    var isOverridden = false
        private set

    /**
     * If true, the font will not be updated. Used internally to avoid recursive calls
     */
    private var ignore = false

    var cachedLineBreakWidth: Float = 0f
        private set
    var cachedLineHeight: Float = 0f
        private set

    open fun align(alignment: UIAlignment) {
        x ?: run { x = px(0) }
        y ?: run { y = px(0) }
        align(alignment, x!!, y!!)
    }

    override fun update(component: UIComponent<*>?) {
        if (ignore) return
        super.update(component)
        cachedLineBreakWidth = calculate(lineBreakWidth, component, component!!.x, component.y, false)
        cachedLineHeight = calculate(lineHeight, component, component.x, component.y, true)

        if (component is UILabel) {
            render(component.text)
            // Updates the component to ensure that the width, and
            // height are at least the size of the text rendered
            if (overrideParent && (textWidth > component.width || textHeight > component.height)) {
                component.width = textWidth.coerceAtLeast(component.width)
                component.height = textHeight.coerceAtLeast(component.height)
                component.calculateBounds()
                component.updateAnchorPoint()
                component.updatePosition()
                component.updateBounds()
                ignore = true
                component.updateStyle()
                ignore = false
            }
        }
    }

    @Deprecated("Use render(text: String) instead", ReplaceWith("render(text: String)"))
    override fun render() {
        throw IllegalStateException("Use render(text: String) instead")
    }

    open fun render(text: String) {
        lineCount = 1
        renderer {
            font(this@UIFont)
            when (fontRenderType) {
                FontRenderType.NORMAL -> text.render(cachedX, cachedY)
                FontRenderType.NEWLINE -> {

                }
                FontRenderType.WRAP -> lineCount = text.render(cachedX, cachedY, cachedLineBreakWidth, cachedLineHeight)
                FontRenderType.CLIP, FontRenderType.APPEND -> {
                    textWidth = text.render(cachedX, cachedY, component!!.relX + cachedLineBreakWidth, null, false)
                }
            }

            textWidth = (x() + width()) - cachedX + (cachedX - component!!.x)
            textHeight = (y() + height()) - cachedY + (cachedY - component!!.y)
        }
    }

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
        fontName = fontFamily + "-" + fontType.name.lowercase() + if (fontStyle == FontStyle.Italic) "-italic" else ""
    }

    protected var fontCache: FontCache? = null

    override fun updateAnimationCache(component: UIComponent<*>) {

    }

    override fun clearAnimationCache() {

    }

    override fun animate(previous: UIFont?, current: UIFont?, progress: Float, component: UIComponent<*>) {
        fontCache = fontCache ?: FontCache(fontColor, fontSize, fontSpacing, lineBreakWidth, lineHeight)
        // fontColor
        // fontSize
        // fontSpacing
        // lineBreakWidth
        // lineHeight
        fontColor = transition(current?.fontColor ?: fontCache!!.fontColor, previous?.fontColor ?: fontCache!!.fontColor, progress)
        fontSize = fromProgress(previous?.fontSize ?: fontCache!!.fontSize, current?.fontSize ?: fontCache!!.fontSize, progress)
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIFont?, retain: Boolean) {
        if (fontCache == null)
            throw RuntimeException("WTF")
        //TODO("Not yet implemented")
        if (retain) {
            fontColor = keyframe?.fontColor ?: fontCache!!.fontColor
        }
    }

    /**
     * Instructs [UIFont] on how to render the text. See the enums for details.
     *
     * @author sen
     * @since 5/15/2022
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
         * Omits any text that exceeds the bounds.
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
     * @since 5/15/2022
     */
    enum class FontStyle {
        Normal,
        Italic
    }

    /**
     * The type of the font: Regular, Bold, Thin etc..
     *
     * @author sen
     * @since 5/15/2022
     */
    enum class FontType {
        Regular,
        Black,
        Bold,
        Light,
        Thin
    }

    override fun copy(): UIFont = UIFont().also {
        it.apply(this)

        it.overrideParent = overrideParent
        it.fontRenderType = fontRenderType
        it.textAlignment = textAlignment
        it.fontStyle = fontStyle
        it.fontType = fontType
        it.fontColor = fontColor
        it.fontFamily = fontFamily
        it.fontSize = fontSize
        it.fontSpacing = fontSpacing
        it.lineBreakWidth = lineBreakWidth
        it.lineHeight = lineHeight
        it.appendedString = appendedString
        if (isOverridden)
            it.overwriteFontName(fontName)
    }

    protected inner class FontCache(var fontColor: Int, var fontSize: Float, var fontSpacing: Float, var lineBreakWidth: UIUnit?, var lineHeight: UIUnit?)
}