package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.unit.util.PIXELS
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIUnit] represents a unit on screen. It holds a float
 * value, named [value], and [type], which describes how
 * the unit should be calculated. Please refer to the [Units.kt]
 * class for more information on what each unit does.
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIUnit @JvmOverloads constructor(var value: Float = 0f, var type: Byte = PIXELS) : UICopy<UIUnit> {
    override fun copy() = UIUnit(value, type)

    override fun toString(): String {
        return "UIUnit(value=$value, type=$type)"
    }
}