package net.prismclient.aether.ui.animation.impl

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.style.impl.animation.UIAnimationSheet
import net.prismclient.aether.ui.util.UIAnimationPriority

/**
 * [UIDefaultAnimation] is the default implementation of [UIAnimation]. It supports a generic
 * style sheet
 *
 * @author sen
 * @since 9/5/2022
 */
class UIDefaultAnimation(
    name: String,
    priority: UIAnimationPriority = UIAnimationPriority.NORMAL
) : UIAnimation<UIAnimationSheet>(name, priority) {
    override fun updateKeyframes() {
        val c = this.component
        val p = nextKeyframe!!
        val ap = activeKeyframe!!
        val prog = p.ease.getValue().toFloat()
        c.x = ap.x.updateX(c.x) + ((p.x.updateX(c.x) - ap.x.updateX(c.x)) * prog) + c.getParentX()
        c.y = ap.y.updateY(c.y) + ((p.y.updateY(c.y) - ap.y.updateY(c.y)) * prog) + c.getParentY()
        c.width = ap.width.updateX(c.width) + ((p.width.updateX(c.width) - ap.width.updateX(c.width)) * prog)
        c.height = ap.height.updateY(c.height) + ((p.height.updateY(c.height) - ap.height.updateY(c.height)) * prog)
    }

    inline fun keyframe(block: UIAnimationSheet.() -> Unit = {}): UIAnimationSheet =
            keyframe(UIAnimationSheet(), block)

    inline fun keyframe(ease: UIEase, block: UIAnimationSheet.() -> Unit = {}) =
            keyframe(block).also { it.ease = ease }

    inline fun keyframe(ease: UIEase, delay: Long, block: UIAnimationSheet.() -> Unit = {}) =
            keyframe(block).also { it.ease = ease }.also { it.ease.delay = delay }

    override fun copy(): UIAnimation<UIAnimationSheet> = UIDefaultAnimation(name, priority).also {
        for (animation in timeline)
            it.timeline.add(animation)
    }
}