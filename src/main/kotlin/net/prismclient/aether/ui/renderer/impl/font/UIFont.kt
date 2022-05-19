package net.prismclient.aether.ui.renderer.impl.font

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.util.extensions.align
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.renderer
import kotlin.math.max

/**
 * [UIFont] is a renderer which renders a component's font.
 *
 * Note: Width, and height are ignored.
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIFont : UIShape() {
    /**
     * When true, the component will be ensured to be at
     * least the size of the font width and height
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
     * Specifies the font type e.g. Regular, Bold, Thin etc..
     *
     * @see FontType
     */
    var fontType = UIDefaults.instance.fontType
        set(value) {
            field = value; updateFontName()
        }
    var fontFamily = UIDefaults.instance.fontFamily
        set(value) {
            field = value; updateFontName()
        }
    var fontSize = UIDefaults.instance.fontSize
    var fontSpacing = UIDefaults.instance.fontSpacing
    var lineBreakWidth = 0f
        set(value) {
            field = value
            if (component != null && component!!.animation?.animating != true)
                component!!.update()
        }
    var lineHeight = 0f
    var lineCount = 0
    var appendedString: String? = null
    var stringWidth = 0f
    var stringHeight = 0f

    /**
     * If true, fontName will not update automatically. It will only update when [overwriteFontName] is called
     */
    var isOverridden = false
        private set

    fun align(alignment: UIAlignment) {
        x ?: run { x = px(0) }
        y ?: run { y = px(0) }
        align(alignment, x!!, y!!)
    }

    @Deprecated("Use render(text: String) instead", ReplaceWith("render(text: String)"))
    override fun render() {
        throw IllegalStateException("Use render(text: String) instead")
    }

    fun render(text: String) {
        renderer {
            font(this@UIFont)
            when (fontRenderType) {
                FontRenderType.NORMAL -> {
                    text.render(cachedX, cachedY)
                    stringWidth = text.width()
                    stringHeight = text.height()
                }
                FontRenderType.WRAP -> {
                    val c: Int = text.render(cachedX, cachedY, lineBreakWidth, lineHeight)
                    if (lineCount != c) {
                        component!!.update()
                        lineCount = c
                    }
                    stringWidth = getWrappedWidth()
                    stringHeight = getWrappedHeight()
                }
                FontRenderType.CLIP, FontRenderType.APPEND -> {
                    stringWidth = text.render(cachedX, cachedY, component!!.relX + lineBreakWidth, null, false)
                    stringHeight = text.height()
                }
            }

            // Updates the component to ensure that the width, and
            // height are at least the size of the text rendered
            if (overrideParent && (stringWidth > component!!.width || stringHeight > component!!.height)) {
                component!!.width = max(stringWidth, component!!.width)
                component!!.height = max(stringHeight, component!!.height)
                component!!.updatePosition()
                component!!.updateBounds()
                component!!.updateStyle()
            }
        }
    }

    /**
     * Returns the position of the ascender line in the current font
     */
    fun getAscend(): Float {
        UIRendererDSL.font(this)
        return UIRendererDSL.ascender()
    }

    /**
     * Returns the position of the descender line in the current font
     */
    fun getDescend(): Float {
        UIRendererDSL.font(this)
        return UIRendererDSL.descender()
    }

    /**
     * If the font was loaded with a special name, use this function to override the default formatting
     */
    fun overwriteFontName(name: String) {
        fontName = name
        isOverridden = true
    }

    /**
     * Updates [fontName] based on the [fontStyle], [fontType] and [fontFamily]
     */
    private fun updateFontName() {
        isOverridden = false
        fontName = fontFamily + "-" + fontType.name.lowercase() + if (fontStyle == FontStyle.Italic) "-italic" else ""
    }

    enum class FontStyle {
        Normal,
        Italic
    }

    enum class FontType {
        Regular,
        Black,
        Bold,
        Light,
        Thin
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

    override fun copy(): UIFont = UIFont().also {
        it.apply(this)

        it.overrideParent = overrideParent
        it.fontRenderType = fontRenderType
        it.textAlignment = textAlignment
        it.fontStyle = fontStyle
        it.fontColor = fontColor
        it.fontType = fontType
        it.fontFamily = fontFamily
        it.fontSize = fontSize
        it.fontSpacing = fontSpacing
        it.lineBreakWidth = lineBreakWidth
        it.lineHeight = lineHeight
        it.appendedString = appendedString

        if (isOverridden)
            it.overwriteFontName(fontName)
    }
}