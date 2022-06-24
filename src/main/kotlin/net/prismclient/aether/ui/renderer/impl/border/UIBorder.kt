package net.prismclient.aether.ui.renderer.impl.border

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.util.extensions.fromProgress
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.extensions.transition
import net.prismclient.aether.ui.util.interfaces.UIAnimatable

/**
 * [UIBorder] is a [UIShape] which renders a border to a component
 *
 * @author sen
 * @since 4/26/2022
 */
class UIBorder : UIShape(), UIAnimatable<UIBorder> {
    var borderWidth = 0f
    var borderColor = 0
    var borderDirection: UIStrokeDirection = UIStrokeDirection.OUTSIDE

    fun render(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) {
        renderer {
            stroke(borderColor, borderWidth, borderDirection) {
                rect(x, y, width, height, radius)
            }
        }
    }

    @Deprecated("Use render(x, y, width, height, radius)", ReplaceWith("render"))
    override fun render() {
        throw RuntimeException("Should not be invoked")
    }

    override fun copy(): UIBorder = UIBorder().also {
        it.borderWidth = borderWidth
        it.borderColor = borderColor
        it.borderDirection = borderDirection
    }

    private var cachedBorder: UIBorder? = null

    override fun updateAnimationCache(component: UIComponent<*>) {}

    override fun clearAnimationCache() {
        cachedBorder = null
    }

    override fun animate(previous: UIBorder?, current: UIBorder?, progress: Float, component: UIComponent<*>) {
        cachedBorder = cachedBorder ?: copy()

        borderWidth = fromProgress(previous?.borderWidth ?: cachedBorder!!.borderWidth, current?.borderWidth ?: cachedBorder!!.borderWidth, progress)
        borderColor = transition(previous?.borderColor ?: cachedBorder!!.borderColor, current?.borderColor ?: cachedBorder!!.borderColor, progress)
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIBorder?, retain: Boolean) {
        TODO("Not yet implemented")
    }

    override fun toString(): String =
        "UIBorder(borderWidth=$borderWidth, borderColor=$borderColor, borderDirection=$borderDirection)"
}