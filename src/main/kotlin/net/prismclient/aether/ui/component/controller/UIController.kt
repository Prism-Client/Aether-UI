package net.prismclient.aether.ui.component.controller

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.warn
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * [UIController] is a transparent system that monitors and controls
 * the state of a list of [UIComponent]. It contains a list of [UIComponent]
 * and controls their state based on a given input.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIController<T : UIComponent<*>>(val filter: KClass<T>) {

    /**
     * The list of components to be handled
     *
     * @see addComponent
     * @see removeComponent
     */
    var components = ArrayList<T>()
        protected set

    /**
     * Adds a [component] to the list of [UIComponent]. The [component] is expected to be an instance of [T].
     */
    open fun addComponent(component: UIComponent<*>) {
        components.add(component as T)
    }

    /**
     * Removes the given [component] if it was found within the list.
     */
    open fun removeComponent(component: UIComponent<*>) {
        components.remove(component)
    }
}