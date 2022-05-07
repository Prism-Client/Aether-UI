package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.UICore
import org.apache.commons.io.IOUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Returns a ByteBuffer from a file location using a resource stream
 */
fun String.toByteBuffer(): ByteBuffer {
    val bytes: ByteArray = IOUtils.toByteArray(UICore::class.java.getResourceAsStream(this))
    val buff = ByteBuffer.allocateDirect(bytes.size)
    buff.order(ByteOrder.nativeOrder())
    buff.put(bytes)
    buff.flip()
    return buff
}

fun String.nullableByteBuffer(): ByteBuffer? {
    try {
        return this.toByteBuffer()
    } catch (ignored: Exception) {}
    return null
}