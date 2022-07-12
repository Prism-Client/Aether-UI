package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.border.UIStrokeDirection
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.calculate
import net.prismclient.aether.ui.util.extensions.mix
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIAnimatable

/**
 * [UIBackground] represents the background properties of a UIComponent which can be rendered. It also has
 * derivatives, [UIGradientBackground] and [UIImageBackground] to support different backgrounds types.
 *
 * @author sen
 * @since 1.0
 */
open class UIBackground : UIShape<UIBackground>(), UIAnimatable<UIBackground> {
    var backgroundColor: UIColor? = null
    var radius: UIRadius? = null
    var border: UIBorder? = null

    override fun update(component: UIComponent<*>?) {
        // Reflect the rel units instead of the inner values
        this.component = component
        cachedX = (component?.relX ?: 0f) + calculate(
            x,
            component,
            component?.relWidth ?: 0f,
            component?.relHeight ?: 0f,
            false
        )
        cachedY = (component?.relY ?: 0f) + calculate(
            y,
            component,
            component?.relWidth ?: 0f,
            component?.relHeight ?: 0f,
            true
        )
        cachedWidth = calculate(width, component, component?.relWidth ?: 0f, component?.relHeight ?: 0f, false)
        cachedHeight = calculate(height, component, component?.relWidth ?: 0f, component?.relHeight ?: 0f, true)
        border?.update(component)
    }

    override fun render() {
        renderer {
            color(backgroundColor)
            rect(cachedX, cachedY, cachedWidth, cachedHeight, radius)
            border?.render(cachedX, cachedY, cachedWidth, cachedHeight, radius)
        }
    }

    inline fun border(block: UIBorder.() -> Unit) {
        border = border ?: UIBorder()
        border!!.block()
    }

    @JvmOverloads
    inline fun border(
        borderColor: UIColor? = null,
        borderWidth: UIUnit? = px(1),
        strokeDirection: UIStrokeDirection = UIStrokeDirection.OUTSIDE,
        block: UIBorder.() -> Unit = {}
    ) {
        border {
            this.borderColor = borderColor
            this.borderWidth = borderWidth
            this.borderDirection = strokeDirection
            block()
        }
    }

    fun radius(radius: Float) {
        this.radius = UIRadius(radius)
    }

    override fun animate(
        animation: UIAnimation<*>,
        previous: UIBackground?,
        current: UIBackground?,
        progress: Float
    ) {
        if (previous?.backgroundColor != null || current?.backgroundColor != null) {
            backgroundColor = backgroundColor ?: UIColor(0)
            backgroundColor!!.rgba =
                previous?.backgroundColor.mix(current?.backgroundColor, backgroundColor!!, progress)
        }
        if (previous?.radius != null || current?.radius != null) {
            radius = radius ?: UIRadius()
            radius!!.animate(animation, previous?.radius, current?.radius, progress)
        }
        if (previous?.border != null || current?.border != null) {
            border = border ?: UIBorder()
            border!!.animate(animation, previous?.border, current?.border, progress)
        }
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIBackground?) {
        backgroundColor = keyframe?.backgroundColor ?: backgroundColor?.copy()
        radius = keyframe?.radius ?: radius?.copy()
        border?.save(animation, keyframe?.border)
    }

    override fun copy(): UIBackground = UIBackground().also {
        it.backgroundColor = backgroundColor?.copy()
        it.radius = radius?.copy()
        it.border = border?.copy()
    }

    override fun toString(): String = "UIBackground(backgroundColor=$backgroundColor, radius=$radius, border=$border)"
}