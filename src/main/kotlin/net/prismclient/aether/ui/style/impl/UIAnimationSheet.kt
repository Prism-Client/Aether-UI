package net.prismclient.aether.ui.style.impl

import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.UIAnimation

/**
 * [UIAnimationSheet] is a normal style, but used to define a [UIAnimation]
 *
 * @author sen
 * @since 5/5/2022
 */
class UIAnimationSheet : UIStyleSheet() {
    var ease: UIEase? = null
    var animationResult: AnimationResult = AnimationResult.Retain

    override fun copy(): UIStyleSheet = UIAnimationSheet().also {
        it.apply(this)
        it.ease = ease?.copy()
    }

    /**
     * Defines what should happen after the animation is completed.
     *
     * @author sen
     * @since 7/5/2022
     */
    enum class AnimationResult {
        Retain,
        Reset
    }
}