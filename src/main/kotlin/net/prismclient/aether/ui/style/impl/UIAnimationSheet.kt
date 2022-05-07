package net.prismclient.aether.ui.style.impl

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIAnimationSheet] is a normal style sheet with an ease property.
 *
 * @author sen
 * @since 5/5/2022
 */
class UIAnimationSheet() : UIStyleSheet() {
    val ease: UIEase by lazy { UILinear() }

    override fun copy(): UIStyleSheet = UIAnimationSheet().also {
        it.apply(this)
//        it.ease = ease.copy()
    }
}