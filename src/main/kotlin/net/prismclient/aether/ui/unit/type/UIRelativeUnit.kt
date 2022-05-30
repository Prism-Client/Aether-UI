package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.unit.UIUnit

/**
 * [UIRelativeUnit] acts as a normal [UIUnit] except when in an
 * active animation. When in an active animation this represents the
 * initial starting point plus the calculated value.
 *
 * @author sen
 * @since 5/26/2022
 */
class UIRelativeUnit(value: Float, type: Byte) : UIUnit(value, type) {
    override fun toString(): String {
        return "UIRelativeUnit(value=$value, type=$type)"
    }
}