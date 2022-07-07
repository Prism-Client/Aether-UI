package net.prismclient.aether.ui.renderer.impl.border

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.interfaces.UIAnimatable

/**
 * [UIBorder] is a [UIShape] which renders a border to a component
 *
 * @author sen
 * @since 1.0
 */
class UIBorder : UIShape<UIBorder>(), UIAnimatable<UIBorder> {
    var borderWidth = 0f
    var borderColor: UIColor? = null
    var borderDirection: UIStrokeDirection = UIStrokeDirection.OUTSIDE

    fun render(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) {
        renderer {
            stroke(borderColor?.rgba ?: 0, borderWidth, borderDirection) {
                rect(x, y, width, height, radius)
            }
        }
    }

    @Deprecated("Use render(x, y, width, height, radius)", ReplaceWith("render"))
    override fun render() {
        throw RuntimeException("Should not be invoked")
    }

    override fun animate(animation: UIAnimation<*, *>, previous: UIBorder?, current: UIBorder?, progress: Float) {
        borderWidth = (previous?.borderWidth ?: 0f).lerp(current?.borderWidth ?: 0f, progress)
        borderColor?.rgba = previous?.borderColor.mix(current?.borderColor, progress)
        if (progress > 0.5f) {
            if (previous?.borderDirection != current?.borderDirection)
                borderDirection = current?.borderDirection ?: previous?.borderDirection ?: borderDirection
        }
    }

    override fun save(animation: UIAnimation<*, *>, keyframe: UIBorder?) {
        borderWidth = keyframe?.borderWidth ?: borderWidth
        borderColor = keyframe?.borderColor ?: borderColor
        borderDirection = keyframe?.borderDirection ?: borderDirection
    }

    override fun copy(): UIBorder = UIBorder().also {
        it.borderWidth = borderWidth
        it.borderColor = borderColor?.copy()
        it.borderDirection = borderDirection
    }

    override fun toString(): String =
        "UIBorder(borderWidth=$borderWidth, borderColor=$borderColor, borderDirection=$borderDirection)"
}