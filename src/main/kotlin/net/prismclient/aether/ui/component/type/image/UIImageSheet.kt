package net.prismclient.aether.ui.component.type.image

import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.UIColor

/**
 * [UIImageSheet] is the sheet implementation for [UIImage].
 *
 * @author sen
 * @since 1.0
 */
class UIImageSheet(name: String) : UIStyleSheet(name) {
    /**
     * The color of the image. The default value is RGBA(255, 255, 255)
     */
    var imageColor: UIColor? = null

    /**
     * The radius of the image.
     */
    var imageRadius: UIRadius? = null

    override fun copy() = UIImageSheet(name).also {
        it.apply(this)
        it.imageColor = imageColor
        it.imageRadius = imageRadius?.copy()
    }
}