package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.mix

/**
 * Exactly like [UIShape], except with a [UIColor] property.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIColoredShape<T : UIColoredShape<T>> : UIShape<T>() {
    var color: UIColor? = null

    override fun animate(animation: UIAnimation<*>, previous: T?, current: T?, progress: Float) {
        super.animate(animation, previous, current, progress)
        color?.rgba = previous?.color.mix(current?.color, progress)
    }

    override fun save(animation: UIAnimation<*>, keyframe: T?) {
        super.save(animation, keyframe)
        color = keyframe?.color ?: color?.copy()
    }

    override fun apply(shape: UIShape<T>): UIColoredShape<T> {
        x = shape.x?.copy()
        y = shape.y?.copy()
        width = shape.width?.copy()
        height = shape.height?.copy()
        color = (shape as? UIColoredShape)?.color?.copy() ?: color
        return this
    }
}