package net.prismclient.aether.ui.style.util

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.util.extensions.px

class UIAnchorPoint {
    var x: UIUnit? = null
    var y: UIUnit? = null

    infix fun align(alignment: UIAlignment) {
        x ?: run { x = px(0) }
        y ?: run { y = px(0) }
        net.prismclient.aether.ui.util.extensions.align(alignment, x!!, y!!)
    }

    fun copy(): UIAnchorPoint = UIAnchorPoint().also {
        it.x = x?.copy()
        it.y = y?.copy()
    }
}