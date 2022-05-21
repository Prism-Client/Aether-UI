package net.prismclient.aether.ui.renderer.impl.border

import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIBorder]
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIBorder : UICopy<UIBorder> {
    var borderWidth = 0f
    var borderColor = 0
    var borderDirection: UIRendererDSL.StrokeDirection = UIRendererDSL.StrokeDirection.OUTSIDE

    open fun render(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) {
        renderer {
            stroke(borderColor, borderWidth, borderDirection) {
                rect(x, y, width,height, radius)
            }
        }
    }

    override fun copy(): UIBorder = UIBorder().also {
        it.borderWidth = borderWidth
        it.borderColor = borderColor
        it.borderDirection = borderDirection
    }
}