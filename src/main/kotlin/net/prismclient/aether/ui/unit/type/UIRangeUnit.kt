package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.unit.UIUnit

/**
 * [UIRangeUnit] accepts two units as the range. The [min] unit describes
 * the minimum value that a unit can be while the [max] unit describes
 * the largest value a unit can be. With the data provided by [min] and [max],
 * the output will be constrained to the range based on [unit]
 *
 * @author sen
 * @since 5/26/2022
 */
class UIRangeUnit(var unit: UIUnit, var min: UIUnit, var max: UIUnit) : UIUnit(0f, -1)