package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.lerp
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIMargin] as the name represents a padding unit for a component. It saves the values as [UIUnit]s.
 *
 * @author sen
 * @since 1.0
 */
class UIMargin : UICopy<UIMargin>, UIAnimatable<UIMargin> {
    var marginTop: UIUnit? = null
    var marginRight: UIUnit? = null
    var marginBottom: UIUnit? = null
    var marginLeft: UIUnit? = null

    override fun animate(animation: UIAnimation<*>, previous: UIMargin?, current: UIMargin?, progress: Float) {
        val component = animation.component
        component.marginTop =
            previous?.marginTop?.lerp(current?.marginTop, component, progress, true) ?: component.marginTop
        component.marginRight =
            previous?.marginRight?.lerp(current?.marginRight, component, progress, true) ?: component.marginRight
        component.marginBottom =
            previous?.marginBottom?.lerp(current?.marginBottom, component, progress, true) ?: component.marginBottom
        component.marginLeft =
            previous?.marginLeft?.lerp(current?.marginLeft, component, progress, true) ?: component.marginLeft
    }

    override fun save(animation: UIAnimation<*>, keyframe: UIMargin?) {
        marginTop = keyframe?.marginTop ?: marginTop
        marginRight = keyframe?.marginRight ?: marginRight
        marginBottom = keyframe?.marginBottom ?: marginBottom
        marginLeft = keyframe?.marginLeft ?: marginLeft
    }

    override fun copy(): UIMargin = UIMargin().also {
        it.marginTop = marginTop?.copy()
        it.marginRight = marginRight?.copy()
        it.marginBottom = marginBottom?.copy()
        it.marginLeft = marginLeft?.copy()
    }

    override fun toString(): String {
        return "UIMargin(marginTop=$marginTop, marginRight=$marginRight, marginBottom=$marginBottom, marginLeft=$marginLeft)"
    }
}