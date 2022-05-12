package net.prismclient.aether.ui.renderer.image

import net.prismclient.aether.ui.renderer.builder.UIRendererDSL
import net.prismclient.aether.ui.util.extensions.toByteBuffer
import java.nio.ByteBuffer

/**
 * [UIImage] holds an image name, and a corresponding bytebuffer to the data
 *
 * @author sen
 * @since 12/5/2022
 */
class UIImage(val imageName: String, val imageData: ByteBuffer, flags: Int = 0) {
    constructor(imageName: String, imageLocation: String, flags: Int = 0) : this(imageName, imageLocation.toByteBuffer(), flags)
    constructor(imageName: String, flags: Int = 0) : this(imageName, "/images/$imageName.png".toByteBuffer(), flags)

    var loaded: Boolean = false
        private set

    init {
        UIRendererDSL.instance.loadImage(imageName, imageData, flags)
    }
}