package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UILabel] is a component which draws a label, or string on screen.
 *
 * @author sen
 * @since 5/15/2022
 */
class UILabel(var text: String, style: String) : UIComponent<UIStyleSheet>(style) {
    override fun updateSize() {
        super.updateSize()
        // Render the font after updating the size to ensure that the
        // text width and height are calculated before the frame render
//        style.font?.update(this)
//        style.font?.render(text)
//        style.font?.update(this)
    }

    override fun updateStyle() {}

    override fun renderComponent() {
        style.font?.render(text)
    }
}