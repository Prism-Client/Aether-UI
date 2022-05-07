package net.prismclient.aether.ui.animation

import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.animation.keyframe.UIKeyframe
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.impl.UIAnimationSheet
import net.prismclient.aether.ui.util.UIAnimationPriority

/**
 * [UIAnimation] has a sequence of [UIKeyframe] which holds
 * a [UIStyleSheet] and other data pertaining to the Animation
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
class UIAnimation(val name: String, var priority: UIAnimationPriority = UIAnimationPriority.NORMAL) { // TODO: Ease should be in the keyframe
    val timeline: ArrayList<UIKeyframe> = ArrayList() // TODO: Get rid of UIKeyframe lmfao

    private lateinit var component: UIComponent<*>
    private var activeKeyframe: UIKeyframe? = null
    private var nextKeyframe: UIKeyframe? = null
    private var nextKeyframeIndex: Int = 0

    private var animation: UIAnimation? = null
    var animationLength: Long = 0L
        private set

    fun start(component: UIComponent<*>) {
        this.component = component

        if (timeline.size < 1) {
            println("Timeline must have at least 1 keyframe to start an animation")
            return
        }

        animationLength = 0L
        for (keyframe in timeline) {
            if (keyframe.property.ease == null)
                keyframe.property.ease = UILinear()
            animationLength += keyframe.property.ease!!.duration
        }
    }

    fun pause() {

    }

    fun stop() {

    }

    fun update() {
        if (!this::component.isInitialized) {
            throw RuntimeException("Component cannot be null for animation")
        }

        if (activeKeyframe == null) {
            activeKeyframe = timeline[0]
            nextKeyframe = if (timeline.size < 1) {
                return
            } else {
                timeline[1]
            }
            nextKeyframe!!.property.ease!!.start()
            nextKeyframeIndex = 1
        }

        if (nextKeyframe == null) {
            println("null")
            return
        } else {
            // If current animation past the current time
            if (nextKeyframe!!.property.ease!!.endTime <= System.currentTimeMillis()) {
                if (timeline.size > ++nextKeyframeIndex) {
                    activeKeyframe = nextKeyframe
                    nextKeyframe = timeline[nextKeyframeIndex]
                    nextKeyframe!!.property.ease!!.start()
                } else {
                    return // Next keyframe is null
                }
            }
        }

        val component = this.component!!
        val akf = activeKeyframe!!
        val kf = nextKeyframe!!
        val p = kf.property
        val ap = akf.property
        val position = kf.property.ease!!.getValue().toFloat()


        if (p.x != null) {
            component.relX = ((p.x!!.value * position) + (ap.x?.value ?: 0f)) + component.getParentX()
            println(component.relX)
        }
    }

    /**
     * Returns true if the animation is active
     */
    fun isAnimating(): Boolean = false //this::ease.isInitialized && this.ease.animating

    /**
     * Creates a keyframe at the specified position (0.0-100.0)
     *
     * @param block The [UIStyleSheet] that adjusts the component's properties.
     */
    inline fun keyframe(position: Float, block: UIAnimationSheet.() -> Unit): UIKeyframe {
        // Check if the animation is active
        if (isAnimating()) {
            throw RuntimeException("Cannot add keyframe while animating")
        }

        val sheet = UIAnimationSheet()
        sheet.name = "anim-$name-$position"
        sheet.block()

        return UIKeyframe(sheet).also { timeline.add(it) }
    }

    /**
     * Creates a Keyframe at position 0 of the animation
     *
     * @param block The [UIStyleSheet] that adjusts the component's properties.
     */
    inline fun from(block: UIAnimationSheet.() -> Unit) =
            keyframe(0f, block)

    /**
     * Creates a Keyframe at position 100 of the animation
     *
     * @param block The [UIStyleSheet] that adjusts the component's properties.
     */
    inline fun to(block: UIAnimationSheet.() -> Unit) =
            keyframe(100f, block)

    //    fun update() {
//        if (component == null) {
//            println("Cannot update component if null")
//            return
//        }
//
//        val animationPosition = time.getValue().toFloat() * 100f
//
//        // Find the nearest keyframe if null
//        if (activeKeyframe == null) {
//            // Use a binary sort algorithm to find the closest keyframe to the provided position
//            val index = searchForKeyframe(animationPosition, 0, timeline.size)
//            activeKeyframe = timeline[index]
//
//
//            // check if the position is greater than the keyframe
//            if (activeKeyframe!!.position > animationPosition)
//                return
//            // Get the next keyframe or return if the next keyframe is null, as there is nothing to animate to
//            nextKeyframe = if (index + 1 >= timeline.size) return else timeline[index + 1]
//            nextKeyframeIndex = index + 1
//        }
//
////        if (nextKeyframe!!.position <= position && position != 100f) {
////            // Increment the keyframes by 1 if the position surpasses the next keyframe
////            activeKeyframe = nextKeyframe
////            nextKeyframe = if (nextKeyframeIndex + 1 > timeline.size) return else timeline[nextKeyframeIndex + 1]
////        }
//    }

}