package net.prismclient.aether.ui.unit

import net.prismclient.aether.ui.unit.util.PIXELS
import net.prismclient.aether.ui.util.UICopy

/**
 * [UIUnit] represents any positional unit on screen.
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIUnit @JvmOverloads constructor(var value: Float = 0f, var type: Byte = PIXELS) : UICopy<UIUnit> {
    override fun copy() = UIUnit(value, type)
}