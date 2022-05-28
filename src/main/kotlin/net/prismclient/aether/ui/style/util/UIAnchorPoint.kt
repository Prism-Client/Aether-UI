package net.prismclient.aether.ui.style.util

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.unit.UIUnit
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

    private var anchorCache: UIAnchorPoint? = null

    override fun updateAnimationCache(component: UIComponent<*>) {
        // TODO: Anchor point cache updating
    }

    override fun clearAnimationCache() {
        anchorCache = null
    }

    override fun animate(previous: UIAnchorPoint?, current: UIAnchorPoint?, progress: Float, component: UIComponent<*>) {
        anchorCache = anchorCache ?: copy()

        // TODO: UIUnit animation
    }
}