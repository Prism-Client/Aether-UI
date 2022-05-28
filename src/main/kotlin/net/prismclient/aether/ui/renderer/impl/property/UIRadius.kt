package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.util.extensions.fromProgress
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIRadius] represents a shape with 4 customizable corner radii
 *
 * @author sen
 * @since 4/26/2022
 */
class UIRadius(
    var topLeft: Float = 0f,
    var topRight: Float = 0f,
    var bottomRight: Float = 0f,
    var bottomLeft: Float = 0f
) : UICopy<UIRadius>, UIAnimatable<UIRadius> {
    constructor(radius: Float) : this(radius, radius, radius, radius)

    fun set(radius: Float) {
        topLeft = radius
        topRight = radius
        bottomRight = radius
        bottomLeft = radius
    }

    override fun copy(): UIRadius = UIRadius(topLeft, topRight, bottomRight, bottomLeft)

    private var cachedRadius: UIRadius? = null

    override fun updateAnimationCache(component: UIComponent<*>) {}

    override fun clearAnimationCache() {
        cachedRadius = null
    }

    override fun animate(previous: UIRadius?, current: UIRadius?, progress: Float, component: UIComponent<*>) {
        cachedRadius = cachedRadius ?: this.copy()
        
        topLeft = fromProgress(previous?.topLeft ?: cachedRadius!!.topLeft, current?.topLeft ?: cachedRadius!!.topLeft, progress)
        topRight = fromProgress(previous?.topRight ?: cachedRadius!!.topRight, current?.topRight ?: cachedRadius!!.topRight, progress)
        bottomRight = fromProgress(previous?.bottomRight ?: cachedRadius!!.bottomRight, current?.bottomRight ?: cachedRadius!!.bottomRight, progress)
        bottomLeft = fromProgress(previous?.bottomLeft ?: cachedRadius!!.bottomLeft, current?.bottomLeft ?: cachedRadius!!.bottomLeft, progress)
    }
}