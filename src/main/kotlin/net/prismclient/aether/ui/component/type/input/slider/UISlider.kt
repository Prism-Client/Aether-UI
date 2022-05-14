package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UISlider]
 *
 * @author sen
 * @since 5/13/2022
 */
open class UISlider<T : Number>(
        var value: T,
        var min: T,
        var max: T,
        var step: T,
        style: String
) : UIComponent<UISliderSheet>(style) {
    protected var controlX = 0f
    protected var controlY = 0f
    protected var controlWidth = 0f
    protected var controlHeight = 0f

    override fun update() {
        super.update()
        controlX = style.controlX.getX(relWidth)
        controlY = style.controlY.getY(relHeight)
        controlWidth = style.controlWidth.getX(relWidth)
        controlHeight = style.controlHeight.getY(relHeight)
    }

    override fun renderComponent() {
        renderer {
            
        }
    }
}