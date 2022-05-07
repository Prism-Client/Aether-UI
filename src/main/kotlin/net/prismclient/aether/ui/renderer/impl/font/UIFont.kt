package net.prismclient.aether.ui.renderer.impl.font

import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.builder.UIRendererDSL
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIFont] is a renderer which renders a component's font.
 *
 * @author sen
 * @since 4/26/2022
 */
class UIFont : UICopy<UIFont> {
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
     * Specifies the font type e.g. Regular, Bold, Thin etc...
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

    var x: UIUnit = px(0)
    var y: UIUnit = px(0)

    /**
     * The calculated X position of the component, which is updated when [update] is called
     */
    var calculatedX = 0f
        private set
    /**
     * The calculated Y position of the component, which is updated when [update] is called
     */
    var calculatedY = 0f
        private set

    /**
     * If true, fontName will not update automatically. It will only update when [overwriteFontName] is called
     */
    var isOverridden = false
        private set

    fun align(alignment: UIAlignment) =
        net.prismclient.aether.ui.util.extensions.align(alignment, x, y)

    fun update(x: Float, y: Float) {
        calculatedX = x
        calculatedY = y
    }

    fun render(text: String) {
        renderer {
            font(this@UIFont)
            text.render(calculatedX, calculatedY)
        }
    }

    /**
     * Returns the position of the ascender line in the current font
     */
    fun getAscend(): Float {
        UIRendererDSL.instance.font(this)
        return UIRendererDSL.instance.ascender()
    }

    /**
     * Returns the position of the descender line in the current font
     */
    fun getDescend(): Float {
        UIRendererDSL.instance.font(this)
        return UIRendererDSL.instance.descender()
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

    override fun copy(): UIFont = UIFont().also {
        it.textAlignment = textAlignment
        it.fontStyle = fontStyle
        it.fontColor = fontColor
        it.fontType = fontType
        it.fontFamily = fontFamily
        it.fontSize = fontSize
        it.fontSpacing = fontSpacing
        it.x = x
        it.y = y

        if (isOverridden) {
            it.overwriteFontName(fontName)
        }
    }
}