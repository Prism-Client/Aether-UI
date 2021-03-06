package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.util.extensions.PIXELS
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIUnit] represents a unit on screen. It containers [value], a float which represents the value
 * which with the [type] value will be calculated to produce an output value. Please refer to the
 * [net.prismclient.aether.ui.util.extensions.UnitKt] class for more information on what each unit does.
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