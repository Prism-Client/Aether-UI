package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.interfaces.UICopy
import net.prismclient.aether.ui.util.extensions.calculateX
import net.prismclient.aether.ui.util.extensions.calculateY

/**
 * [UIShape] is an extension of the class [UIObject] which provides
 * a position and size unit properties which are calculating within
 * the update method.
 *
 * @author sen
 * @since 5/4/2022
 */
abstract class UIShape : UIObject(), UICopy<UIShape> {
    var x: UIUnit? = null
    var y: UIUnit? = null
    var width: UIUnit? = null
    var height: UIUnit? = null

    var offsetX = 0f
    var offsetY = 0f

    var cachedX = 0f
        protected set
    var cachedY = 0f
        protected set
    var cachedWidth = 0f
        protected set
    var cachedHeight = 0f
        protected set

    override fun update(component: UIComponent<*>) {
        this.component = component
        cachedX = calculateX(x, component, component.width) + component.x
        cachedY = calculateY(y, component, component.height) + component.y
        cachedWidth = calculateX(width, component, component.width)
        cachedHeight = calculateY(height, component, component.height)
    }

    open fun apply(shape: UIShape): UIShape {
        x = shape.x?.copy()
        y = shape.y?.copy()
        width = shape.width?.copy()
        height = shape.height?.copy()
        return this
    }
}