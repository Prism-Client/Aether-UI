package net.prismclient.aether.ui.style.util

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.component.util.enums.UIAlignment

class UIAnchorPoint {
    var x: UIUnit = UIUnit()
    var y: UIUnit = UIUnit()

    infix fun align(alignment: UIAlignment) =
        net.prismclient.aether.ui.util.extensions.align(alignment, x, y)

    fun copy(): UIAnchorPoint {
        return UIAnchorPoint().also {
            it.x = x.copy()
            it.y = y.copy()
        }
    }
}