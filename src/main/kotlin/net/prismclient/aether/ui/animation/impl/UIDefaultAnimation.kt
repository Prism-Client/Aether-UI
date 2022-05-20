package net.prismclient.aether.ui.animation.impl

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.impl.animation.UIAnimationSheet
import net.prismclient.aether.ui.util.UIAnimationPriority
import net.prismclient.aether.ui.util.extensions.transition

/**
 * [UIDefaultAnimation] is the default implementation of [UIAnimation]. It supports a generic
 * style sheet
 *
 * @author sen
 * @since 9/5/2022
 */
open class UIDefaultAnimation(
    name: String,
    priority: UIAnimationPriority = UIAnimationPriority.NORMAL
) : UIAnimation<UIAnimationSheet>(name, priority) {
    override fun updateKeyframes() {
        val c = this.component
        val p = nextKeyframe!!
        val ap = activeKeyframe!!
        val prog = nextKeyframe!!.ease.getValue().toFloat()
        updatePosition(prog, c, p, ap)
        updateSize(prog, c, p, ap)
        updateBackground(prog, c, p, ap)
        updateFont(prog, c, p, ap)
    }

    open fun updatePosition(prog: Float, c: UIComponent<*>, p: UIAnimationSheet, ap: UIAnimationSheet) {
        c.x = ap.x.updateX(c.x) + ((p.x.updateX(c.x) - ap.x.updateX(c.x)) * prog) + c.getParentX()
        c.y = ap.y.updateY(c.y) + ((p.y.updateY(c.y) - ap.y.updateY(c.y)) * prog) + c.getParentY()
    }

    open fun updateSize(prog: Float, c: UIComponent<*>, p: UIAnimationSheet, ap: UIAnimationSheet) {
        c.width = ap.width.updateX(c.width) + ((p.width.updateX(c.width) - ap.width.updateX(c.width)) * prog)
        c.height = ap.height.updateY(c.height) + ((p.height.updateY(c.height) - ap.height.updateY(c.height)) * prog)
    }

    private var cachedBackground = false
    private var cachedBackgroundColor: Int = 0

    open fun updateBackground(prog: Float, c: UIComponent<*>, p: UIAnimationSheet, ap: UIAnimationSheet) {
        if (c.style.background != null) { // TODO: Need to cache the initial color(s)
            if (!cachedBackground) {
                cachedBackgroundColor = c.style.background!!.color
                cachedBackground = true
            }
            c.style.background!!.color = transition(
                (ap.background?.color ?: cachedBackgroundColor),
                (p.background?.color ?: cachedBackgroundColor),
                prog
            )
        }
    }

    open fun updateFont(prog: Float, c: UIComponent<*>, p: UIAnimationSheet, ap: UIAnimationSheet) {

    }

    override fun saveState(k: UIAnimationSheet) {
        if (k.animationResult == UIAnimationResult.Reset) {
            component.update()

            if (cachedBackground) {
                component.style.background!!.color = cachedBackgroundColor!!
            }
        } else {
            val s = component.style
            s.x = +k.x ?: s.x
            s.y = +k.y ?: s.y
            s.width = +k.width ?: s.width
            s.height = +k.height ?: s.height
            component.update()
        }
    }

    inline fun keyframe(block: UIAnimationSheet.() -> Unit = {}): UIAnimationSheet =
            keyframe(UIAnimationSheet(), block)

    inline fun keyframe(ease: UIEase, block: UIAnimationSheet.() -> Unit = {}) =
            keyframe(block).also { it.ease = ease }

    inline fun keyframe(ease: UIEase, delay: Long, block: UIAnimationSheet.() -> Unit = {}) =
            keyframe(block).also { it.ease = ease; it.ease.delay = delay }

    inline fun keyframe(ease: UIEase, keep: Boolean, block: UIAnimationSheet.() -> Unit = {}) =
            keyframe(ease, keep, 0L, block)

    inline fun keyframe(ease: UIEase, keep: Boolean, delay: Long, block: UIAnimationSheet.() -> Unit = {}) =
            keyframe(block).also { it.ease = ease; it.ease.delay = delay; it.keep() }

    override fun copy(): UIAnimation<UIAnimationSheet> = UIDefaultAnimation(name, priority).also {
        it.apply(this)
        for (animation in timeline)
            it.timeline.add(animation.copy())
    }

    override fun getStyle(): UIAnimationSheet = UIAnimationSheet()
}