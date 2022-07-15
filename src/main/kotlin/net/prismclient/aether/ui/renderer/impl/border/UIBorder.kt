package net.prismclient.aether.ui.renderer.impl.border

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.lerp
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.extensions.transition
import net.prismclient.aether.ui.util.interfaces.UIAnimatable

/**
 * [UIBorder] is a [UIShape] which renders a border to a component
 *
 * @author sen
 * @since 1.0
 */
class UIBorder : UIShape<UIBorder>(), UIAnimatable<UIBorder> {
    var borderWidth: UIUnit? = null
    var borderColor: UIColor? = null
    var borderDirection: UIStrokeDirection = UIStrokeDirection.OUTSIDE

    var cachedBorderWidth = 0f
    var isAnimating = false

    override fun update(component: UIComponent<*>?) {
        super.update(component)
        if (!isAnimating)
            cachedBorderWidth = component!!.computeUnit(borderWidth, false)
    }

    fun render(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) = renderer {
        stroke(cachedBorderWidth, borderColor?.rgba ?: 0, borderDirection) {
            rect(x, y, width, height, radius)
        }
    }

    @Deprecated("Use render(x, y, width, height, radius)", ReplaceWith("render"))
    override fun render() {
        throw RuntimeException("Should not be invoked")
    }

    override fun animate(animation: UIAnimation<*>, previous: UIBorder?, current: UIBorder?, progress: Float) {
        isAnimating = true
        cachedBorderWidth = previous?.borderWidth.lerp(current?.borderWidth, component!!, borderWidth, progress, false)
//        if (previous?.borderColor != null || current?.borderColor != null) {
//            borderColor = borderColor ?: UIColor(0)
//            borderColor!!.rgba = previous?.borderColor.mix(current?.borderColor, borderColor!!, progress)
//        }
        borderColor?.rgba = transition(
            previous?.borderColor?.rgba ?: borderColor!!.rgba,
            current?.borderColor?.rgba ?: borderColor!!.rgba,
            progress
        )
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIBorder?) {
        borderWidth = keyframe?.borderWidth?.copy() ?: borderWidth
        borderColor = keyframe?.borderColor?.copy() ?: borderColor
        borderDirection = keyframe?.borderDirection ?: borderDirection
        isAnimating = false
    }

    override fun copy(): UIBorder = UIBorder().also {
        it.borderWidth = borderWidth?.copy()
        it.borderColor = borderColor?.copy()
        it.borderDirection = borderDirection
    }

    override fun toString(): String =
        "UIBorder(borderWidth=$borderWidth, borderColor=$borderColor, borderDirection=$borderDirection)"
}