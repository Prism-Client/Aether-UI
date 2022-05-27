package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.util.interfaces.UICopy
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.extensions.transition

/**
 * [UIBackground] represents the background properties of a UIComponent which can be rendered. It also has
 * derivatives, [UIGradientBackground] and [UIImageBackground] to support different backgrounds types.
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIBackground : UICopy<UIBackground> {
    var color = UIDefaults.instance.backgroundColor
    var radius: UIRadius? = null
    var border: UIBorder? = null

    open fun render(x: Float, y: Float, width: Float, height: Float) {
        renderer {
            color(color)
            rect(x, y, width, height, radius)
            border?.render(x, y, width, height, radius)
        }
    }

    inline fun border(block: UIBorder.() -> Unit) {
        border = border ?: UIBorder()
        border!!.block()
    }

    /**
     * A cached instance of this before an animation is started
     */
    protected var cachedBackground: UIBackground? = null

    /**
     * Necessary for animations. Given two background classes, and a progress
     * the values must be calculated and then applied to this class for the
     * animation to work properly.
     *
     * The given [previousBackground] and [activeBackground] can assume to be the
     * same type of [UIBackground] as this.
     *
     * If the given [previousBackground] or [activeBackground] are null, then the
     * value should be the cached value
     */
    open fun animate(previousBackground: UIBackground?, activeBackground: UIBackground?, progress: Float) {
        cachedBackground = cachedBackground ?: this.copy()

        color = transition(
            previousBackground?.color ?: cachedBackground!!.color,
            activeBackground?.color ?: cachedBackground!!.color,
            progress
        )

        radius
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.color = color
        it.radius = radius?.copy()
        it.border = border?.copy()
    }
}