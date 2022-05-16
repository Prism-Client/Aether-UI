package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.UICore
import net.prismclient.aether.ui.component.UIComponent
import java.util.function.Consumer
import kotlin.math.roundToInt

/**
 * [UISlider]
 *
 * @author sen
 * @since 5/13/2022
 */
open class UISlider(value: Float, var min: Float, var max: Float, var step: Float, style: String) : UIComponent<UISliderSheet>(style) {
    var value: Float = value
        set(value) {
            field = value
            valueListeners?.forEach { it.accept(this) }
        }

    protected var normalizedValue = (value / (max - min))
            .coerceAtLeast(0f)
            .coerceAtMost(1f)
    protected var offsetX = 0f
    protected var selected = false

    protected var valueListeners: MutableList<Consumer<UISlider>>? = null

    override fun renderComponent() {
        style.sliderControl.update(this)
        style.sliderControl.offsetX = normalizedValue * (relWidth - style.sliderControl.cachedWidth)
        style.sliderControl.render()
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        if (selected) { // I got lazy
            normalizedValue = (((UICore.mouseX - offsetX - style.sliderControl.cachedX)) / (relWidth - style.sliderControl.cachedWidth))
                    .coerceAtLeast(0f)
                    .coerceAtMost(1f)
            updateSlider()
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
            offsetX = UICore.mouseX - (style.sliderControl.cachedX + style.sliderControl.offsetX)
            selected = true
        }
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        selected = false
    }

    fun updateSlider() {
        value = (max - min) * normalizedValue + min
        value = (value / step).roundToInt() * step
        normalizedValue = value / (max - min)
    }

    fun onValueChanged(event: Consumer<UISlider>) {
        if (valueListeners == null)
            valueListeners = mutableListOf()
        valueListeners!!.add(event)
    }
}