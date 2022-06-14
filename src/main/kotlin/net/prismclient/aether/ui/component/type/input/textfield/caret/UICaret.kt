package net.prismclient.aether.ui.component.type.input.textfield.caret

import net.prismclient.aether.ui.shape.type.UIRectangle

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

    override fun copy(): UICaret = UICaret().also {
        it.apply(this)
        it.radius = radius?.copy()
        it.offsetX = offsetX
        it.offsetY = offsetY
    }
}