package net.prismclient.aether.ui.style.util

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.align
import net.prismclient.aether.ui.util.extensions.lerp
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.interfaces.UIAnimatable

/**
 * [UIAnchorPoint] holds two [UIUnit], [x], and [y]. They represent the position
 * within a component. When the component's position is updated, the two [UIUnit]
 * are incorporated into the position calculation.
 *
 * @author sen
 * @since 1.0
 */
class UIAnchorPoint : UIAnimatable<UIAnchorPoint> {
    var x: UIUnit? = null
    var y: UIUnit? = null

    infix fun align(alignment: UIAlignment) {
        x ?: run { x = px(0) }
        y ?: run { y = px(0) }
        align(alignment, x!!, y!!)
    }

    override fun animate(
        animation: UIAnimation<*>,
        previous: UIAnchorPoint?,
        current: UIAnchorPoint?,
        progress: Float
    ) {
        val component = animation.component
        component.anchorX = current?.x?.lerp(previous?.x, component, progress, false) ?: component.anchorX
        component.anchorY = current?.y?.lerp(previous?.y, component, progress, false) ?: component.anchorY
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIAnchorPoint?) {
        x = keyframe?.x ?: x?.copy()
        y = keyframe?.y ?: y?.copy()
    }

    fun copy(): UIAnchorPoint = UIAnchorPoint().also {
        it.x = x?.copy()
        it.y = y?.copy()
    }

    override fun toString(): String = "UIAnchorPoint(x=$x, y=$y)"
}