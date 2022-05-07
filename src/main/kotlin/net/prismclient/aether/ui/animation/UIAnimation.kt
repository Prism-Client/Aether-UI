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
    val timeline: ArrayList<UIKeyframe> = ArrayList()

    private var component: UIComponent<*>? = null
    private var activeKeyframe: UIKeyframe? = null
    private var nextKeyframe: UIKeyframe? = null
    private var nextKeyframeIndex: Int = 0

    lateinit var time: UILinear
        private set
    private var animation: UIAnimation? = null
    var animationLength: Long = 0L
        private set

    fun start(component: UIComponent<*>) {
        this.component = component

        if (timeline.size < 1) {
            println("Timeline must have at least 1 keyframe to start an animation")
            return
        }

        timeline.sort()
        time = UILinear(animationLength)
        time.start()
    }

    fun pause() {

    }

    fun stop() {

    }

    fun update() {
        if (component == null) {
            throw RuntimeException("Component cannot be null for animation")
        }

        if (activeKeyframe == null) {
            activeKeyframe = timeline[0]
            nextKeyframe = if (timeline.size > 1) return else timeline[1]
            nextKeyframe!!.property.ease.start()
        }

        if (activeKeyframe == null || nextKeyframe == null) {
            println("null")
            return
        }

        val position = nextKeyframe!!.property.ease.getValue()

        println(position)
    }

    fun searchForKeyframe(position: Float, start: Int, end: Int): Int {
        val mid = (start + end) / 2
        if (timeline[mid].position == position)
            return mid
        if (start == end - 1)
            return if (timeline[end].position - position >= timeline[start].position - position) start else end
        return if (timeline[mid].position > position) searchForKeyframe(position, start, mid) else searchForKeyframe(position, mid, end)
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
        return UIKeyframe(position, sheet).also { timeline.add(it) }
    }

    /**
     * Creates a Keyframe at position 0 of the animation
     *
     * @param block The [UIStyleSheet] that adjusts the component's properties.
     */
    inline fun from(block: UIStyleSheet.() -> Unit) =
            keyframe(0f, block)

    /**
     * Creates a Keyframe at position 100 of the animation
     *
     * @param block The [UIStyleSheet] that adjusts the component's properties.
     */
    inline fun to(block: UIStyleSheet.() -> Unit) =
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