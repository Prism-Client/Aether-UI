package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.shape.type.UIRectangle

/**
 * [UISliderShape] is the shape which represents the control for the slider.
 *
 * @author sen
 * @since 1.0
 * @see UISlider
 * @see UISliderSheet
 */
open class UISliderShape : UIRectangle() {
    var offset = 0f

    override fun render() {
        val temp = cachedX
        cachedX += offset
        super.render()
        cachedX = temp
    }

    override fun copy(): UISliderShape = UISliderShape().also {
        it.apply(this)
        it.radius = radius?.copy()
        it.offset = offset
    }
}