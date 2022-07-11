package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.util.warn
import org.apache.commons.io.IOUtils
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Returns a [ByteBuffer] from the given input stream. An exception is thrown if the stream is not readable.
 */
fun InputStream.byteBuffer(): ByteBuffer {
    val bytes: ByteArray = IOUtils.toByteArray(this)
    val buff = ByteBuffer.allocateDirect(bytes.size + 1)
    buff.order(ByteOrder.nativeOrder())
    buff.put(bytes)
    buff.put(0)
    buff.flip()
    return buff
}

/**
 * Returns a [ByteBuffer] from the given input stream. If it fails to read the stream, it will return null.
 */
fun InputStream.safeByteBuffer(): ByteBuffer? {
    return try {
        this.byteBuffer()
    } catch (e: Exception) {
        warn("Failed to read steam.")
        null
    }
}

/**
 * Returns a ByteBuffer from the given class path file location. An exception is
 * thrown if the file is not found.
 */
fun String.toByteBuffer(): ByteBuffer =
    (Aether.javaClass.getResourceAsStream(this)
        ?: throw FileNotFoundException("[$this] was not found within the classpath.")).byteBuffer()

/**
 * Returns a ByteBuffer from the given class path location. If it fails to read the file, it will return null.
 */
fun String.safeByteBuffer(): ByteBuffer? {
    try {
        return this.toByteBuffer()
    } catch (ignored: Exception) {
        warn("[$this] was not found within the classpath.")
    }
    return null
}