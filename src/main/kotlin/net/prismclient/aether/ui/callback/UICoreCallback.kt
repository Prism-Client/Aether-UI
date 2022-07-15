package net.prismclient.aether.ui.callback

/**
 * [UICoreCallback] is an interface for getting data that
 * is not available to aether by default.
 *
 * @author sen
 * @since 5/13/2022
 */
interface UICoreCallback {
    /**
     * Returns the color of the pixel at the given position
     *
     * @return RGB(A) formatted int
     */
    fun getPixelColor(x: Float, y: Float): Int
}