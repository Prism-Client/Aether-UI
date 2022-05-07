package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy

class UIPadding : UICopy<UIPadding> {
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
}