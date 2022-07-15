package net.prismclient.aether.ui.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.safeByteBuffer
import net.prismclient.aether.ui.util.extensions.toByteBuffer
import org.apache.commons.io.FilenameUtils
import java.io.File
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

    private var DEFAULT_IMAGE_FLAGS = PREMULTIPLIED or GENERATE_MIPMAPS or REPEATX or REPEATY

    /**
     * Loads an image from the classpath. Supports general images formats such as PNG, JPEG, etc...
     *
     * @see svg
     */
    @JvmStatic
    fun image(name: String, path: String, flags: Int = DEFAULT_IMAGE_FLAGS) =
        image(name, path.toByteBuffer(), flags)

    /**
     * Loads an image from the given [buffer]. The image is registered with the [flags] and is named [name].
     *
     * @see svg
     */
    @JvmStatic
    fun image(name: String, buffer: ByteBuffer?, flags: Int) {
        if (buffer == null) {
            warn("Failed to load image name, as the buffer was null.")
            return
        }
        Aether.renderer.createImage(name, buffer, flags)
    }

    @JvmStatic
    @JvmOverloads
    fun svg(name: String, path: String, scale: Float = Aether.devicePxRatio) = svg(name, path.safeByteBuffer(), scale)

    @JvmOverloads
    fun svg(name: String, byteBuffer: ByteBuffer?, scale: Float = Aether.devicePxRatio) {
        if (byteBuffer == null) {
            warn("Failed to load svg $name, as the buffer was null.")
            return
        }

        Aether.renderer.createSvg(name, byteBuffer, scale)
    }

    @JvmStatic
    fun font(name: String, path: String) = font(name, path.toByteBuffer())

    @JvmStatic
    fun font(name: String, buffer: ByteBuffer?) {
        if (buffer == null) {
            warn("Failed to load font $name, as the buffer was null.")
            return
        }
        Aether.renderer.createFont(name, buffer)
    }

    // TODO: Append stuff to bulk load and stuff

    /**
     * Attempts to load a directory of images and SVGs into memory. TTF files are not loaded. When the images
     * are loaded into memory, they are formatted as
     *
     *      [appendedString][directory/][fileName]
     *
     * For example if the appended string is blank, the file is a svg in the directory called gradient, then
     *
     *      gradient/setting
     *
     * This can be especially useful if there is multiple of the same file name in different directory's:
     *
     *      solid/setting
     *      outline/setting
     *      gradient/setting
     *
     * The appended string appends at the front prior to everything else.
     *
     * @param deep When true, subdirectories will also be loaded.
     * @param imageFlags The flags of the image if the file is an image
     * @param svgScale The scale of the SVG if the file is an SVG.
     * @return The number of files loaded.
     * @see bulkLoad
     */
    @JvmOverloads
    fun bulkLoad(
        folderLocation: String,
        deep: Boolean = true,
        appendedString: String = "",
        imageFlags: Int = DEFAULT_IMAGE_FLAGS,
        svgScale: Float = Aether.devicePxRatio
    ): Int {
        val file = Aether.javaClass.getResource(folderLocation) ?: run {
            warn("Failed to bulk load [$folderLocation] as the file was null.")
            return 0
        }

        return internalBulkLoad(File(file.toURI()), deep, appendedString, imageFlags, svgScale).also {
            inform("Bulk loaded $it files.")
        }
    }

    /**
     * An internal version of [bulkLoad] which expects a file instead of a resource location.
     */
    @JvmStatic
    @JvmOverloads
    fun internalBulkLoad(
        fileLocation: File,
        deep: Boolean,
        appendedString: String,
        imageFlags: Int = DEFAULT_IMAGE_FLAGS,
        svgScale: Float = Aether.devicePxRatio,
        first: Boolean = true
    ): Int {
        var loc = "${fileLocation.name}/"
        if (first)
            loc = appendedString + loc
        var count = 0
        for (file in fileLocation.listFiles()!!) {
            val fileExtension = FilenameUtils.getExtension(file.name).lowercase()
            if (file.isDirectory && deep) {
                count += internalBulkLoad(file, true, loc, imageFlags, svgScale, false)
            } else {
                val name = loc + FilenameUtils.removeExtension(file.name)

                when (fileExtension) {
                    "png", "jpeg", "jpg" -> image(name, file.inputStream().safeByteBuffer(), imageFlags)
                    "svg" -> svg(name, file.inputStream().safeByteBuffer(), svgScale)
                    else -> {
                        warn("Unsupported file type: ${file.name}")
                        continue
                    }
                }
                count++
            }
        }
        return count
    }
}