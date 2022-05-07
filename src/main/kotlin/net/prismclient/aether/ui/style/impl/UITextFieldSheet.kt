package net.prismclient.aether.ui.style.impl

import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.EM
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.px

class UITextFieldSheet : UIStyleSheet() {
    /**
     * The text color when there is text inside the field
     */
    var activeColor = asRGBA(255, 255, 255)

    /**
     * The text color when the placeholder text is rendered
     */
    var placeholderColor = asRGBA(255, 255, 255, 0.8f)

    /**
     * The color of the line that shows where you are at in a text field
     */
    var caretColor = asRGBA(255, 255, 255)
    var caretX: UIUnit? = null
    var caretY: UIUnit? = null
    var caretWidth: UIUnit? = px(2)
    var caretHeight: UIUnit? = UIUnit(1f, EM)
    var caretRadius: UIRadius? = null

    /**
     * The color of the selection
     */
    var selectionColor = asRGBA(137, 207, 240, 0.3f)


    override fun copy(): UIStyleSheet {
        val it = UITextFieldSheet()

        it.apply(this)

        it.caretColor = caretColor
        it.caretX = caretX
        it.caretY = caretY
        it.caretWidth = caretWidth
        it.caretHeight = caretHeight
        it.caretRadius = caretRadius
        it.selectionColor = selectionColor

        return it
    }
}