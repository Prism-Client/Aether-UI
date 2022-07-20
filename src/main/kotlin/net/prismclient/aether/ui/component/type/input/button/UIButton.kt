package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIButton] is the default implementation of [UIComponent]. It renders the given text to the font.
 *
 * @author sen
 * @since 1.0
 */
open class UIButton(text: String) : UIComponent<UIStyleSheet>() {
    open var text: String = text
        set(value) {
            field = value
            style.font?.actualText = text
            updateFont()
        }

    override fun update() {
        super.update()
        style.font?.actualText = text
        updateFont()
    }

    override fun renderComponent() {
        style.font?.render()
    }

    fun updateFont() {
        width = width.coerceAtLeast(style.font?.cachedWidth ?: 0f)
        height = height.coerceAtLeast(style.font?.cachedHeight ?: 0f)
        updateAnchorPoint()
        updatePosition()
        updateBounds()
        updateStyle()
        style.font?.updateFont()
    }

    override fun createsStyle(): UIStyleSheet = UIStyleSheet()
}