package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.unit.util.*
import net.prismclient.aether.ui.unit.util.UIOperation.*

/** General Units **/

fun px(value: Number) = UIUnit(value.toFloat(), PIXELS)


fun rel(value: Number) = UIUnit(value.toFloat(), RELATIVE)

fun percent(value: Number) = UIUnit(value.toFloat() / 100f, RELATIVE)

fun em(value: Number) = UIUnit(value.toFloat(), EM)

fun ascender(value: Number) = UIUnit(value.toFloat(), ASCENDER)

fun descender(value: Number) = UIUnit(value.toFloat(), DESCENDER)

fun unit(value: Number, type: Byte) = UIUnit(value.toFloat(), type)

fun operation(unit1: UIUnit, unit2: UIUnit, operation: UIOperation) = UIOperationUnit(unit1, unit2, operation)

/** Operator functions **/

operator fun UIUnit?.plus(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, ADD)

operator fun UIUnit?.minus(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, SUBTRACT)

operator fun UIUnit?.times(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, MULTIPLY)

operator fun UIUnit?.div(unit: UIUnit) = UIOperationUnit(this ?: px(0), unit, DIVIDE)

/** Calculation functions **/

/**
 * Calculates a [UIUnit] given a component on the x-axis.
 *
 * @param ignoreOperation When true, the calculation will to care if the [UIUnit] is an instance of [UIOperationUnit]
 */
@JvmOverloads
fun calculateX(
    unit: UIUnit?,
    component: UIComponent<*>,
    width: Float = component.getParentWidth(),
    ignoreOperation: Boolean = false
): Float {
    return if (unit == null) 0f else if (!ignoreOperation && unit is UIOperationUnit) calculateX(unit, component, width)
    else when (unit.type) {
        PIXELS,-> unit.value
        RELATIVE -> unit.value * width
        EM -> unit.value * (component.style.font?.fontSize ?: 0f)
        ASCENDER -> unit.value * (component.style.font?.getAscend() ?: 0f)
        DESCENDER -> unit.value * (component.style.font?.getDescend() ?: 0f)
        IMAGEWIDTH -> ((component as UIImage).activeImage?.width?.toFloat() ?: 0f) * unit.value
        IMAGEHEIGHT -> ((component as UIImage).activeImage?.height?.toFloat() ?: 0f) * unit.value
        else -> throw RuntimeException("{${unit.type} is not a valid Unit Type.}")
    }
}

/**
 * Returns the value of a [UIOperationUnit] on the y-axis
 */
fun calculateX(operationUnit: UIOperationUnit, component: UIComponent<*>, width: Float): Float =
    when (operationUnit.operation) {
        ADD -> calculateX(operationUnit.unit1, component, width, false) + calculateX(
            operationUnit.unit2,
            component,
            width,
            false
        )
        SUBTRACT -> calculateX(operationUnit.unit1, component, width, false) - calculateX(
            operationUnit.unit2,
            component,
            width,
            false
        )
        MULTIPLY -> calculateX(operationUnit.unit1, component, width, false) * calculateX(
            operationUnit.unit2,
            component,
            width,
            false
        )
        DIVIDE -> calculateX(operationUnit.unit1, component, width, false) / calculateX(
            operationUnit.unit2,
            component,
            width,
            false
        )
    }

/**
 * Calculates a [UIUnit] given a component on the y-axis.
 *
 * @param ignoreOperation When true, the calculation will to care if the [UIUnit] is an instance of [UIOperationUnit]
 */
@JvmOverloads
fun calculateY(unit: UIUnit?, component: UIComponent<*>, height: Float, ignoreOperation: Boolean = false): Float {
    return if (unit == null) 0f else if (!ignoreOperation && unit is UIOperationUnit) calculateY(
        unit,
        component,
        height
    )
    else when (unit.type) {
        PIXELS -> unit.value
        RELATIVE -> unit.value * height
        EM -> unit.value * (component.style.font?.fontSize ?: 0f)
        ASCENDER -> unit.value * (component.style.font?.getAscend() ?: 0f)
        DESCENDER -> unit.value * (component.style.font?.getDescend() ?: 0f)
        IMAGEWIDTH -> ((component as UIImage).activeImage?.width?.toFloat() ?: 0f) * unit.value
        IMAGEHEIGHT -> ((component as UIImage).activeImage?.height?.toFloat() ?: 0f) * unit.value
        else -> throw RuntimeException("{${unit.type} is not a valid Unit Type.}")
    }
}

/**
 * Returns the value of a [UIOperationUnit] on the y-axis
 */
fun calculateY(operationUnit: UIOperationUnit, component: UIComponent<*>, height: Float): Float =
    when (operationUnit.operation) {
        ADD -> calculateY(operationUnit.unit1, component, height, false) + calculateY(
            operationUnit.unit2,
            component,
            height,
            false
        )
        SUBTRACT -> calculateY(operationUnit.unit1, component, height, false) - calculateY(
            operationUnit.unit2,
            component,
            height,
            false
        )
        MULTIPLY -> calculateY(operationUnit.unit1, component, height, false) * calculateY(
            operationUnit.unit2,
            component,
            height,
            false
        )
        DIVIDE -> calculateY(operationUnit.unit1, component, height, false) / calculateY(
            operationUnit.unit2,
            component,
            height,
            false
        )
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
 * Returns true if the Unit is of a normal unit that is not unique to a given component
 */
fun Byte.isNormal(): Boolean =
    this == PIXELS || this == RELATIVE || this == EM || this == ASCENDER || this == DESCENDER