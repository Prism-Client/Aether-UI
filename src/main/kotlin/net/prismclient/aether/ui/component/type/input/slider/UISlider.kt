package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.component.UIComponent
import kotlin.math.roundToInt

/**
 * [UISlider]
 *
 * @author sen
 * @since 5/13/2022
 */
open class UISlider(var value: Float, var min: Float, var max: Float, var step: Float, style: String) : UIComponent<UISliderSheet>(style) {
    protected var normalizedValue = value / (max - min)

    protected var offsetX = 0f
    protected var selected = false

    override fun update() {
        super.update()
        style.sliderControl.update(this)
    }

    override fun renderComponent() {
        style.sliderControl.offsetX = normalizedValue * (relWidth - style.sliderControl.cachedWidth)
        style.sliderControl.render()
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        if (selected) { // I got lazy
            normalizedValue = (((mouseX - offsetX - style.sliderControl.cachedX)) / (relWidth - style.sliderControl.cachedWidth))
                    .coerceAtLeast(0f)
                    .coerceAtMost(1f)
            value = (max - min) * normalizedValue + min
            value = (value / step).roundToInt() * step
            normalizedValue = value / (max - min)
        }
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float) {
        super.mouseClicked(mouseX, mouseY)

        if (isWithinBounds(
                        style.sliderControl.cachedX + style.sliderControl.offsetX,
                        style.sliderControl.cachedY + style.sliderControl.offsetY,
                        style.sliderControl.cachedWidth,
                        style.sliderControl.cachedHeight
        )) {
            offsetX = mouseX - (style.sliderControl.cachedX + style.sliderControl.offsetX)
            selected = true
        }
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        selected = false
    }
}