package net.prismclient.aether.ui.util.interfaces

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.util.UIAnimationResult
import net.prismclient.aether.ui.component.UIComponent

/**
 * [UIAnimatable] is an interface which all renderable object must inherit
 * to properly animate itself on screen.
 *
 * @author sen.
 * @since 5/27/2022
 * @see <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Animation.md#understanding-the-animation-implementation">Understanding the animation implementation</a>
 *
 * @param T The object of this
 */
interface UIAnimatable<T> {
    /**
     * This function is a crucial function for animating renderable objects. Given
     * two classes of [T] (this), all properties within this should be set to the
     * difference between the [previous] and [current] based on the [progress].
     *
     * For example, let's assume you have a variable called x. If x in the [previous]
     * class is 50, in the [current] class it is 100, and the [progress] is 0.5, it can
     * be calculated by previous + ((current - previous) * progress) aka 100 - 50 = 50,
     * 50 * 0.5 = 25, 50 + 25 = 75; thus, the output should be 75.
     *
     * If at any point [previous] or [current] is null, then the output should be the initial
     * value of this. To do this, it might be needed to have a cached instance of self and initialize
     * it if it is not initialized when this method is invoked
     *
     * In essence, this function should produce the output of the two of [T] based on [progress].
     *
     * @see <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Animation.md#understanding-the-animation-implementation">Understanding the animation implementation</a>
     *
     * @param component This parameter is option and can be null depending on the needs of the [UIAnimatable]
     */
    fun animate(previous: T?, current: T?, progress: Float, component: UIComponent<*>?)
}