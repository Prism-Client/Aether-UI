package net.prismclient.aether.ui.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.util.GENERATE_MIPMAPS
import net.prismclient.aether.ui.util.REPEATX
import net.prismclient.aether.ui.util.REPEATY
import net.prismclient.aether.ui.util.extensions.toByteBuffer
import net.prismclient.aether.ui.util.extensions.toTerminatingByteBuffer
import net.prismclient.aether.ui.util.warn
import org.apache.commons.io.FilenameUtils
import java.nio.ByteBuffer

/**
 * [UIAssetDSL] is a DSL which specializes in creating bulk loading assets such as  images, and fonts.
 * is designed to support the formats SVG, PNG, JPEG, and TTFs by default. If a function expects a path
 * it is attempting to retrieve a resource from the classpath. If a resource in the class path is in say
 * "/images/background.png" then the file location should be as it was written there. Alternatively, if the
 * assets are loaded externally a file can be specified, or it can be loaded into a Byte Buffer, and then
 * passed to this class to load the asset.
 *
 * @author sen
 * @since 1.2
 */
object UIAssetDSL {
    /**
     * Loads an image from the classpath. The file extension is automatically figured out. Supports png, jpeg, and svg.
     */
    fun image(name: String, path: String, flags: Int = GENERATE_MIPMAPS or REPEATX or REPEATY) {
        when (FilenameUtils.getExtension(path)) {
            "png", "jpeg", "jpg" -> Aether.instance.renderer.createImage(name, path.toByteBuffer(), flags)
            "svg" -> Aether.instance.renderer.createSvg(name, path.toTerminatingByteBuffer(), Aether.devicePxRatio)
            else -> warn("Failed to load image $path, unsupported file extension")
        }
    }

    fun svg(name: String, path: String) {
        Aether.instance.renderer.createSvg(name, path.toTerminatingByteBuffer(), Aether.devicePxRatio)
    }

    fun font(name: String, path: String) = font(name, path.toTerminatingByteBuffer())

    fun font(name: String, buffer: ByteBuffer?) = Aether.instance.renderer.createFont(name, buffer)

    fun bulkLoad() {

    }
}