package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.calculate
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIGradientBackground] is the background renderer for a component which requests a gradient instead of a solid
 *
 * @author sen
 * @since 4/26/2022
 */
class UIGradientBackground : UIBackground() {
    var gradientStartColor = 0
    var gradientEndColor = 0
    var gradientX: UIUnit? = null
    var gradientY: UIUnit? = null
    var gradientWidth: UIUnit? = null
    var gradientHeight: UIUnit? = null

    var gradientXCache = 0f
        private set
    var gradientYCache = 0f
        private set
    var gradientWidthCache = 0f
        private set
    var gradientHeightCache = 0f
        private set

    override fun update(component: UIComponent<*>?) {
        super.update(component)
        gradientXCache = (component?.relX ?: 0f) + calculate(gradientX, component, component?.relWidth ?: 0f, component?.relHeight ?: 0f, false)
        gradientYCache = (component?.relY ?: 0f) + calculate(gradientY, component, component?.relWidth ?: 0f, component?.relHeight ?: 0f, true)
        gradientWidthCache = calculate(gradientWidth, component, component?.relWidth ?: 0f, component?.relHeight ?: 0f, false)
        gradientHeightCache = calculate(gradientHeight, component, component?.relWidth ?: 0f, component?.relHeight ?: 0f, true)
    }

    override fun render() {
        renderer {
            TODO("Gradient suport")
        }
    }

    override fun copy(): UIGradientBackground = UIGradientBackground().also {
        it.backgroundColor = backgroundColor
        it.radius = radius?.copy()
        it.border = border?.copy()
        it.gradientStartColor = gradientStartColor
        it.gradientEndColor = gradientEndColor
        it.gradientX = gradientX?.copy()
        it.gradientY = gradientY?.copy()
        it.gradientWidth = gradientWidth?.copy()
        it.gradientHeight = gradientHeight?.copy()
    }

    // TODO: GradientType
//    enum class GradientType {
//        LINEAR,
//        RADIAL
//    }
}