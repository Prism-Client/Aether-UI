package net.prismclient.aether.ui.component.type.input.textfield.caret

import net.prismclient.aether.ui.shape.type.UIRectangle
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UICaret], also known as a text cursor, is a vertical line which displays the index
 * that the text field is at when input is given.
 *
 * @author sen
 * @since 5/11/2022
 */
open class UICaret : UIRectangle() {
    var offsetX = 0f
    var offsetY = 0f

    override fun render() {
        renderer {
            color(color)
            rect(cachedX + offsetX, cachedY + offsetY, cachedWidth, cachedHeight, radius)
        }
    }

    override fun copy(): UICaret = UICaret().also {
        it.apply(this)
        it.radius = radius?.copy()
    }
}