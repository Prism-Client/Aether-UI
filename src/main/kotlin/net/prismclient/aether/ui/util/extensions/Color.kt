package net.prismclient.aether.ui.util.extensions

fun Int.getRed(): Int = this shr 16 and 0xFF

fun Int.getGreen(): Int = this shr 8 and 0xFF

fun Int.getBlue(): Int = this and 0xFF

fun Int.getAlpha(): Int = this shr 24 and 0xFF

fun Int.setAlpha(alpha: Int): Int = (this and 0x00FFFFFF) or (alpha shl 24)

fun Int.setAlpha(alpha: Float): Int = (this and 0x00FFFFFF) or ((alpha * 255f + 0.5).toInt() shl 24)

fun asRGBA(r: Int, g: Int, b: Int, a: Int = 255) =
    a and 0xFF shl 24 or (r and 0xFF shl 16) or (g and 0xFF shl 8) or (b and 0xFF)

fun asRGBA(r: Float, g: Float, b: Float, a: Float = 1f): Int =
    (a * 255 + 0.5).toInt() and 0xFF shl 24 or ((r * 255 + 0.5).toInt() and 0xFF shl 16) or ((g * 255 + 0.5).toInt() and 0xFF shl 8) or ((b * 255 + 0.5).toInt() and 0xFF)

fun asRGBA(r: Int, g: Int, b: Int, a: Float) = asRGBA(r, g, b, (a * 255 + 0.5).toInt())

fun Int.limitRange(): Int =
    asRGBA(this.getRed().limit(), this.getGreen().limit(), this.getBlue().limit(), this.getAlpha().limit())

/**
 * Returns the value if it is greater than 0, and less than 255
 */
fun Int.limit(): Int = this.coerceAtMost(255).coerceAtLeast(0)

/**
 * Returns the value if it is greater than 0f, and less than 1f
 */
fun Float.limit(): Float = this.coerceAtMost(1f).coerceAtLeast(0f)

/**
 * Creates a new color from two provided values based on the progress between each value.
 */
fun transition(c1: Int, c2: Int, progress: Float): Int {
    val prog = progress.limit()

    val red = c1.getRed() + ((c2.getRed() - c1.getRed()) * prog).toInt()
    val green = c1.getGreen() + ((c2.getGreen() - c1.getGreen()) * prog).toInt()
    val blue = c1.getBlue() + ((c2.getBlue() - c1.getBlue()) * prog).toInt()
    val alpha = c1.getAlpha() + ((c2.getAlpha() - c1.getAlpha()) * prog).toInt()

    return asRGBA(red.limit(), green.limit(), blue.limit(), alpha.limit())
}