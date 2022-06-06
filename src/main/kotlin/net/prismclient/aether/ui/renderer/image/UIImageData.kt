package net.prismclient.aether.ui.renderer.image

import java.nio.ByteBuffer

/**
 * [UIImageData] holds data pertaining to an image
 *
 * @author sen
 * @since 5/20/2022
 */
class UIImageData {
    var handle: Int = -1
    var width: Int = -1
    var height: Int = -1
    var buffer: ByteBuffer? = null
    var imageType: ImageType = ImageType.Image
    var loaded: Boolean = false

    /**
     * [ImageType] describes what type of image this is.
     */
    enum class ImageType {
        Image, Svg, Other
    }
}