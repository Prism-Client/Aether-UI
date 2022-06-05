package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.fromProgress
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

class UIPadding : UICopy<UIPadding>, UIAnimatable<UIPadding> {
    var paddingTop: UIUnit? = null
    var paddingRight: UIUnit? = null
    var paddingBottom: UIUnit? = null
    var paddingLeft: UIUnit? = null

    override fun copy(): UIPadding = UIPadding().also {
        it.paddingTop = paddingTop?.copy()
        it.paddingRight = paddingRight?.copy()
        it.paddingBottom = paddingBottom?.copy()
        it.paddingLeft = paddingLeft?.copy()
    }

    private var cachedPadding: CachedValues? = null

    override fun updateAnimationCache(component: UIComponent<*>) {
        cachedPadding = CachedValues(component.paddingTop, component.paddingRight, component.paddingBottom, component.paddingLeft)
    }

    override fun clearAnimationCache() {
        cachedPadding = null
    }

    override fun animate(previous: UIPadding?, current: UIPadding?, progress: Float, component: UIComponent<*>) {
        cachedPadding = cachedPadding ?: CachedValues(component.paddingTop, component.paddingRight, component.paddingBottom, component.paddingLeft)

        component.paddingTop = fromProgress(
                if (previous == null) cachedPadding!!.top else component.computeUnit(previous.paddingTop, true),
                if (current == null) cachedPadding!!.top else component.computeUnit(current.paddingTop, true),
                progress
        )

        component.paddingRight = fromProgress(
                if (previous == null) cachedPadding!!.right else component.computeUnit(previous.paddingRight, false),
                if (current == null) cachedPadding!!.right else component.computeUnit(current.paddingRight, false),
                progress
        )

        component.paddingBottom = fromProgress(
                if (previous == null) cachedPadding!!.bottom else component.computeUnit(previous.paddingBottom, true),
                if (current == null) cachedPadding!!.bottom else component.computeUnit(current.paddingBottom, true),
                progress
        )

        component.paddingLeft = fromProgress(
                if (previous == null) cachedPadding!!.left else component.computeUnit(previous.paddingLeft, false),
                if (current == null) cachedPadding!!.left else component.computeUnit(current.paddingLeft, false),
                progress
        )
    }

    override fun saveState(component: UIComponent<*>, keyframe: UIPadding?, retain: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun toString(): String {
        return "UIPadding(paddingTop=$paddingTop, paddingRight=$paddingRight, paddingBottom=$paddingBottom, paddingLeft=$paddingLeft)"
    }

    inner class CachedValues(val top: Float, val right: Float, val bottom: Float, val left: Float)
}