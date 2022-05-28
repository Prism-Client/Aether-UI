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
        cachedMargin = cachedMargin ?: CachedValues(component.paddingTop, component.paddingRight, component.paddingBottom, component.paddingLeft)

        component.marginTop = fromProgress(
                if (previous == null) cachedMargin!!.top else component.getY(previous.marginTop),
                if (current == null) cachedMargin!!.top else component.getY(current.marginTop),
                progress
        )

        component.marginRight = fromProgress(
                if (previous == null) cachedMargin!!.right else component.getX(previous.marginRight),
                if (current == null) cachedMargin!!.right else component.getX(current.marginRight),
                progress
        )

        component.marginBottom = fromProgress(
                if (previous == null) cachedMargin!!.bottom else component.getY(previous.marginBottom),
                if (current == null) cachedMargin!!.bottom else component.getY(current.marginBottom),
                progress
        )

        component.marginLeft = fromProgress(
                if (previous == null) cachedMargin!!.left else component.getX(previous.marginLeft),
                if (current == null) cachedMargin!!.left else component.getX(current.marginLeft),
                progress
        )
    }

    inner class CachedValues(val top: Float, val right: Float, val bottom: Float, val left: Float)
}