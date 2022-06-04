package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.util.extensions.getAlpha
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
    var backgroundColor = UIDefaults.instance.backgroundColor
    var radius: UIRadius? = null
    var border: UIBorder? = null

    open fun render(x: Float, y: Float, width: Float, height: Float) {
        renderer {
            color(backgroundColor)
            rect(x, y, width, height, radius)
            border?.render(x, y, width, height, radius)
        }
    }

    inline fun border(block: UIBorder.() -> Unit) {
        border = border ?: UIBorder()
        border!!.block()
    }

    fun radius(radius: Float) {
        this.radius = UIRadius(radius)
    }

    protected var cachedColor: Int? = null

    override fun updateAnimationCache(component: UIComponent<*>) {
        border?.updateAnimationCache(component)
        radius?.updateAnimationCache(component)
    }

    override fun clearAnimationCache() {
        cachedColor = null
//        radius?.clearAnimationCache()
//        border?.clearAnimationCache()
    }

    override fun animate(previous: UIBackground?, current: UIBackground?, progress: Float, component: UIComponent<*>) {
        cachedColor = cachedColor ?: backgroundColor

//        if (previous?.radius != null || current?.radius != null)
//            radius = radius ?: UIRadius()
//        if (previous?.border != null || current?.border != null)
//            border = border ?: UIBorder()

        backgroundColor = transition(
            previous?.backgroundColor ?: cachedColor!!,
            current?.backgroundColor ?: cachedColor!!,
            progress
        )
//        println("Progress: $progress, Alpha: ${backgroundColor.getAlpha()}")
        radius?.animate(previous?.radius, current?.radius, progress, component)
        border?.animate(previous?.border, current?.border, progress, component)
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIBackground?, retain: Boolean) {
//        if (!retain) {
//            backgroundColor = cachedColor!!
//        }
//        radius?.saveState(component, keyframe?.radius, retain)
//        border?.saveState(component, keyframe?.border, retain)
        //TODO("Not yet implemented")
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.backgroundColor = backgroundColor
        it.radius = radius?.copy()
        it.border = border?.copy()
    }
}