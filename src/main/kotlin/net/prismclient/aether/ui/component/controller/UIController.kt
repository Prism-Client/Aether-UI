package net.prismclient.aether.ui.component.controller

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.warn

/**
 * [UIController] is a transparent system that monitors and controls
 * the state of a list of [UIComponent]. It contains a list of [UIComponent]
 * and controls their state based on a given input.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIController<T : UIComponent<*>> {
    /**
     * The list of components to be handled
     *
     * @see addComponent
     * @see removeComponent
     */
    var components = ArrayList<T>()
        protected set

    /**
     * Adds a [UIComponent] to the list of [UIComponent]s to be controlled.
     *
     * @param component The [UIComponent] to be added.
     * @return The [UIController] instance.
     */
    open fun addComponent(component: UIComponent<*>) {
        components.add(component as T)
    }

    /**
     * Removes a [UIComponent] from the list of [UIComponent]s to be controlled.
     *
     * @param component The [UIComponent] to be removed.
     * @return The [UIController] instance.
     */
    open fun removeComponent(component: UIComponent<*>) {
        components.add(component as T)
    }
}