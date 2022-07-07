package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.fromProgress
import net.prismclient.aether.ui.util.extensions.lerp
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIPadding] as the name represents a padding unit for a component. It saves the values as [UIUnit]s.
 *
 * @author sen
 * @since 1.0
 */
class UIPadding : UICopy<UIPadding>, UIAnimatable<UIPadding> {
    var paddingTop: UIUnit? = null
    var paddingRight: UIUnit? = null
    var paddingBottom: UIUnit? = null
    var paddingLeft: UIUnit? = null

    override fun animate(animation: UIAnimation<*>, previous: UIPadding?, current: UIPadding?, progress: Float) {
        val component = animation.component
        component.paddingTop = previous?.paddingTop?.lerp(current?.paddingTop, component, progress, true) ?: component.paddingTop
        component.paddingRight = previous?.paddingRight?.lerp(current?.paddingRight, component, progress, true) ?: component.paddingRight
        component.paddingBottom = previous?.paddingBottom?.lerp(current?.paddingBottom, component, progress, true) ?: component.paddingBottom
        component.paddingLeft = previous?.paddingLeft?.lerp(current?.paddingLeft, component, progress, true) ?: component.paddingLeft
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIPadding?) {
        paddingTop = keyframe?.paddingTop ?: paddingTop
        paddingRight = keyframe?.paddingRight ?: paddingRight
        paddingBottom = keyframe?.paddingBottom ?: paddingBottom
        paddingLeft = keyframe?.paddingLeft ?: paddingLeft
    }

    override fun copy(): UIPadding = UIPadding().also {
        it.paddingTop = paddingTop?.copy()
        it.paddingRight = paddingRight?.copy()
        it.paddingBottom = paddingBottom?.copy()
        it.paddingLeft = paddingLeft?.copy()
    }

    override fun toString(): String {
        return "UIPadding(paddingTop=$paddingTop, paddingRight=$paddingRight, paddingBottom=$paddingBottom, paddingLeft=$paddingLeft)"
    }
}