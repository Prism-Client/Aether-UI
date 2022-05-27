package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.interfaces.UICopy

class UIMargin : UICopy<UIMargin> {
    var marginTop: UIUnit? = null
    var marginRight: UIUnit? = null
    var marginBottom: UIUnit? = null
    var marginLeft: UIUnit? = null

    fun animate() {
        // TODO
    }

    override fun copy(): UIMargin = UIMargin().also {
        it.marginTop = marginTop?.copy()
        it.marginRight = marginRight?.copy()
        it.marginBottom = marginBottom?.copy()
        it.marginLeft = marginLeft?.copy()
    }
}