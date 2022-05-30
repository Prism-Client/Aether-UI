package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.component.UIComponent
import java.util.function.Consumer
import kotlin.math.roundToInt

/**
 * [UISlider] is a form of input which allows the user to select a value
 * from a range. The default slider [UISlider], supports a minimum, maximum,
 * and step to control the value. If a range is preferred, please refer to
 * [UIRangeSlider] instead.
 *
 * @author sen
 * @since 5/13/2022
 *
 * @param value The value of the slider
 * @param min The minimum value that the slider should be
 * @param max The maximum value that the slider should be
 * @param step The value which the slider should step by
 */
open class UISlider(value: Float, var min: Float, var max: Float, var step: Float, style: String) :
    UIComponent<UISliderSheet>(style) {
    var value: Float = value
        set(value) {
            val prev = this.value
            field = ((value / step).roundToInt() * step).coerceAtLeast(min).coerceAtMost(max)
            if (prev != field)
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
//        if (selected) { // I got lazy
//            normalizedValue =
//                (((UICore__.mouseX - offsetX - style.sliderControl.cachedX)) / (relWidth - style.sliderControl.cachedWidth))
//                    .coerceAtLeast(0f)
//                    .coerceAtMost(1f)
//            updateSlider()
//        }
    }

//    override fun mousePressed(mouseX: Float, mouseY: Float) {
//        super.mousePressed(mouseX, mouseY)
//
//        if (isWithinBounds(
//                style.sliderControl.cachedX + style.sliderControl.offsetX,
//                style.sliderControl.cachedY + style.sliderControl.offsetY,
//                style.sliderControl.cachedWidth,
//                style.sliderControl.cachedHeight
//            )
//        ) {
//            offsetX = UICore__.mouseX - (style.sliderControl.cachedX + style.sliderControl.offsetX)
//            selected = true
//        }
//    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        selected = false
    }

    fun updateSlider() {
        value = (max - min) * normalizedValue + min
        normalizedValue = value / (max - min)
    }

    /**
     * Adds a slider value change listener. When the values of the slider
     * changes, the event is invoked.
     *
     * @param acceptImmediately If true, the event is invoked on add.
     */
    @JvmOverloads
    fun onValueChanged(acceptImmediately: Boolean = false, event: Consumer<UISlider>) {
        if (valueListeners == null)
            valueListeners = mutableListOf()
        valueListeners!!.add(event)
        if (acceptImmediately)
            event.accept(this)
    }
}