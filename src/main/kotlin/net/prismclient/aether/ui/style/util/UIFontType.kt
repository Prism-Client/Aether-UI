package net.prismclient.aether.ui.style.util

import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.util.extensions.safeByteBuffer
import java.nio.ByteBuffer

/**
 * [UIFontType] holds a Normal, and italic version of a font
 *
 * @author sen
 * @since 4/22/2022
 */
class UIFontType(val normal: String = "", val italic: String = "") {
    private var normalBuffer: ByteBuffer? = null
    private var italicBuffer: ByteBuffer? = null

    var loaded = false
    var hasItalic = false

    fun attemptLoad() = load(normalBuffer, italicBuffer)

    fun load(fileLocation: String) {
        load(
            if (normal.isNotEmpty()) ("$fileLocation.ttf").safeByteBuffer() else null,
            if (italic.isNotEmpty()) ("$fileLocation-italic.ttf").safeByteBuffer() else null
        )
    }

    fun load(normalBuffer: ByteBuffer?, italicBuffer: ByteBuffer?) {
        this.normalBuffer = normalBuffer
        this.italicBuffer = italicBuffer

        if (normalBuffer != null) {
            UIAssetDSL.font(normal, normalBuffer)
        }
        if (italicBuffer != null) {
            UIAssetDSL.font(italic, italicBuffer)
            hasItalic = true
        }
        if (normalBuffer != null || italicBuffer != null) loaded = true
    }
}