package net.prismclient.aether.ui.renderer.impl.background

/**
 * [UIGradientBackground] is the background renderer for a component which requests a gradient instead of a solid
 *
 * @author sen
 * @since 4/26/2022
 */
class UIGradientBackground : UIBackground() {
    var color1 = 0
    var color2 = 0
    var gradientX = 0f
    var gradientY = 0f
    var gradientWidth = 0f
    var gradientHeight = 0f

    override fun render(x: Float, y: Float, width: Float, height: Float) {
        // TODO Redesign Units, with parent positioning and multiple operations
        TODO("Gradient Backgrounds have not yet been implemented")
    }

    enum class GradientType {
        LINEAR,
        RADIAL
    }
}