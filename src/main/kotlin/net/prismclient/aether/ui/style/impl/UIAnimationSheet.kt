package net.prismclient.aether.ui.style.impl

import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.animation.ease.impl.UILinear

/**
 * [UIAnimationSheet] is a normal style, but used to define a [UIAnimation]
 *
 * @author sen
 * @since 5/5/2022
 */
class UIAnimationSheet : UIStyleSheet() {
    lateinit var ease: UIEase
    var animationResult: AnimationResult = AnimationResult.Retain

    fun isEaseInitialized(): Boolean = this::ease.isInitialized

    override fun copy(): UIStyleSheet = UIAnimationSheet().also {
        if (!isEaseInitialized())
            ease = UILinear()
        it.apply(this)
        it.ease = ease.copy()
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