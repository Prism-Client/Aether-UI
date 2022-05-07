package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.component.UIComponent
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
open class UIBackground() : UICopy<UIBackground> {
    var color = UIDefaults.instance.fontColor
    var radius: UIRadius = UIDefaults.instance.backgroundRadius.copy()
    var border: UIBorder? = UIDefaults.instance.backgroundBorder?.copy()

    open fun render(x: Float, y: Float, width: Float, height: Float) {
        renderer {
            color(color)
            rect(
                x,
                y,
                width,
                height,
                radius.topLeft,
                radius.topRight,
                radius.bottomRight,
                radius.bottomLeft
            )
            if (border != null) {
                outline(border!!.borderWidth, border!!.borderColor) {
                    rect(
                        x,
                        y,
                        width,
                        height,
                        radius.topLeft,
                        radius.topRight,
                        radius.bottomRight,
                        radius.bottomLeft
                    )
                }
            }
        }
    }

    fun border(block: UIBorder.() -> Unit) {
        border = UIBorder()
        border!!.block()
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.color = color
        it.radius = radius.copy()
        it.border = border?.copy()
    }
}