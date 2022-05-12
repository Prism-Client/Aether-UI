package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIWindow] is a "viewport" for components. The window holds a list of
 * children which are rendered. It is the superclass for components like
 * layouts, such as a list, or grid layout. If you are looking for simply
 * a window that does not control the layout of it's components, take a look
 * at [UIFrame].
 *
 * [UIWindow] works by rendering all the content to an FBO if content clipping is
 * enabled. If disabled, everything is rendered when the render method for this
 * component is invoked.
 *
 * Because of how [UIFrame] is designed, more custom rendering features can be applied
 * (such as shaders), by extending the class and applying your own code.
 *
 * @author sen
 * @since 5/12/2022
 *
 * @see UIFrame
 */
abstract class UIWindow<T : UIStyleSheet>(style: String) : UIComponent<T>(style) {

}