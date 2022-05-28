package net.prismclient.aether.ui.style.util

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.fromProgress
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
        net.prismclient.aether.ui.util.extensions.align(alignment, x!!, y!!)
    }

    fun copy(): UIAnchorPoint = UIAnchorPoint().also {
        it.x = x?.copy()
        it.y = y?.copy()
    }

    private var anchorCache: AnchorCache? = null

    override fun updateAnimationCache(component: UIComponent<*>) {
        // TODO: Anchor point cache updating
    }

    override fun clearAnimationCache() {
        anchorCache = null
    }

    override fun animate(previous: UIAnchorPoint?, current: UIAnchorPoint?, progress: Float, component: UIComponent<*>) {
        anchorCache = anchorCache ?: AnchorCache(component.anchorX, component.anchorY)

        component.anchorX = fromProgress(
            if (current?.x != null) component.calculateUnitX(current.x, component.width, false) else anchorCache!!.x,
            if (previous?.x != null) component.calculateUnitX(previous.x, component.width, false) else anchorCache!!.x,
            progress
        )

        component.anchorY = fromProgress(
            if (current?.y != null) component.calculateUnitY(current.y, component.height, false) else anchorCache!!.y,
            if (previous?.y != null) component.calculateUnitY(previous.y, component.height, false) else anchorCache!!.y,
            progress
        )
    }

    override fun saveState(component: UIComponent<*>, retain: Boolean) {
        // TODO: Save state anchor point
    }

    private inner class AnchorCache(var x: Float, var y: Float)
}