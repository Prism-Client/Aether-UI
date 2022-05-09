package net.prismclient.aether.ui.animation

import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.animation.keyframe.UIKeyframe
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.impl.UIAnimationSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.*
import net.prismclient.aether.ui.util.UIAnimationPriority
import net.prismclient.aether.ui.util.extensions.isNormal

/**
 * [UIAnimation] has a sequence of [UIStyleSheet] and other data pertaining to the Animation
 * (such as easing). An animation consists of at least two keyframes.
 * For example if you set a keyframe at 0, and at 100, and set the
 * keyframe at 100 to a Quad Ease, the and the position from (0,0) -> (100, 0)
 * the component will animate with a quadratic ease from the property difference.
 * In essence, the following keyframe defines the ease properties.
 *
 * @author sen
 * @since 3/5/2022
 * @see UIKeyframe
 */
class UIAnimation(val name: String, var priority: UIAnimationPriority = UIAnimationPriority.NORMAL) {
    val timeline: ArrayList<UIAnimationSheet> = ArrayList()

    private lateinit var component: UIComponent<*>
    private var activeKeyframe: UIAnimationSheet? = null
    private var nextKeyframe: UIAnimationSheet? = null
    private var nextKeyframeIndex: Int = 0

    var animationLength: Long = 0L
        private set
    var animating: Boolean = false
        private set
    var completed: Boolean = false
        private set

    fun start(component: UIComponent<*>) {
        this.component = component

        if (timeline.size < 1) {
            println("Timeline must have at least 1 keyframe to start an animation")
            return
        }

        animationLength = 0L
        animating = true
        completed = false

        for (keyframe in timeline) {
            if (!keyframe.isEaseInitialized())
                keyframe.ease = UILinear()
            animationLength += keyframe.ease.duration
        }
    }

    fun pause() {

    }

    fun stop() {

    }

    fun update() {
        if (completed || !animating)
            return
        if (!this::component.isInitialized)
            throw RuntimeException("Component cannot be null for animation")

        if (activeKeyframe == null) {
            activeKeyframe = timeline[0]
            nextKeyframe = timeline[1]
            nextKeyframe!!.ease.start()
            nextKeyframeIndex = 1
        }

        if (nextKeyframe == null) {
            return
        } else {
            // If current animation past the current time
            if (nextKeyframe!!.ease.endTime <= System.currentTimeMillis()) {
                if (timeline.size > ++nextKeyframeIndex) {
                    swapKeyframe(timeline[nextKeyframeIndex])
                } else {
                    // Next keyframe is null, so finish the animation
                    completeAnimation()
                    return
                }
            }
        }

        val c = this.component
        val p = nextKeyframe!!
        val ap = activeKeyframe!!
        val prog = p.ease.getValue().toFloat()

        c.update()

        p.x?.let { c.x = ap.x.updateX(c.x) + ((p.x.updateX(c.x) - ap.x.updateX(c.x)) * prog) + c.getParentX() }
        p.y?.let { c.y = ap.y.updateY(c.y) + ((p.y.updateY(c.y) - ap.y.updateY(c.y)) * prog) + c.getParentY() }
        p.width?.let { c.width = ap.width.updateX(c.width) + ((p.width.updateX(c.width) - ap.width.updateX(c.width)) * prog) }
        p.height?.let { c.height = ap.height.updateY(c.height) + ((p.height.updateY(c.height) - ap.height.updateY(c.height)) * prog)}

        c.updateBounds()
        c.updateStyle()
    }

    fun swapKeyframe(next: UIAnimationSheet) {
        activeKeyframe = nextKeyframe
        nextKeyframe = next
        nextKeyframe!!.ease.start()
        saveState(activeKeyframe!!)
    }

    fun saveState(k: UIAnimationSheet) {
        if (k.animationResult == UIAnimationSheet.AnimationResult.Reset) {
            component.update()
        } else {
            val s = component.style
            s.x = +k.x ?: s.x
            s.y = +k.y ?: s.y
            s.width = +k.width ?: s.width
            s.height = +k.height ?: s.height
        }
    }

    fun completeAnimation() {
        println("Completed animation: $name, Component: $component")
        completed = true
        animating = false

        saveState(nextKeyframe!!)
    }

    /**
     * Creates a keyframe at the specified position (0.0-100.0)
     *
     * @param block The [UIStyleSheet] that adjusts the component's properties.
     */
    inline fun keyframe(block: UIAnimationSheet.() -> Unit): UIAnimationSheet {
        // Check if the animation is active
        if (animating)
            throw RuntimeException("Cannot add keyframe while animating")
        val sheet = UIAnimationSheet()
        timeline.add(sheet)
        sheet.name = "anim-$name-${timeline.size}"
        sheet.block()

        return sheet
    }

    private fun UIUnit?.updateX(x: Float): Float {
        if (this == null)
            return x
        if (this.type.isNormal())
            return component.calculateUnitX(this, component.getParentWidth(), false)
        return when (this.type) {
            INITIAL -> x
            PXANIMRELATIVE -> x + this.value
            RELANIMRELATIVE -> x + component.getParentWidth() * this.value
            else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
        }
    }

    private fun UIUnit?.updateY(y: Float): Float {
        if (this == null)
            return y
        if (this.type.isNormal())
            return component.calculateUnitY(this, component.getParentHeight(), false)
        return when (this.type) {
            INITIAL -> y
            PXANIMRELATIVE -> y + this.value
            RELANIMRELATIVE -> y + component.getParentHeight() * this.value
            else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
        }
    }

    /**
     * Returns null if type is INITIAL, as a component cannot have it set to that type
     */
    private operator fun UIUnit?.unaryPlus(): UIUnit? =
            if (this?.type == INITIAL) null else this
}