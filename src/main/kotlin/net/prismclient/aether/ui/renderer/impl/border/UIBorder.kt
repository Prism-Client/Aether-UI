package net.prismclient.aether.ui.renderer.impl.border

import net.prismclient.aether.ui.util.UICopy

/**
 * [UIBorder] represents a border renderer
 *
 * @author sen
 * @since 4/26/2022
 */
open class UIBorder : UICopy<UIBorder> {
    var borderWidth = 0f
    var borderColor = 0
    // TODO: border-style

    open fun render(x: Float, y: Float, width: Float, height: Float) {
        TODO("Not yet implemented")
    }

    override fun copy(): UIBorder = UIBorder().also {
        it.borderWidth = borderWidth
        it.borderColor = borderColor
    }
}