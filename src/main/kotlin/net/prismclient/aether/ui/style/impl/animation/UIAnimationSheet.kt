package net.prismclient.aether.ui.style.impl.animation

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.animation.util.UIIEase
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * The implementation for [UIStyleSheet] for animations.
 */
class UIAnimationSheet : UIStyleSheet(), UIIEase {
    override var ease: UIEase = UILinear()
    override var animationResult: UIAnimationResult = UIAnimationResult.Reset

    /**
     * Shorthand for setting the [animationResult] to Retain
     */
    fun keep() {
        animationResult = UIAnimationResult.Retain
    }

    override fun copy(): UIAnimationSheet {
        val sheet = UIAnimationSheet()

        sheet.apply(this)
        sheet.ease = ease.copy()
        sheet.animationResult = animationResult

        return sheet
    }
}