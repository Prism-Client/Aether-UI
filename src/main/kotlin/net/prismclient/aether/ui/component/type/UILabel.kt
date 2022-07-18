package net.prismclient.aether.ui.component.type

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UILabel] acts nearly identical to [UIButton] however, the size of the label is automatically
 * updated to be at least the size of the font within it.
 *
 * @author sen
 * @since 1.0
 */
class UILabel(text: String) : UIComponent<UIStyleSheet>() {
    var text: String = text
        set(value) {
            field = value
            style.font?.actualText = text
        }

    override fun update() {
        super.update()
        style.font?.actualText = text
        width = width.coerceAtLeast(style.font?.cachedWidth ?: 0f)
        height = height.coerceAtLeast(style.font?.cachedHeight ?: 0f)
        updateAnchorPoint()
        updatePosition()
        updateBounds()
        updateStyle()
        style.font?.updateFont()
    }


    override fun renderComponent() {
        style.font?.render()
    }

    override fun createsStyle(): UIStyleSheet = UIStyleSheet()
}