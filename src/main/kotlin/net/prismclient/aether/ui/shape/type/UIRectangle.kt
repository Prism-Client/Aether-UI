package net.prismclient.aether.ui.shape.type

import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIColoredShape
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIRectangle] is an implementation of [UIShape] that draws a rectangle with the given [radius].
 *
 * @author sen
 * @since 1.0
 */
open class UIRectangle : UIColoredShape<UIRectangle>() {
    var radius: UIRadius? = null

    override fun render() {
        renderer {
            color(this@UIRectangle.color)
            rect(cachedX, cachedY, cachedWidth, cachedHeight, radius)
        }
    }

    override fun apply(shape: UIShape<UIRectangle>): UIRectangle {
        x = shape.x?.copy()
        y = shape.y?.copy()
        width = shape.width?.copy()
        height = shape.height?.copy()
        color = (shape as UIColoredShape).color?.copy()
        radius = (shape as UIRectangle).radius?.copy()
        return shape
    }

    override fun copy(): UIRectangle = UIRectangle().apply(this)
}