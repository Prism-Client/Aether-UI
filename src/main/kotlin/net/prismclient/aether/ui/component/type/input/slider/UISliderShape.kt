package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.shape.type.UIRectangle

/**
 * [UISliderShape] is the shape which represents the value for a slider
 *
 * @author sen
 * @since 5/13/2022
 */
open class UISliderShape : UIRectangle() {
    var offsetX = 0f
    var offsetY = 0f

    override fun copy(): UISliderShape = UISliderShape().also {
        it.apply(this)
        it.radius = radius?.copy()
        it.offsetX = offsetX
        it.offsetY = offsetY
    }
}