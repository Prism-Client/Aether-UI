package net.prismclient.aether.ui.component.type.input.slider

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.event.input.UIMouseEvent
import java.util.function.BiConsumer
import kotlin.math.roundToInt

/**
 * [UISlider] is a knob or control which allows the user to select a value from a range
 * of options. [UIRangeSlider] allows the user to select two values from a range of options.
 * The [value] is represented as a double, the [range] determines the range of which the value
 * can be, and [step] determines how much the value increments.
 *
 * @author sen
 * @since 1.0
 * @see UISliderSheet
 * @see UISliderShape
 */
open class UISlider(
    value: Double, var range: ClosedFloatingPointRange<Double>, var step: Double, style: String?
) : UIComponent<UISliderSheet>(style) {
    /**
     * The value of this slider.
     */
    var value: Double = 0.0
        set(value) {
            val different = value != field
            field = ((value / step.coerceAtLeast(Double.MIN_VALUE)).roundToInt() * step)
                .coerceAtLeast(range.start)
                .coerceAtMost(range.endInclusive)
//            normalizedValue = value / (range.endInclusive - range.start)
            if (different) valueChangeListeners?.forEach { it.value.accept(this, value) }
        }

    /**
     * [normalizedValue] represents the [value] as a value between 0 and 1.
     */
    var normalizedValue: Double = 0.0
        protected set(value) {
            field = value.coerceAtLeast(0.0).coerceAtMost(1.0)
        }

    /**
     * Returns true if the slider is selected.
     */
    var isSelected = false
        protected set

    /**
     * The listeners for when the value of this slider changes.
     */
    var valueChangeListeners: HashMap<String, BiConsumer<UISlider, Double>>? = null
        protected set

    /**
     * The bounds of the control / slider value shape
     */
    val sliderBounds: FloatArray = FloatArray(4)

    private var mouseOffset = 0f

    init {
        this.value = value
    }

    override fun update() {
        super.update()
        style.control.update(this)
        sliderBounds[1] = style.control.cachedY
        sliderBounds[2] = style.control.cachedWidth
        sliderBounds[3] = style.control.cachedHeight
    }

    override fun renderComponent() {
        style.control.offset = normalizedValue.toFloat() * (width - style.control.cachedWidth)
        style.control.render()
        sliderBounds[0] = style.control.cachedX + style.control.offset
    }

    override fun mousePressed(event: UIMouseEvent) {
        val (x, y, w, h) = sliderBounds
        if (getMouseX() >= x && getMouseY() >= y && getMouseX() <= x + w && getMouseY() <= y + h) {
            isSelected = true
            mouseOffset = x - getMouseX()
        }
        super.mousePressed(event)
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        isSelected = false
        super.mouseReleased(mouseX, mouseY)
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        if (isSelected) {
            normalizedValue = (mouseX - x + mouseOffset).toDouble() / (width - sliderBounds[2])
            value = (range.endInclusive - range.start) * normalizedValue + range.start
        }
        super.mouseMoved(mouseX, mouseY)
    }

    /**
     * Adds a value change listener which is invoked when the value of the slider changes. The [event] is a
     * [BiConsumer] where the first value is this, the [UISlider] and the second is the value of this.
     */
    @JvmOverloads
    fun onValueChange(
        eventName: String = "Default-${valueChangeListeners?.size ?: 0}", event: BiConsumer<UISlider, Double>
    ) {
        valueChangeListeners = valueChangeListeners ?: hashMapOf()
        valueChangeListeners!![eventName] = event
    }
}