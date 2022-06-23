package net.prismclient.aether.ui.component.type.other.progress

import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIProgressSheet] is the corresponding sheet to [UIProgress].
 *
 * @author sen
 * @since 6/23/2022
 */
class UIProgressSheet : UIStyleSheet() {
    /**
     * The color of the progress
     */
    var progressColor = -1

    override fun copy() = UIProgressSheet().also {
        it.apply(this)
        it.progressColor = progressColor
    }
}