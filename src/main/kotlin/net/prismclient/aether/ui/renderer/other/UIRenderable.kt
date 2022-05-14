package net.prismclient.aether.ui.renderer.other

import net.prismclient.aether.ui.component.UIComponent

/**
 * [UIRenderable]
 *
 * @author sen
 * @since 5/13/2022
 */
interface UIRenderable {
    fun update(component: UIComponent<*>)

    fun render()
}