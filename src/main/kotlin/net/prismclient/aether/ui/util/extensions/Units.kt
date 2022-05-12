package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.unit.util.UIOperation.*
import net.prismclient.aether.ui.unit.util.*

/** General Units **/

fun px(value: Number) = UIUnit(value.toFloat(), PIXELS)

fun percent(value: Number) = UIUnit(value.toFloat() / 100f, RELATIVE)

fun em(value: Number) = UIUnit(value.toFloat(), EM)

fun ascender(value: Number) = UIUnit(value.toFloat(), ASCENDER)

fun descender(value: Number) = UIUnit(value.toFloat(), DESCENDER)

fun unit(value: Number, type: Byte) = UIUnit(value.toFloat(), type)

fun operation(unit1: UIUnit, unit2: UIUnit, operation: UIOperation) = UIOperationUnit(unit1, unit2, operation)

/**
 * When in a [UIAnimation] style sheet, the value is equal to the value += this instead of value = this
 */
fun rel(value: Number) = UIUnit(value.toFloat())

/** Operator functions **/

/**
 * Creates an addition [UIOperationUnit] from two units
 */
operator fun UIUnit.plus(unit: UIUnit) = UIOperationUnit(this, unit, ADD)

operator fun UIUnit.minus(unit: UIUnit) = UIOperationUnit(this, unit, SUBTRACT)

operator fun UIUnit.times(unit: UIUnit) = UIOperationUnit(this, unit, MULTIPLY)

operator fun UIUnit.div(unit: UIUnit) = UIOperationUnit(this, unit, DIVIDE)

/** Calculation functions **/

/**
 * Calculates a [UIUnit] given a component on the x-axis.
 *
 * @param ignoreOperation When true, the calculation will to care if the [UIUnit] is an instance of [UIOperationUnit]
 */
@JvmOverloads
fun calculateX(unit: UIUnit,  component: UIComponent<*>, ignoreOperation: Boolean = false, width: Float = component.getParentWidth()): Float {
    return if (!ignoreOperation && unit is UIOperationUnit) calculateX(unit, component)
    else when (unit.type) {
        PIXELS, PXANIMRELATIVE -> unit.value
        RELATIVE, RELANIMRELATIVE -> unit.value * width
        EM -> unit.value * (component.style.font?.fontSize ?: 0f)
        ASCENDER -> unit.value * (component.style.font?.getAscend() ?: 0f)
        DESCENDER -> unit.value * (component.style.font?.getDescend() ?: 0f)
        else -> throw RuntimeException("{${unit.type} is not a valid Unit Type.}")
    }
}

/**
 * Returns the value of a [UIOperationUnit] on the y-axis
 */
fun calculateX(operationUnit: UIOperationUnit, component: UIComponent<*>): Float = when (operationUnit.operation) {
    ADD -> calculateX(operationUnit.unit1, component) + calculateX(operationUnit.unit2, component)
    SUBTRACT -> calculateX(operationUnit.unit1, component) - calculateX(operationUnit.unit2, component)
    MULTIPLY -> calculateX(operationUnit.unit1, component) * calculateX(operationUnit.unit2, component)
    DIVIDE -> calculateX(operationUnit.unit1, component) / calculateX(operationUnit.unit2, component)
}

/**
 * Calculates a [UIUnit] given a component on the y-axis.
 *
 * @param ignoreOperation When true, the calculation will to care if the [UIUnit] is an instance of [UIOperationUnit]
 */
@JvmOverloads
fun calculateY(unit: UIUnit, component: UIComponent<*>, ignoreOperation: Boolean = false, height: Float = component.getParentHeight()): Float {
    return if (!ignoreOperation && unit is UIOperationUnit) calculateY(unit, component)
    else when (unit.type) {
        PIXELS, PXANIMRELATIVE -> unit.value
        RELATIVE, RELANIMRELATIVE -> unit.value * height
        EM -> unit.value * (component.style.font?.fontSize ?: 0f)
        ASCENDER -> unit.value * (component.style.font?.getAscend() ?: 0f)
        DESCENDER -> unit.value * (component.style.font?.getDescend() ?: 0f)
        else -> throw RuntimeException("{${unit.type} is not a valid Unit Type.}")
    }
}

/**
 * Returns the value of a [UIOperationUnit] on the y-axis
 */
fun calculateY(operationUnit: UIOperationUnit, component: UIComponent<*>): Float = when (operationUnit.operation) {
    ADD -> calculateY(operationUnit.unit1, component) + calculateY(operationUnit.unit2, component)
    SUBTRACT -> calculateY(operationUnit.unit1, component) - calculateY(operationUnit.unit2, component)
    MULTIPLY -> calculateY(operationUnit.unit1, component) * calculateY(operationUnit.unit2, component)
    DIVIDE -> calculateY(operationUnit.unit1, component) / calculateY(operationUnit.unit2, component)
}

/** Other **/

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

/**
 * Returns true if the [UIUnit.type] is a relative animation type
 */
fun Byte.isAnimationRelative(): Boolean =
    this == PXANIMRELATIVE || this == RELANIMRELATIVE

/**
 * Returns true if the Unit is of a normal unit that is not unique to a given component
 */
fun Byte.isNormal(): Boolean =
    this == PIXELS || this == RELATIVE || this == EM || this == ASCENDER || this == DESCENDER