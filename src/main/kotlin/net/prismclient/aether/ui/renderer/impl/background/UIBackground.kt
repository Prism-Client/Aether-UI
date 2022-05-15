package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIBackground] represents the background properties of a UIComponent which can be rendered. It also has
 * derivatives, [UIGradientBackground] and [UIImageBackground] to support different backgrounds types.
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIBackground : UICopy<UIBackground> {
    var color = UIDefaults.instance.backgroundColor
    var border: UIBorder? = null
    var radius: UIRadius? = null


    open fun render(x: Float, y: Float, width: Float, height: Float) {
        renderer {
            color(color)
            rect(x, y, width, height, radius)
            border?.render(x, y, width, height, radius)
        }
    }

    inline fun border(block: UIBorder.() -> Unit) {
        border = UIBorder()
        border!!.block()
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.color = color
        it.radius = radius?.copy()
        it.border = border?.copy()
    }
}