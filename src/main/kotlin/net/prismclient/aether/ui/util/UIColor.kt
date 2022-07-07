package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.util.extensions.getAlpha
import net.prismclient.aether.ui.util.extensions.getBlue
import net.prismclient.aether.ui.util.extensions.getGreen
import net.prismclient.aether.ui.util.extensions.getRed
import net.prismclient.aether.ui.util.interfaces.UICopy
import java.awt.Color

/**
 * [UIColor] represents an RGBA color. It also provides utility functions to convert
 * the color into different formats. The color could be stored as an Int, as the color
 * stores exactly enough bytes to support a 32-bit color, however, because of how animations
 * are designed, the color property must be nullable. Theoretically, the Int could be represented
 * as it's class type, Integer, however there is overhead to boxing and unboxing the integer. Using
 * a wrapper avoids the boxing and unboxing, reducing the overhead, though probably slower than having
 * it as a normal Int.
 *
 * However, because it is wrapped in a class, certain optimizations can now be used especially when converting
 * to HSV, as the values can be cached, thus reducing overhead from calculations.
 *
 * @author sen
 * @since 1.1
 */
class UIColor(color: Int) : UICopy<UIColor> {
    /**
     * Creates the [UIColor] from an HSV color type.
     */
    constructor(hue: Float, saturation: Float, brightness: Float) : this(Color.HSBtoRGB(hue, saturation, brightness))

    var rgba: Int = color
        set(value) {
            field = value
            // Update the HSV value if it is not null
            if (hsv != null)
                Color.RGBtoHSB(rgba.getRed(), rgba.getBlue(), rgba.getGreen(), hsv)
        }
    var hsv: FloatArray? = null

    fun getHSV(): FloatArray {
        if (hsv == null) {
            hsv = FloatArray(3)
            Color.RGBtoHSB(rgba.getRed(), rgba.getBlue(), rgba.getGreen(), hsv)
        }
        return hsv!!
    }

    fun getRed() = rgba.getRed()

    fun getGreen() = rgba.getGreen()

    fun getBlue() = rgba.getBlue()

    fun getAlpha() = rgba.getAlpha()

    override fun copy(): UIColor = UIColor(rgba)

    override fun toString(): String = "UIColor(color=$rgba, hsv=${hsv.contentToString()}, r:${rgba.getRed()}, g:${rgba.getGreen()}, b:${rgba.getBlue()}, a:${rgba.getAlpha()})"
}