package net.prismclient.aether.ui.animation.impl

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.animation.UIAnimationPriority
import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.style.impl.animation.UIAnimationSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIRelativeUnit
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

    protected var background: UIBackground? = null

    open fun updateBackground(prog: Float, c: UIComponent<*>, p: UIAnimationSheet, ap: UIAnimationSheet) {
        if (c.style.background != null) {
            if (background == null)
                background = c.style.background!!.copy()

            c.style.background!!.color = transition(
                (ap.background?.color ?: background!!.color),
                (p.background?.color ?: background!!.color),
                prog
            )

            if (ap.background?.border != null || p.background?.border != null) {
                if (background == null) {
                    c.style.background = UIBackground()
                    background = c.style.background
                }
                if (background!!.border == null) {
                    background!!.border = UIBorder()
                    c.style.background!!.border = background!!.border
                }
                c.style.background!!.border!!.borderColor = transition(
                    (ap.background?.border?.borderColor ?: background!!.border!!.borderColor),
                    (p.background?.border?.borderColor ?: background!!.border!!.borderColor),
                    prog
                )
                c.style.background!!.border!!.borderWidth =
                    (ap.background?.border?.borderWidth
                        ?: background!!.border!!.borderWidth) + (((p.background?.border?.borderWidth
                        ?: background!!.border!!.borderWidth) - (ap.background?.border?.borderWidth
                        ?: background!!.border!!.borderWidth)) * prog)
            }
        }
    }

    open fun updateFont(prog: Float, c: UIComponent<*>, p: UIAnimationSheet, ap: UIAnimationSheet) {

    }

    override fun saveState(k: UIAnimationSheet) {
        if (k.animationResult == UIAnimationResult.Reset) {
            component.update()

            if (background != null)
                component.style.background = background
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
        keyframe(block).also {
            it.ease = ease; it.ease.delay = delay; it.animationResult =
            if (keep) UIAnimationResult.Retain else UIAnimationResult.Reset
        }

    override fun copy(): UIAnimation<UIAnimationSheet> = UIDefaultAnimation(name, priority).also {
        it.apply(this)
        for (animation in timeline)
            it.timeline.add(animation.copy())
    }

    override fun getStyle(): UIAnimationSheet = UIAnimationSheet()
}