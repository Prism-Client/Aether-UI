package net.prismclient.aether.ui.component.util.interfaces

import net.prismclient.aether.ui.component.UIComponent

/**
 * [UILayout] is an interface to define a Layout component
 * which has the ability to add and remove components from it.
 *
 * @author sen
 * @since 5/1/2022
 */
interface UILayout {
    /**
     * Adds a [UIComponent] to the Layout
     */
    fun addComponent(component: UIComponent<*>)

    fun removeComponent(component: UIComponent<*>)
}