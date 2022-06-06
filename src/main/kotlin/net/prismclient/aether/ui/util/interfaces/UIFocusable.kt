package net.prismclient.aether.ui.util.interfaces

import net.prismclient.aether.ui.component.UIComponent

/**
 * [UIFocusable] is an interface which allows a [UIComponent] to be focusable. Only one singular
 * component can be focused at once. This can be useful for components such as text fields, where
 * you only want one component to be focused at once.
 *
 * @author sen
 * @since 6/1/2022
 * @param T The type of component which is focusable.
 */
interface UIFocusable<T : UIComponent<*>>