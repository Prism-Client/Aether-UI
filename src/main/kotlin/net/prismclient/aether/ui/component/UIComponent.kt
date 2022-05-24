package net.prismclient.aether.ui.component

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.type.layout.styles.UIFrameSheet
import net.prismclient.aether.ui.component.util.interfaces.UIFocusable
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.unit.util.*
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.calculateY
import net.prismclient.aether.ui.util.extensions.renderer
import java.util.function.Consumer

abstract class UIComponent<T : UIStyleSheet>(style: String) {
    var style: T = UIProvider.getStyle(style, false) as T
    var parent: UIComponent<*>? = null
    var animation: UIAnimation<*>? = null
    var overrided = false

    /** Component plot properties **/
    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f
    var relX = 0f
    var relY = 0f
    var relWidth = 0f
    var relHeight = 0f

    /** Padding and Margin **/
    var paddingTop = 0f
    var paddingRight = 0f
    var paddingBottom = 0f
    var paddingLeft = 0f

    var marginTop = 0f
    var marginRight = 0f
    var marginBottom = 0f
    var marginLeft = 0f

    var wasInside = false

    /** Listeners **/
    protected var mousePressedListeners: MutableList<Consumer<UIComponent<*>>>? = null
    protected var mouseReleasedListeners: MutableList<Consumer<UIComponent<*>>>? = null
    protected var mouseEnteredListeners: MutableList<Consumer<UIComponent<*>>>? = null
    protected var mouseLeaveListeners: MutableList<Consumer<UIComponent<*>>>? = null

    /**
     * Invoked on creation, and screen resize
     */
    open fun update() {
        calculateBounds()
        // Update the size, then the position
        updateSize()
        updatePosition()

        // Update the relative values
        updateBounds()
        updateStyle()
    }

    open fun updatePosition() {
        if (!overrided) {
            x = +style.x + getParentX() - getAnchorX() + marginLeft
            y = -style.y + getParentY() - getAnchorY() + marginTop
        }
    }

    open fun updateSize() {
        width = +style.width
        height = -style.height
    }

    open fun calculateBounds() {
        // Update padding
        paddingTop = -style.padding?.paddingTop
        paddingRight = +style.padding?.paddingRight
        paddingBottom = -style.padding?.paddingBottom
        paddingLeft = +style.padding?.paddingLeft

        // Update margin
        marginTop = -style.margin?.marginTop
        marginRight = +style.margin?.marginRight
        marginBottom = -style.margin?.marginBottom
        marginLeft = +style.margin?.marginLeft
    }

    /**
     * Updates the relative values based on the absolute values
     */
    open fun updateBounds() {
        relX = x - paddingLeft
        relY = y - paddingTop

        relWidth = width + paddingLeft + paddingRight
        relHeight = height + paddingTop + paddingBottom
    }


    open fun updateStyle() {
        // Update font if not null
        style.font?.update(this)
    }

    open fun updateAnimation() {
        animation?.update()
    }

    open fun render() {
        updateAnimation()
        style.background?.render(relX, relY, relWidth, relHeight)
        renderer {
            if (style.clipContent) {
                scissor(relX, relY, relWidth, relHeight) {
                    renderComponent()
                }
            } else renderComponent()
        }
    }

    abstract fun renderComponent()

    /** Input **/

    open fun mouseMoved(mouseX: Float, mouseY: Float) {
        if (isMouseInsideBoundingBox()) {
            if (!wasInside) {
                mouseEntered()
                wasInside = true
            }
        } else if (wasInside) {
            mouseLeft()
            wasInside = false
        }
    }

    open fun mouseClicked(mouseX: Float, mouseY: Float) {
        if (this is UIFocusable<*> && !isFocused() && isMouseInsideBoundingBox()) {
            UICore.instance.focus(this)
        }
        mousePressedListeners?.forEach { it.accept(this) }
    }

    open fun mouseReleased(mouseX: Float, mouseY: Float) {
        mouseReleasedListeners?.forEach { it.accept(this) }
    }

    open fun keyPressed(key: UIKey, character: Char) {

    }

    open fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {

    }

    protected open fun mouseEntered() {
        mouseEnteredListeners?.forEach { it.accept(this) }
    }

    protected open fun mouseLeft() {
        mouseLeaveListeners?.forEach { it.accept(this) }
    }

    /** Event **/

    open fun onMousePressed(event: Consumer<UIComponent<*>>): UIComponent<*> {
        if (mousePressedListeners == null)
            mousePressedListeners = mutableListOf()
        mousePressedListeners!!.add(event)
        return this
    }

    open fun onMouseReleased(event: Consumer<UIComponent<*>>): UIComponent<*> {
        if (mouseReleasedListeners == null)
            mouseReleasedListeners = mutableListOf()
        mouseReleasedListeners!!.add(event)
        return this
    }

    open fun onMouseEnter(event: Consumer<UIComponent<*>>): UIComponent<*> {
        if (mouseEnteredListeners == null)
            mouseEnteredListeners = mutableListOf()
        mouseEnteredListeners!!.add(event)
        return this
    }

    open fun onMouseLeave(event: Consumer<UIComponent<*>>): UIComponent<*> {
        if (mouseLeaveListeners == null)
            mouseLeaveListeners = mutableListOf()
        mouseLeaveListeners!!.add(event)
        return this
    }

    /** Position Calculation **/

    operator fun UIUnit?.unaryPlus() = this.getX()

    operator fun UIUnit?.unaryMinus() = this.getY()

    @JvmOverloads
    fun UIUnit?.getX(ignore: Boolean = false): Float = this.getX(getParentWidth(), ignore)

    @JvmOverloads
    fun UIUnit?.getX(width: Float, ignore: Boolean = false): Float =
        calculateUnitX(this, width, ignore)

    fun calculateUnitX(unit: UIUnit?, width: Float, ignore: Boolean): Float = if (unit == null) 0f else {
        calculateX(unit, this, width, ignore)
    }

    @JvmOverloads
    fun calculateX(
        unit: UIUnit?,
        component: UIComponent<*>,
        width: Float = component.getParentWidth(),
        ignoreOperation: Boolean = false
    ): Float {
        return if (unit == null) 0f else if (!ignoreOperation && unit is UIOperationUnit) net.prismclient.aether.ui.util.extensions.calculateX(
            unit,
            component,
            width
        )
        else when (unit.type) {
            PIXELS, PXANIMRELATIVE -> unit.value
            RELATIVE, RELANIMRELATIVE -> unit.value * width
            EM -> unit.value * (component.style.font?.fontSize ?: 0f)
            ASCENDER -> unit.value * (component.style.font?.getAscend() ?: 0f)
            DESCENDER -> unit.value * (component.style.font?.getDescend() ?: 0f)
            else -> throw RuntimeException("{${unit.type} is not a valid Unit Type.}")
        }
    }


    @JvmOverloads
    fun UIUnit?.getY(ignore: Boolean = false): Float = this.getY(getParentHeight(), ignore)

    @JvmOverloads
    fun UIUnit?.getY(height: Float, ignore: Boolean = false) =
        calculateUnitY(this, height, ignore)

    fun calculateUnitY(unit: UIUnit?, height: Float, ignore: Boolean): Float = if (unit == null) 0f else {
        calculateY(unit, this, height, ignore)
    }

    fun getParentX() = if (parent != null)
        if (parent is UIFrame && ((parent as UIFrame).style as UIFrameSheet).clipContent) 0f else parent!!.x else 0f

    fun getParentY() = if (parent != null)
        if (parent is UIFrame && ((parent as UIFrame).style as UIFrameSheet).clipContent) 0f else parent!!.y else 0f

    fun getParentWidth() =
        if (parent != null) parent!!.width else UICore.width

    fun getParentHeight() =
        if (parent != null) parent!!.height else UICore.height

    fun getAnchorX() =
        calculateUnitX(style.anchor.x, width, false)

    fun getAnchorY() =
        calculateUnitY(style.anchor.y, height, false)

    // TODO: Cleanup this mess with mouse bubbling and stuff

    fun isMouseInside() =
        (getMouseX() > x) &&
                (getMouseY() > y) &&
                (getMouseX() < x + width) &&
                (getMouseY() < y + height)

    /**
     * Returns true if the mouse is inside the rel x, y, width and height
     */
    fun isMouseInsideBoundingBox() = ((getMouseX() >= relX) &&
            (getMouseY() >= relY) &&
            (getMouseX() <= relX + relWidth) &&
            (getMouseY() <= relY + relHeight)) &&
            (parent == null ||
                    (UICore.mouseX >= parent!!.relX) &&
                    (UICore.mouseY >= parent!!.relY) &&
                    (UICore.mouseX <= parent!!.relX + parent!!.relWidth) &&
                    (UICore.mouseY <= parent!!.relY + parent!!.relHeight))

    fun isFocused(): Boolean = UICore.instance.focusedComponent == this

    fun getMouseX(): Float =
        UICore.mouseX - getParentXOffset()

    fun getMouseY(): Float =
        UICore.mouseY - getParentYOffset()

    fun getParentXOffset(): Float {
        return (if (parent is UIFrame && (parent!!.style as UIFrameSheet).clipContent) {
            parent!!.relX
        } else 0f) - if (parent != null && parent is UIContainer) {
            ((parent!!.style as UIContainerSheet).horizontalScrollbar.value * (parent as UIContainer).expandedWidth)
        } else 0f
    }

    fun getParentYOffset(): Float {
        return (if (parent is UIFrame && (parent!!.style as UIFrameSheet).clipContent) {
            parent!!.relY
        } else 0f) - if (parent != null && parent is UIContainer) {
            ((parent!!.style as UIContainerSheet).verticalScrollbar.value * (parent as UIContainer).expandedHeight)
        } else 0f
    }

    fun isWithinBounds(x: Float, y: Float, width: Float, height: Float) =
        (x <= getMouseX() && y <= getMouseY() && x + width >= getMouseX() && y + height >= getMouseY())


    /** Other **/
    inline fun style(block: T.() -> Unit) =
        block.invoke(style)

    /**
     * Shorthand for loading animations for when the mouse hovers over the component
     */
    fun hover(hoverAnimation: String, leaveAnimation: String): UIComponent<*> {
        onMouseEnter {
            UIProvider.dispatchAnimation(hoverAnimation, this)
        }
        onMouseLeave {
            UIProvider.dispatchAnimation(leaveAnimation, this)
        }
        return this
    }
}