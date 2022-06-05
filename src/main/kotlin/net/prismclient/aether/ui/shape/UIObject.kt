package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.component.UIComponent

/**
 * [UIObject] is a base class for all non-component objects to render
 * on screen. It is the superclass for [UIShape] which allows for general
 * things to be rendered on screen.
 *
 * @author sen
 * @since 5/4/2022
 */
abstract class UIObject {
    lateinit var component: UIComponent<*>

    /**
     * Invoked within the component when it is updated
     */
    abstract fun update(component: UIComponent<*>)

    /**
     * Invoked when the shape should be rendered. All
     * rendering code should  be placed within this function
     */
    abstract fun render()
}