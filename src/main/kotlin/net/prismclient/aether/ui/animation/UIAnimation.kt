package net.prismclient.aether.ui.animation

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.interfaces.UICopy
import java.util.function.Consumer

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
 * When ran, a copy of this is assigned to the component. If any styles are changed at that point,
 * it will change the original animation keyframes. Everything but the listeners are copied.
 *
 * @author sen
 * @since 1.0
 *
 * @param style The style sheet of the component. The style should be completely new, with no styles changed.
 */
@Suppress("UNCHECKED_CAST")
class UIAnimation<S : UIStyleSheet>(val name: String, val style: S) : UICopy<UIAnimation<S>> {
    /**
     * The component that this animation is attached to
     */
    lateinit var component: UIComponent<S>
        private set

    /**
     * The keyframes of the animation where the ease and
     * properties of the keyframe are held within [keyframes].
     */
    var keyframes: ArrayList<Keyframe> = arrayListOf()
        private set

    var activeKeyframe: Keyframe? = null
        private set
    var nextKeyframe: Keyframe? = null
        private set

    var isAnimating = false
        private set
    var isCompleted = false
        private set

    var completionListeners: HashMap<String, Consumer<UIAnimation<S>>>? = null
        private set

    /**
     * The time when the animation is first started
     */
    var startTime = 0L

    /**
     * A private variable which stores the active ease when using [repeat].
     */
    var activeEase: UIEase? = null

    /**
     * Starts the animation with the given [component].
     */
    fun start(component: UIComponent<S>) {
        this.component = component

        component.animations = component.animations ?: hashMapOf()
        component.animations!![name] = this

        if (keyframes.isEmpty()) throw IllegalStateException("No keyframes were added to the animation.")
        if (keyframes.size == 1) {
            keyframes.add(keyframes.first())
            keyframes[0] = Keyframe(UILinear(1000L), style, true)
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
        completionListeners?.forEach { it.value.accept(this) }
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

        component.style.animate(
            this,
            activeKeyframe!!.style,
            nextKeyframe!!.style,
            nextKeyframe!!.ease.getValue().toFloat()
        )
    }

    /**
     * Saves the state of the [activeKeyframe]. It applies all used properties to the component.
     */
    fun save() {
        if (activeKeyframe != null) {
            component.style.save(this, activeKeyframe!!.style)
        }
    }

    /**
     * Creates a keyframe with the given [ease], and properties from [block]
     */
    inline fun keyframe(ease: UIEase? = null, block: S.() -> Unit = {}) {

        val style = this.style.copy() as S
        style.block()
        // Ease -> activeEase -> UILinear if null.
        keyframe(ease ?: activeEase?.copy() ?: UILinear(1000L), style)
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

    /**
     * Shorthand for creating a keyframe.
     *
     * @see keyframe
     */
    inline fun kf(ease: UIEase? = null, block: S.() -> Unit = {}) = keyframe(ease, block)

    /**
     * An alternative to [keyframe], where the ease is created then this is applied.
     *
     * @see keyframe
     */
    inline infix fun UIEase.to(block: S.() -> Unit) = keyframe(this, block)

    /**
     * Applies the ease to each keyframe within this animation.
     */
    inline infix fun UIEase.repeat(block: UIAnimation<S>.() -> Unit) {
        activeEase = this
        block()
    }

    private fun incrementKeyframe() {
        if (activeKeyframe == null || nextKeyframe == null) return
        if (nextKeyframe!!.ease.finished) {
            activeKeyframe = nextKeyframe
            nextKeyframe = keyframes.getOrNull(keyframes.indexOf(activeKeyframe) + 1)
            nextKeyframe?.ease?.start()
            save()
        }
    }

    /**
     * Adds a listener which is invoked when the animation is completed
     */
    @JvmOverloads
    fun onCompletion(name: String = "Default-${completionListeners?.size ?: 0}", listener: Consumer<UIAnimation<S>>) {
        completionListeners = completionListeners ?: hashMapOf()
        completionListeners!![name] = listener
    }

    override fun copy(): UIAnimation<S> = UIAnimation(name, style).also {
        // To save memory, avoid creating copies of the style sheet. Instead,
        // only copy the ease and pass the reference of the style sheet in a
        // new keyframe and populate the copy of this.
        for (keyframe in keyframes)
            it.keyframes.add(Keyframe(keyframe.ease.copy(), keyframe.style.copy() as S, keyframe.relative))
        it.completionListeners = completionListeners
    }

    /**
     * [Keyframe] is an inner class which holds the ease and style of a keyframe.
     */
    inner class Keyframe(val ease: UIEase, val style: S, val relative: Boolean)
}