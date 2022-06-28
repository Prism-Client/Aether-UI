package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIDependentUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.unit.type.UIRangeUnit
import net.prismclient.aether.ui.unit.util.UIOperation
import java.util.function.Supplier
import net.prismclient.aether.ui.unit.util.UIOperation.*

/**
 * The default unit type. Represents the value in pixels.
 */
const val PIXELS: Byte = 0

/**
 * Represents the screen width or height, depending on the unit times the value.
 *
 * @see WIDTH
 * @see HEIGHT
 */
const val RELATIVE: Byte = 1

/**
 * The font size of the active component
 */
const val EM: Byte = 2

/**
 * The ascender of the active font
 */
const val ASCENDER: Byte = 3

/**
 * The descender of the active font
 */
const val DESCENDER: Byte = 4

/**
 * Like relative, but it is always the width
 */
const val WIDTH: Byte = 5

/**
 * Like relative, but it is always the height
 */
const val HEIGHT: Byte = 6

/**
 * The border width of the active component
 */
const val BORDER: Byte = 7

/**
 * The width of the screen
 */
const val SCREENWIDTH: Byte = 8

/**
 * The height of the screen
 */
const val SCREENHEIGHT: Byte = 9

/** Unit Functions **/

/**
 * Creates a unit that represents the value in pixels.
 */
fun px(value: Number) = UIUnit(value.toFloat(), PIXELS)

/**
 * Creates a unit that represents the value times the width or height of the parent (the window or a component etc..)
 *
 * @param value Is the amount times the width or height.
 */
fun rel(value: Number) = UIUnit(value.toFloat(), RELATIVE)

/**
 * [rel] but described in percentage
 *
 * @see rel
 */
fun percent(value: Number) = UIUnit(value.toFloat() / 100f, RELATIVE)

/**
 * Creates a unit that represents the font size of this (else 0 if not applicable)
 */
fun em(value: Number) = UIUnit(value.toFloat(), EM)

/**
 * The ascender of the active font (else 0 if not applicable)
 */
fun ascender(value: Number) = UIUnit(value.toFloat(), ASCENDER)

/**
 * The descender of the active font (else 0 if not applicable)
 */
fun descender(value: Number) = UIUnit(value.toFloat(), DESCENDER)

/**
 * Simplified way of creating a [UIUnit]
 */
fun unit(value: Number, type: Byte) = UIUnit(value.toFloat(), type)

/**
 * Simplified way of creating a [UIOperationUnit]
 */
fun operation(unit1: UIUnit, unit2: UIUnit, operation: UIOperation) = UIOperationUnit(unit1, unit2, operation)

/**
 * Creates a [UIUnit] with the type [SCREENWIDTH]
 */
fun vw(value: Number) = UIUnit(value.toFloat(), SCREENWIDTH)

/**
 * Creates a [UIUnit] with the type [SCREENHEIGHT]
 */
fun vh(value: Number) = UIUnit(value.toFloat(), SCREENHEIGHT)

fun range(unit: UIUnit, min: UIUnit, max: UIUnit) = UIRangeUnit(unit, min, max)

fun dependentUnit(function: Supplier<Float>) = UIDependentUnit(function)

/** Operator functions **/

operator fun UIUnit?.plus(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, ADD)

operator fun UIUnit?.minus(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, SUBTRACT)

operator fun UIUnit?.times(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, MULTIPLY)

operator fun UIUnit?.div(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, DIVIDE)

/**
 * Calculates the given unit based on the given properties.
 *
 * @param unit The unit to calculate.
 * @param component The component used to calculate certain properties. If not available, the assumed value is
 * @param width The width of the space for this unit (For example, the width of the screen is the width for a component unit)
 * @param height This height of the space for this unit
 * @param isY If true, calculate the y-axis, otherwise calculate the x-axis.
 */
fun calculate(unit: UIUnit?, component: UIComponent<*>?, width: Float, height: Float, isY: Boolean): Float {
    fun calculateOperation(unit: UIOperationUnit, component: UIComponent<*>?, width: Float, height: Float, isY: Boolean): Float {
        val unit1 = calculate(unit.unit1, component, width, height, isY)
        val unit2 = calculate(unit.unit2, component, width, height, isY)
        return when (unit.operation) {
            ADD -> unit1 + unit2
            SUBTRACT -> unit1 - unit2
            MULTIPLY -> unit1 * unit2
            DIVIDE -> unit1 / unit2
        }
    }

    if (unit == null) return 0f
    if (unit is UIOperationUnit) return calculateOperation(unit, component, width, height, isY)
    if (unit is UIRangeUnit) return calculate(unit.unit, component, width, height, isY)
            .coerceAtLeast(calculate(unit.min, component, width, height, isY))
            .coerceAtMost(calculate(unit.max, component, width, height, isY))
    if (unit is UIDependentUnit) {
        unit.value = unit.function.get()
        return unit.value
    }

    return when (unit.type) {
        PIXELS -> unit.value
        RELATIVE -> (if (isY) height else width) * unit.value
        ASCENDER -> (component?.style?.font?.getAscend() ?: 0f) * unit.value
        DESCENDER -> (component?.style?.font?.getDescend() ?: 0f) * unit.value
        EM -> (component?.style?.font?.fontSize ?: 0f) * unit.value
        WIDTH -> width * unit.value
        HEIGHT -> height * unit.value
        SCREENWIDTH -> Aether.width
        SCREENHEIGHT -> Aether.height
        else -> throw RuntimeException("${unit.type} is not considered a unit type.")
    }
}

/**
 * Given an alignment and two units, the function will make the two
 * given units a relative value to fit the alignment constraints.
 */
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