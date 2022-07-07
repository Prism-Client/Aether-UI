package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.util.extensions.fromProgress
import net.prismclient.aether.ui.util.extensions.lerp
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIRadius] represents a shape with 4 customizable corner radii
 *
 * @author sen
 * @since 1.0
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

    override fun animate(animation: UIAnimation<*>, previous: UIRadius?, current: UIRadius?, progress: Float) {
        topLeft = lerp(previous?.topLeft ?: topLeft, current?.topLeft ?: topLeft, progress)
        topRight = lerp(previous?.topRight ?: topRight, current?.topRight ?: topRight, progress)
        bottomRight = lerp(previous?.bottomRight ?: bottomRight, current?.bottomRight ?: bottomRight, progress)
        bottomLeft = lerp(previous?.bottomLeft ?: bottomLeft, current?.bottomLeft ?: bottomLeft, progress)
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIRadius?) {
        topLeft = keyframe?.topLeft ?: topLeft
        topRight = keyframe?.topRight ?: topRight
        bottomRight = keyframe?.bottomRight ?: bottomRight
        bottomLeft = keyframe?.bottomLeft ?: bottomLeft
    }

    override fun toString(): String {
        return "UIRadius(topLeft=$topLeft, topRight=$topRight, bottomRight=$bottomRight, bottomLeft=$bottomLeft)"
    }
}