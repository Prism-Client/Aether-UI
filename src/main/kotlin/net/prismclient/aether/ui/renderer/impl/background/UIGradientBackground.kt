package net.prismclient.aether.ui.renderer.impl.background

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
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

    override fun update(component: UIComponent<*>) {
        super.update(component)
        gradientXCache = component.relX + component.calculateUnitX(gradientX, component.relWidth, false)
        gradientYCache = component.relY + component.calculateUnitY(gradientY, component.relHeight, false)
        gradientWidthCache = component.calculateUnitX(gradientWidth, component.relWidth, false)
        gradientHeightCache = component.calculateUnitY(gradientHeight, component.relHeight, false)
    }

    override fun render() {
        renderer {
            renderer.linearGradient(
                cachedX,
                cachedY,
                cachedWidth,
                cachedHeight,
                radius?.topLeft ?: 0f,
                radius?.topRight ?: 0f,
                radius?.bottomRight ?: 0f,
                radius?.bottomLeft ?: 0f,
                gradientXCache,
                gradientYCache,
                gradientWidthCache,
                gradientHeightCache,
                gradientStartColor,
                gradientEndColor
            )
        }
    }

    enum class GradientType {
        LINEAR,
        RADIAL
    }
}