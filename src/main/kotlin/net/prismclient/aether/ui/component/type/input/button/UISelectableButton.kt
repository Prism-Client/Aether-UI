package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import java.util.function.BiConsumer

/**
 * [UISelectableButton] is like a [UIButton], except with the ability
 * to be selected. To do an action when the button selected, use [onCheckChange]
 * which passes this component, and a boolean indicating whether the component
 * is selected or not.
 *
 * @author sen
 * @since 5/24/2022
 */
open class UISelectableButton<T : UIStyleSheet>(checked: Boolean = false, text: String, style: String?) : UIButton<T>(text, style) {
    var checked = checked
        set(value) {
            field = value
            checkListeners?.forEach { it.accept(this, checked) }
        }
    var checkListeners: MutableList<BiConsumer<UISelectableButton<T>, Boolean>>? = null

    init {
        onMousePressed {
            if (isMouseInside()) {
                this.checked = !this.checked
            }
        }
    }

    open fun onCheckChange(block: BiConsumer<UISelectableButton<T>, Boolean>): UISelectableButton<T> {
        checkListeners = checkListeners ?: mutableListOf()
        checkListeners!!.add(block)
        return this
    }
}