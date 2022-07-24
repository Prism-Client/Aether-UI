package net.prismclient.aether.ui.component.type.image

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.mix
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.name

/**
 * [UIImage] is the default component for rendering images to a screen. It accepts
 * the [name] of the image that is to be rendered onto the screen. Alternatively, the
 * image can also be loaded with the alternative constructor from a resource file.
 *
 * @author sen
 * @since 1.0
 */
class UIImage(name: String) : UIComponent<UIImageSheet>() {
    var image: String = name
        set(value) {
            field = value
            activeImage = UIProvider.getImage(image)
        }
    var activeImage: UIImageData? = null

    /**
     * Loads am image or svg from the specified location with a given name
     */
    constructor(name: String, location: String) : this(
        name
    ) { UIAssetDSL.image(name, location) }

    init {
        activeImage = UIProvider.getImage(image)
    }

    override fun renderComponent() {
        renderer {
            color(style.imageColor?.rgba ?: -1)
            renderImage(
                image, x, y, width, height,
                style.imageRadius?.topLeft ?: 0f,
                style.imageRadius?.topRight ?: 0f,
                style.imageRadius?.bottomRight ?: 0f,
                style.imageRadius?.bottomLeft ?: 0f,
            )
        }
    }

    override fun createsStyle(): UIImageSheet = UIImageSheet()
}

class UIImageSheet : UIStyleSheet() {
    /**
     * The color of the image. The default value is RGBA(255, 255, 255)
     */
    var imageColor: UIColor? = null

    /**
     * The radius of the image.
     */
    var imageRadius: UIRadius? = null

    override fun animate(animation: UIAnimation<*>, previous: UIStyleSheet?, current: UIStyleSheet?, progress: Float) {
        super.animate(animation, previous, current, progress)
        val p = previous as UIImageSheet
        val c = current as UIImageSheet
        if (p.imageColor != null || c.imageColor != null) {
            imageColor = imageColor ?: UIColor(0)
            imageColor!!.rgba = p.imageColor.mix(c.imageColor, imageColor!!, progress)
        }
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIStyleSheet?) {
        super.save(animation, keyframe)
    }

    override fun copy() = UIImageSheet().name(name).also {
        it.apply(this)
        it.imageColor = imageColor
        it.imageRadius = imageRadius?.copy()
    }
}