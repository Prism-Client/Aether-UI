package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.rel

class UISliderSheet : UIStyleSheet() {
    var controlX: UIUnit = rel(0.5)
    var controlY: UIUnit = rel(0.5)
    var controlWidth: UIUnit = px(40)
    var controlHeight: UIUnit = rel(1)

    override fun copy(): UIStyleSheet {
        val it = UISliderSheet()
        it.apply(this)
        return it
    }
}