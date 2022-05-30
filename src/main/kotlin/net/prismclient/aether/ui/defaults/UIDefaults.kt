package net.prismclient.aether.ui.defaults

import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIRadius

/**
 * [UIDefaults] is designed to avoid lots of boilerplate  code when defining
 * styles. The properties set here will be the default properties of all the styles.
 * Not all properties are available for setting. Only generic classes such as [UIFont],
 * [UIBackground] and so on are applicable for changing the default values.
 *
 * To apply the default, create a new instance of [UIDefaults], and apply it to net.prismclient.aether.ui.UICore
 * <pre>
 *     val defaultStyle = UIDefaults()
 *
 *     /* Define properties here */
 *
 *     net.prismclient.aether.ui.UICore.instance.apply(defaultStyle)
 * </pre>
 *
 * @author sen
 * @since 1/5/2022
 */
class UIDefaults {
    var backgroundColor = -1
    var backgroundRadius = UIRadius()
    var backgroundBorder: UIBorder? = null

    var textAlignment = UIRenderer.ALIGNTOP or UIRenderer.ALIGNLEFT
    var fontColor = -1
    var fontName = ""
    var fontStyle = UIFont.FontStyle.Normal
    var fontType = UIFont.FontType.Regular
    var fontFamily = ""
    var fontSize = 0f
    var fontSpacing = 0f

    companion object {
        var instance = UIDefaults()
    }
}