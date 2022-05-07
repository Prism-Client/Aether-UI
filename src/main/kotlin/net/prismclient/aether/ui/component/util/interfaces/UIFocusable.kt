package net.prismclient.aether.ui.component.util.interfaces

import net.prismclient.aether.ui.component.UIComponent

/**
 * [UIFocusable] indicates a component which can be focused. This
 * can be useful for components that accept input, such as text fields
 * to ensure that only one component is accepting input at a time
 *
 * @author sen
 * @since 4/27/2022
 * @param T The component (to ensure that the class extends [UIComponent<*>])
 */
interface UIFocusable<in T : UIComponent<*>> {
    // var focused: Boolean

    /**
     * Called when the component is focused
     */
    fun focus()

    /**
     * Called when the user removes focus from the current component
     */
    fun removeFocus()
}