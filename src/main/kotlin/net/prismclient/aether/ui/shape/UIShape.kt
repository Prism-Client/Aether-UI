package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.calculate
import net.prismclient.aether.ui.util.extensions.lerp
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.interfaces.UIAnimatable
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIShape] is an extension of the class [UIObject] which provides
 * a position and size unit properties which are calculating within
 * the update method.
 *
 * @author sen
 * @since 5/4/2022
 */
abstract class UIShape<T : UIShape<T>> : UIObject(), UICopy<T>, UIAnimatable<T> {
    var x: UIUnit? = null
    var y: UIUnit? = null
    var width: UIUnit? = null
    var height: UIUnit? = null

    var cachedX = 0f
        protected set
    var cachedY = 0f
        protected set
    var cachedWidth = 0f
        protected set
    var cachedHeight = 0f
        protected set

    // -- Shorthands -- //

    fun position(x: UIUnit?, y: UIUnit?) {
        this.x = x
        this.y = y
    }

    fun position(x: Number, y: Number) = position(px(x), px(y))

    fun size(width: UIUnit?, height: UIUnit?) {
        this.width = width
        this.height = height
    }

    fun size(width: Number, height: Number) = size(px(width), px(height))

    // -- Core -- //

    override fun update(component: UIComponent<*>?) {
        this.component = component
        cachedX = (component?.x ?: 0f) + calculate(x, component, component?.width ?: 0f, component?.height ?: 0f, false)
        cachedY = (component?.y ?: 0f) + calculate(y, component, component?.width ?: 0f, component?.height ?: 0f, true)
        cachedWidth = calculate(width, component, component?.width ?: 0f, component?.height ?: 0f, false)
        cachedHeight = calculate(height, component, component?.width ?: 0f, component?.height ?: 0f, true)
    }

    override fun animate(animation: UIAnimation<*>, previous: T?, current: T?, progress: Float) {
        cachedX = previous?.x.lerp(current?.x, animation.component, progress, false)
        cachedY = previous?.y.lerp(current?.y, animation.component, progress, true)
        cachedWidth = previous?.width.lerp(current?.width, animation.component, progress, false)
        cachedHeight = previous?.height.lerp(current?.height, animation.component, progress, true)
    }

    override fun save(animation: UIAnimation<*>, keyframe: T?) {
        x = keyframe?.x ?: x?.copy()
        y = keyframe?.y ?: y?.copy()
        width = keyframe?.width ?: width?.copy()
        height = keyframe?.height ?: height?.copy()
    }

    open fun apply(shape: UIShape<T>): UIShape<T> {
        x = shape.x?.copy()
        y = shape.y?.copy()
        width = shape.width?.copy()
        height = shape.height?.copy()
        return this
    }
}