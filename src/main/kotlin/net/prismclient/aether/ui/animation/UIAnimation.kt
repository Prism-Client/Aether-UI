package net.prismclient.aether.ui.animation

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.INITIAL
import net.prismclient.aether.ui.util.extensions.isNormal
import net.prismclient.aether.ui.util.interfaces.UICopy
import java.util.function.Consumer

/**
 * [UIAnimation] has a sequence of [UIStyleSheet] and other data pertaining to the Animation
 * (such as easing). An animation consists of at least two keyframes.
 * For example if you set a keyframe at 0, and at 100, and set the
 * keyframe at 100 to a Quad Ease, the and the position from (0,0) -> (100, 0)
 * the component will animate with a quadratic ease from the property difference.
 * In essence, the following keyframe defines the ease properties.
 *
 * If there is only one keyframe, then a blank keyframe will be inserted into the first index
 *
 * @author sen
 * @since 3/5/2022
 *
 * @param style An instance of [T], a [UIStyleSheet] which is copied to create new keyframes. Any properties set to it
 * will be copied to the new keyframes.
 */
@Suppress("UNCHECKED_CAST")
class UIAnimation<T : UIStyleSheet>(
        var style: T,
        val name: String,
        var priority: UIAnimationPriority = UIAnimationPriority.NORMAL
) : UICopy<UIAnimation<T>> { // TODO: update cache property
    val lifetime = System.currentTimeMillis()
    val timeline: ArrayList<T> = ArrayList()

    protected lateinit var component: UIComponent<*>
    protected var activeKeyframe: T? = null
    protected var nextKeyframe: T? = null
    protected var nextKeyframeIndex: Int = 0
    var onCreationListeners: MutableList<Consumer<UIAnimation<T>>>? = null
    var onCompletionListeners: MutableList<Consumer<UIAnimation<T>>>? = null

    var animationLength: Long = 0L
        protected set
    var animating: Boolean = false
        protected set
    var completed: Boolean = false
        protected set

    fun start(component: UIComponent<*>) {
        this.component = component

        if (timeline.size < 1) {
            println("Timeline must have at least 1 keyframe to start an animation")
            return
        } else if (timeline.size == 1) {
            timeline.add(timeline[0])
            timeline[0] = style.copy() as T
        }

        animationLength = 0L
        animating = true
        completed = false

        for (keyframe in timeline) {
            if (keyframe.ease == null)
                keyframe.ease = UILinear()
            animationLength += keyframe.ease!!.duration
        }

        onCreationListeners?.forEach { it.accept(this) }
    }

    fun pause() {
        TODO("Pausing of animations have not yet been implemented")
    }

    fun stop() {
        TODO("Stopping of animations have not yet been implemented")
    }

    /**
     * Updates the animation cache on the offhand chance that the window is resized during
     * the animation
     */
    fun updateCache() {
        component.style.updateAnimationCache(component)
    }

    /**
     * Clears the animation cache. It is automatically invoked when the animation is completed.
     */
    fun clearCache() {
        component.style.clearAnimationCache()
    }

    /**
     * Invoked on the screen render. The animation is updated here.
     */
    fun update() {
        if (completed || !animating)
            return
        if (!this::component.isInitialized)
            throw RuntimeException("Component cannot be null for animation")

        if (activeKeyframe == null) {
            activeKeyframe = timeline[0]
            nextKeyframe = timeline[1]
            nextKeyframe!!.ease!!.start()
            nextKeyframeIndex = 1
        }

        if (nextKeyframe == null) {
            return
        } else {
            // If current animation past the current time
            if (nextKeyframe!!.ease!!.endTime <= System.currentTimeMillis()) {
                if (timeline.size > ++nextKeyframeIndex) {
                    swapKeyframe(timeline[nextKeyframeIndex])
                } else {
                    // Next keyframe is null, so finish the animation
                    completeAnimation()
                    return
                }
            }
        }
        component.update()
        component.style.animate(activeKeyframe, nextKeyframe, nextKeyframe!!.ease!!.getValue().toFloat(), component)
        component.updateBounds()
        component.updateStyle()
    }

    fun swapKeyframe(next: T) {
        activeKeyframe = nextKeyframe
        nextKeyframe = next
        nextKeyframe!!.ease!!.start()
        saveState(activeKeyframe!!)
    }

    fun saveState(keyframe: T) {
        component.style.saveState(component, keyframe, (keyframe.animationResult != UIAnimationResult.Reset))
    }

    fun forceComplete() {
        println("Forced animation completion.")
        completeAnimation()
    }

    fun completeAnimation() {
        completed = true
        animating = false

        saveState(timeline[timeline.size - 1])

        // Clear the cache after the state has been saved (if it needs to be saved)
        clearCache()

        UIProvider.completeAnimation(this)

        // Invoke the listeners that the animation has been completed
        onCompletionListeners?.forEach { it.accept(this) }
    }

    /**
     * Creates a keyframe at the specified position (0.0-100.0)
     *
     * @param block The [UIStyleSheet] that adjusts the component's properties.
     */
    inline fun keyframe(ease: UIEase = UILinear(1000L), keep: Boolean = true, block: T.() -> Unit): T {
        val sheet: T
        try {
            sheet = style.copy() as T
        } catch (e: Exception) {
            throw UIComponent.InvalidStyleSheetException("anim-$name-${timeline.size}", null)
        }
        if (animating)
            throw RuntimeException("Cannot add keyframe while animating")
        timeline.add(sheet)
        sheet.name = "anim-$name-${timeline.size}"
        sheet.ease = ease
        sheet.animationResult = if (keep) UIAnimationResult.Retain else UIAnimationResult.Reset
        sheet.block()

        return sheet
    }

    protected fun UIUnit?.updateX(x: Float): Float {
        if (this == null)
            return x
        if (this.type.isNormal())
            return component.calculateUnitX(this, component.getParentWidth(), false)
        return when (this.type) {
            INITIAL -> x
            else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
        }
    }

    protected fun UIUnit?.updateY(y: Float): Float {
        if (this == null)
            return y
        if (this.type.isNormal())
            return component.calculateUnitY(this, component.getParentHeight(), false)
        return when (this.type) {
            INITIAL -> y
            else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
        }
    }

    /**
     * Returns null if type is INITIAL, as a component cannot have it set to that type
     */
    protected operator fun UIUnit?.unaryPlus(): UIUnit? =
            if (this?.type == INITIAL) null else this

    fun first(event: Consumer<UIAnimation<T>>) {
        if (onCreationListeners == null)
            onCreationListeners = mutableListOf()
        onCreationListeners!!.add(event)
    }

    /**
     * Adds an event which is invoked when the animation is completed
     */
    fun then(event: Consumer<UIAnimation<T>>) {
        if (onCompletionListeners == null)
            onCompletionListeners = mutableListOf()
        onCompletionListeners!!.add(event)
    }

    protected fun apply(animation: UIAnimation<T>) {
        this.onCompletionListeners = animation.onCompletionListeners
    }

    override fun copy(): UIAnimation<T> = UIAnimation(style, name, priority).also {
        it.apply(this)
        for (animation in timeline)
            it.timeline.add(animation.copy() as T)
    }
}