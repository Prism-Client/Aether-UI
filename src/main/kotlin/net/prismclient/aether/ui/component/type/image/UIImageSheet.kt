package net.prismclient.aether.ui.component.type.image

import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIImageSheet] is the sheet implementation for [UIImage].
 *
 * @author sen
 * @since 5/25/2022
 */
class UIImageSheet(name: String) : UIStyleSheet(name) {
    /**
     * The color of the image. Use -1 (WHITE) for the normal color.
     */
    var imageColor = -1

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