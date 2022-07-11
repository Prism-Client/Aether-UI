package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.Block

/**
 * [UISliderSheet] is the corresponding sheet to [UISlider]. It contains the slider shape
 * which is the shape which is moved to control the value of the slider
 *
 * @author sen
 * @since 1.0
 * @see UISlider
 * @see UISliderShape
 * @see UISliderSheet.control The slider shape.
 */
class UISliderSheet @JvmOverloads constructor(name: String = "") : UIStyleSheet(name) {
    /**
     * The slider shape. The [UISliderShape.x] dictates the offset of the slider.
     */
    var control: UISliderShape = UISliderShape()

    inline fun control(block: Block<UISliderShape>) = control.block()

    override fun copy(): UISliderSheet = UISliderSheet(name).also {
        it.apply(this)
        it.control = control.copy()
    }
}