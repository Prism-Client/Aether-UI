package net.prismclient.aether.ui.animation

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIAnimation] is the core handler for all animations. The animation is created
 * with a set of keyframes, nd when the component is in need of this animation the
 * animation is copied and given to the component. From there, the component is updated
 * and the properties of the style sheet and some properties of the component are updated.
 *
 * The ease of the next keyframe dictates the speed (ease) of the animation. If a singular
 * keyframe is given, the animation will automatically allocate a default animation before
 * the keyframe.
 *
 * When ran, a copy of this is assigned to the component. Everything except the actual keyframes
 * are copied. If any styles are changed at that point, it will change the original animation keyframes.
 *
 * @author sen
 * @since 1.0
 *
 * @param style The style sheet of the component. The style should be completely new, with no styles changed.
 */
class UIAnimation<C : UIComponent<S>, S : UIStyleSheet>(val style: S): UICopy<UIAnimation<C, S>> {
    /**
     * The component that this animation is attached to
     */
    lateinit var component: C
        private set

    /**
     * The keyframes of the animation where the ease and
     * properties of the keyframe are held within [keyframes].
     */
    var keyframes: ArrayList<Keyframe> = arrayListOf()
        private set

    var activeKeyframe: Keyframe? = null
        private set
    var nextKeyframe: Keyframe?  = null
        private set

    var isAnimating = false
        private set

    var isCompleted = false
        private set

    /**
     * The time when the animation is first started
     */
    var startTime = 0L

    /**
     * Starts the animation with the given [component].
     */
    fun start(component: C) {
        this.component = component

        if (keyframes.isEmpty()) throw IllegalStateException("No keyframes were added to the animation.")
        if (keyframes.size == 1) {
            keyframes.add(keyframes.first())
            keyframes[0] = Keyframe(UILinear(1000L), style.copy() as S, true)
        }

        // Load the keyframes
        activeKeyframe = keyframes.first()
        nextKeyframe = keyframes[1].also {
            it.ease.start()
        }

        startTime = System.currentTimeMillis()
    }

    /**
     * Temporarily pauses the animation at the active keyframe time.
     *
     * @see resume
     */
    fun pause() {

    }

    /**
     * Resumes the animation at the active keyframe time caused by [pause].
     *
     * @see pause
     */
    fun resume() {

    }

    /**
     * Stops the animations at the given point.
     *
     * @see complete
     */
    fun stop() {

    }

    /**
     * Stops the animation and sets the properties of the component to the ending of the last keyframe.
     *
     * @see stop
     */
    fun complete() {
        isCompleted = true

    }

    /**
     * Invoked whenever properties are updated (every frame).
     */
    fun update() {
        if (isCompleted) return
        if (activeKeyframe == null || nextKeyframe == null) complete()
        if (nextKeyframe!!.ease.finished) {
            incrementKeyframe()
            if (activeKeyframe == null || nextKeyframe == null) {
                complete()
                return
            }
        }

        component.style.animate(this, activeKeyframe!!.style, nextKeyframe!!.style, nextKeyframe!!.ease.getValue().toFloat())
    }

    /**
     * Creates a keyframe with the given [ease], and properties from [block]
     */
    fun keyframe(ease: UIEase = UILinear(1000L), block: S.() -> Unit) {
        val style = this.style.copy() as S
        style.block()
        keyframe(ease, style)
    }

    /**
     * An alternative to [keyframe] mainly for Java, which accepts an [ease] and a style sheet of [S].
     *
     * @param ease The ease of the keyframe.
     * @param style The style of the keyframe.
     * @param relative When true, the properties prior to the keyframe are used when the value is 0 or null.
     */
    @JvmOverloads
    fun keyframe(ease: UIEase, style: S, relative: Boolean = false) {
        keyframes.add(Keyframe(ease, style, relative))
    }

    private fun incrementKeyframe() {
        if (activeKeyframe == null || nextKeyframe == null) return
        if (activeKeyframe!!.ease.finished) {
            activeKeyframe = nextKeyframe
            nextKeyframe = keyframes.getOrNull(keyframes.indexOf(activeKeyframe) + 1)
        }
    }

    override fun copy(): UIAnimation<C, S> = UIAnimation<C, S>(style).also {
        // To save memory, avoid creating copies of the style sheet. Instead,
        // only copy the ease and pass the reference of the style sheet in a
        // new keyframe and populate the copy of this.
        for (keyframe in keyframes)
            it.keyframes.add(Keyframe(keyframe.ease.copy(), keyframe.style, keyframe.relative))
    }

    /**
     * [Keyframe] is an inner class which holds the ease and style of a keyframe.
     */
    inner class Keyframe(val ease: UIEase, val style: S, val relative: Boolean)
}