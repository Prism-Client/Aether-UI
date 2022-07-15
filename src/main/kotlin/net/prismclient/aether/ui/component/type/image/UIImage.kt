package net.prismclient.aether.ui.component.type.image

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIImage] is the default implementation for rendering images on screen. It accepts`
 * an image
 *
 * @author sen
 * @since 5/20/2022
 */
class UIImage(name: String, style: String?) : UIComponent<UIImageSheet>(style) {
    var image: String = name
        set(value) {
            field = value
            activeImage = UIProvider.getImage(image)
        }
    var activeImage: UIImageData? = null

    /**
     * Loads am image or svg from the specified location with a given name
     */
    constructor(name: String, location: String, style: String?) : this(
        name,
        style
    ) {
        UIAssetDSL.image(name, location)
    }

    init {
        activeImage = UIProvider.getImage(image)
    }

    override fun renderComponent() {
        renderer {
            color(style.imageColor)
            renderImage(
                image, x, y, width, height,
                style.imageRadius?.topLeft ?: 0f,
                style.imageRadius?.topRight ?: 0f,
                style.imageRadius?.bottomRight ?: 0f,
                style.imageRadius?.bottomLeft ?: 0f,
            )
        }
    }
}