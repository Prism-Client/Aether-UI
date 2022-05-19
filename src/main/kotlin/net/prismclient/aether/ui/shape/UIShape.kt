package net.prismclient.aether.ui.shape

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.other.UIRenderable
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.calculateX
import net.prismclient.aether.ui.util.extensions.calculateY

abstract class UIShape : UIRenderable, UICopy<UIShape> {
    protected var component: UIComponent<*>? = null
    var color = 0
    var x: UIUnit? = null
    var y: UIUnit? = null
    var width: UIUnit? = null
    var height: UIUnit? = null

    var offsetX = 0f
    var offsetY = 0f

    var cachedX = 0f
    var cachedY = 0f
    var cachedWidth = 0f
    var cachedHeight = 0f

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
        color = shape.color
        return this
    }
}