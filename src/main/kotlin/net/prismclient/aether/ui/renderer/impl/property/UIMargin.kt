package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.fromProgress
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

class UIMargin : UICopy<UIMargin>, UIAnimatable<UIMargin> {
    var marginTop: UIUnit? = null
    var marginRight: UIUnit? = null
    var marginBottom: UIUnit? = null
    var marginLeft: UIUnit? = null
    override fun copy(): UIMargin = UIMargin().also {
        it.marginTop = marginTop?.copy()
        it.marginRight = marginRight?.copy()
        it.marginBottom = marginBottom?.copy()
        it.marginLeft = marginLeft?.copy()
    }

    private var cachedMargin: CachedValues? = null

    override fun updateAnimationCache(component: UIComponent<*>) {
        cachedMargin = CachedValues(component.marginTop, component.marginRight, component.marginBottom, component.marginLeft)
    }

    override fun clearAnimationCache() {
        cachedMargin = null
    }

    override fun animate(previous: UIMargin?, current: UIMargin?, progress: Float, component: UIComponent<*>) {
        cachedMargin = cachedMargin ?: CachedValues(component.marginTop, component.marginRight, component.marginBottom, component.marginLeft)

        component.marginTop = fromProgress(
                if (previous == null) cachedMargin!!.top else component.computeUnit(previous.marginTop, true),
                if (current == null) cachedMargin!!.top else component.computeUnit(current.marginTop, true),
                progress
        )

        component.marginRight = fromProgress(
                if (previous == null) cachedMargin!!.right else component.computeUnit(previous.marginRight, false),
                if (current == null) cachedMargin!!.right else component.computeUnit(current.marginRight, false),
                progress
        )

        component.marginBottom = fromProgress(
            if (current == null) cachedMargin!!.bottom else component.computeUnit(current.marginBottom, true),
                if (previous == null) cachedMargin!!.bottom else component.computeUnit(previous.marginBottom, true),
                progress
        )

        component.marginLeft = fromProgress(
            if (current == null) cachedMargin!!.left else component.computeUnit(current.marginLeft, false),
            if (previous == null) cachedMargin!!.left else component.computeUnit(previous.marginLeft, false),
            progress
        )
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIMargin?, retain: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "UIMargin(marginTop=$marginTop, marginRight=$marginRight, marginBottom=$marginBottom, marginLeft=$marginLeft)"
    }

    inner class CachedValues(val top: Float, val right: Float, val bottom: Float, val left: Float)
}