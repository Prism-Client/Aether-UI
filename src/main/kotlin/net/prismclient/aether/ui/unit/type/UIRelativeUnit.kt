package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.animation.UIAnimation

/**
 * [UIRelativeUnit] represents a [UIUnit]. By default, it is exactly like
 * a [UIUnit] except when in an [UIAnimation] style sheet. When in a [UIAnimation]
 * style sheet, the value is equal to the value += this instead of value = this
 *
 * @author sen
 * @since 4/5/2022
 */
class UIRelativeUnit(value: Float, type: Byte): UIUnit(value, type)