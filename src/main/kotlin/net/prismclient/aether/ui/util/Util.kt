package net.prismclient.aether.ui.util

import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIAnchorPoint
import net.prismclient.aether.ui.util.extensions.RELATIVE
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.rel

/**
 * Type alias for a function which has a receiver of [T] and accepts, and returns
 * nothing. The block is intended to apply properties to the receiver [T].
 */
typealias Block<T> = T.() -> Unit

fun <T : UIStyleSheet> T.name(styleName: String): T {
    this.name = styleName
    return this
}

fun FloatArray.minX(): Float = this[0]
fun FloatArray.minY(): Float = this[1]
fun FloatArray.maxX(): Float = this[2]
fun FloatArray.maxY(): Float = this[3]

fun FloatArray.x(): Float = this[0]
fun FloatArray.y(): Float = this[1]
fun FloatArray.width(): Float = this[2]
fun FloatArray.height(): Float = this[3]

fun FloatArray.red(): Float = this[0]
fun FloatArray.green(): Float = this[1]
fun FloatArray.blue(): Float = this[2]
fun FloatArray.alpha(): Float = this[3]

fun FloatArray.advance(): Float = this[4]

fun UIAnchorPoint.anchor(alignment: UIAlignment) {
    x = x ?: rel(0)
    y = y ?: rel(0)

    x!!.value = when (alignment) {
        UIAlignment.TOPLEFT, UIAlignment.MIDDLELEFT, UIAlignment.BOTTOMLEFT -> 0f
        UIAlignment.TOPCENTER, UIAlignment.CENTER, UIAlignment.BOTTOMCENTER -> 0.5f
        UIAlignment.TOPRIGHT, UIAlignment.MIDDLERIGHT, UIAlignment.BOTTOMRIGHT -> 1f
        else -> throw UnsupportedOperationException("Unknown alignment type: $alignment")
    }

    y!!.value = when (alignment) {
        UIAlignment.TOPLEFT, UIAlignment.TOPCENTER, UIAlignment.TOPRIGHT -> 0f
        UIAlignment.MIDDLELEFT, UIAlignment.CENTER, UIAlignment.MIDDLERIGHT -> 0.5f
        UIAlignment.BOTTOMLEFT, UIAlignment.BOTTOMCENTER, UIAlignment.BOTTOMRIGHT -> 1f
        else -> throw UnsupportedOperationException("Unknown alignment type: $alignment")
    }
}