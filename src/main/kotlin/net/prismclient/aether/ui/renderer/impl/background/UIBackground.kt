package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.util.interfaces.UICopy
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.extensions.transition
import net.prismclient.aether.ui.util.interfaces.UIAnimatable

/**
 * [UIBackground] represents the background properties of a UIComponent which can be rendered. It also has
 * derivatives, [UIGradientBackground] and [UIImageBackground] to support different backgrounds types.
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIBackground : UICopy<UIBackground>, UIAnimatable<UIBackground> {
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

    protected var cachedColor: Int? = null

    override fun updateAnimationCache(component: UIComponent<*>) {
        border?.updateAnimationCache(component)
        radius?.updateAnimationCache(component)
    }

    override fun clearAnimationCache() {
        cachedColor = null
        radius?.clearAnimationCache()
        border?.clearAnimationCache()
    }

    override fun animate(previous: UIBackground?, current: UIBackground?, progress: Float, component: UIComponent<*>) {
        cachedColor = cachedColor ?: color

        if (previous?.radius != null || current?.radius != null)
            radius = radius ?: UIRadius()
        if (previous?.border != null || current?.border != null)
            border = border ?: UIBorder()

        color = transition(
            previous?.color ?: cachedColor!!,
            current?.color ?: cachedColor!!,
            progress
        )
        radius?.animate(previous?.radius, current?.radius, progress, component)
        border?.animate(previous?.border, current?.border, progress, component)
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIBackground?, retain: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.color = color
        it.radius = radius?.copy()
        it.border = border?.copy()
    }
}