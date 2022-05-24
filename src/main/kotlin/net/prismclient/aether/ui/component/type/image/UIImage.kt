package net.prismclient.aether.ui.component.type.image

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIImage]
 *
 * @author sen
 * @since 5/20/2022
 */
class UIImage(var name: String, val image: UIImageData, style: String) : UIComponent<UIImageSheet>(style) {
    /**
     * Loads an image from the specified file location
     */
    constructor(location: String, style: String) : this(location, location, style)

    /**
     * Loads am image or svg from the specified location with a given name
     */
    constructor(name: String, location: String, style: String) : this(
        name,
        UIRendererDSL.assumeLoadImage(name, location),
        style
    )

    override fun renderComponent() {
        renderer {
            renderImage(
                name, x, y, width, height,
                style.imageRadius?.topLeft ?: 0f,
                style.imageRadius?.topRight ?: 0f,
                style.imageRadius?.bottomRight ?: 0f,
                style.imageRadius?.bottomLeft ?: 0f,
            )
        }
    }


}