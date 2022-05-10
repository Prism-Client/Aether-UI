package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.unit.util.*

fun px(value: Number) = UIUnit(value.toFloat(), PIXELS)

fun percent(value: Number) = UIUnit(value.toFloat() / 100f, RELATIVE)

fun unit(value: Number, type: Byte) = UIUnit(value.toFloat(), type)

/**
 * When in a [UIAnimation] style sheet, the value is equal to the value += this instead of value = this
 *
 * @author sen
 * @since 6/5/2022
 */
fun rel(value: Number) = UIUnit(value.toFloat(), )

///**
// * Calculates the x-axis of a [UIUnit] (or subclasses) given the width of the area to calculate.
// *
// * @param width The width to take into consideration with calculating (generally the parent width of a component
// * @param ignore If flagged, a [UIOperationUnit] will be read as a [UIUnit] instead of itself
// */
//@JvmOverloads
//fun UIUnit?.calculateX(width: Float, ignore: Boolean = false): Float = if (this == null) 0f else {
//    if (!ignore && this is UIOperationUnit) { this.calculateX(width) } else {
//        when (this.type) {
//            PIXELS -> this.value
//            RELATIVE -> this.value * width
//            else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
//        }
//    }
//}
//
//fun UIOperationUnit.calculateX(width: Float): Float = when (this.operation) {
//    UIOperation.ADD -> this.calculateX(width, true) + otherUnit.calculateX(width)
//    UIOperation.SUBTRACT -> this.calculateX(width, true) - otherUnit.calculateX(width)
//    UIOperation.MULTIPLY -> this.calculateX(width, true) * otherUnit.calculateX(width)
//    UIOperation.DIVIDE -> this.calculateX(width, true) / otherUnit.calculateX(width)
//}
//
///**
// * Calculates the x-axis of a [UIUnit] (or subclasses) given the width of the area to calculate.
// *
// * @param height The width to take into consideration with calculating (generally the parent width of a component
// * @param ignore If flagged, a [UIOperationUnit] will be read as a [UIUnit] instead of itself
// */
//@JvmOverloads
//fun UIUnit?.calculateY(height: Float, ignore: Boolean = false): Float = if (this == null) 0f else {
//    if (!ignore && this is UIOperationUnit) { this.calculateX(height) } else {
//        when (this.type) {
//            PIXELS -> this.value
//            RELATIVE -> this.value * height
//            else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
//        }
//    }
//}
//
//fun UIOperationUnit.calculateY(height: Float): Float = when (this.operation) {
//    UIOperation.ADD -> this.calculateX(height, true) + otherUnit.calculateX(height)
//    UIOperation.SUBTRACT -> this.calculateX(height, true) - otherUnit.calculateX(height)
//    UIOperation.MULTIPLY -> this.calculateX(height, true) * otherUnit.calculateX(height)
//    UIOperation.DIVIDE -> this.calculateX(height, true) / otherUnit.calculateX(height)
//}

fun align(alignment: UIAlignment, x: UIUnit, y: UIUnit) {
    x.type = RELATIVE
    y.type = RELATIVE
    x.value = when (alignment) {
        UIAlignment.TOPCENTER, UIAlignment.CENTER, UIAlignment.BOTTOMCENTER -> 0.5f
        UIAlignment.TOPRIGHT, UIAlignment.MIDDLERIGHT, UIAlignment.BOTTOMRIGHT -> 1f
        else -> 0f
    }
    y.value = when (alignment) {
        UIAlignment.MIDDLELEFT, UIAlignment.CENTER, UIAlignment.MIDDLERIGHT -> 0.5f
        UIAlignment.BOTTOMLEFT, UIAlignment.BOTTOMCENTER, UIAlignment.BOTTOMRIGHT -> 1f
        else -> 0f
    }
}

fun Byte.isAnimationRelative(): Boolean =
    this == PXANIMRELATIVE || this == RELANIMRELATIVE

/**
 * Returns true if the Unit is of a normal unit that is not unique to a given component
 */
fun Byte.isNormal(): Boolean =
    this == PIXELS || this == RELATIVE || this == EM || this == ASCENDER || this == DESCENDER