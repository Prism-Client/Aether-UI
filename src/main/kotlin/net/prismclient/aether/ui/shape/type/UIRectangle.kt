package net.prismclient.aether.ui.shape.type

import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIRectangle]
 *
 * @author sen
 * @since 5/13/2022
 */
class UIRectangle : UIShape() {
    var radius: UIRadius? = null

    override fun render() {
        renderer {
            color(color)
            rect(cachedX + offsetX, cachedY + offsetY, cachedWidth, cachedHeight, radius)
        }
    }

    override fun copy(): UIShape =
        UIRectangle().also {
            it.radius = radius?.copy()
        }.apply(this)
}