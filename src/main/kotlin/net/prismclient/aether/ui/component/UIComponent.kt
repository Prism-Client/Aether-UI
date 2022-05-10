package net.prismclient.aether.ui.component

import net.prismclient.aether.UICore
import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.util.interfaces.UIFocusable
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.unit.util.*
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.renderer

abstract class UIComponent<T : UIStyleSheet>(style: String) {
    var style: T = UIProvider.getStyle(style, false) as T
    var parent: UIComponent<*>? = null
    var animation: UIAnimation<*>? = null

    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f
    var relX = 0f
    var relY = 0f
    var relWidth = 0f
    var relHeight = 0f

    var wasInside = false

    /**
     * Invoked on creation, and screen resize
     */
    open fun update() {
        // Update the position and size
        updatePosition()
        updateSize()

        // Update the relative values
        updateBounds()

        // Update the style
        updateStyle()
    }

    open fun updatePosition() {
        x = +style.x + getParentX() - getAnchorX()
        y = -style.y + getParentY() - getAnchorY()
    }

    open fun updateSize() {
        width = +style.width
        height = -style.height
    }

    /**
     * Updates the relative values based on the absolute values
     */
    open fun updateBounds() {
        relX = x - +style.padding?.paddingLeft
        relY = y - -style.padding?.paddingTop
        relWidth = width + +style.padding?.paddingRight
        relHeight = height + -style.padding?.paddingBottom
    }


    open fun updateStyle() {
        // Update font if not null
        style.font?.update(x + style.font!!.x.getX(width, false), y + style.font!!.y.getY(height, false))
    }

    open fun updateAnimation() {
        animation?.update()
    }

    open fun render() {
        updateAnimation()
        style.background?.render(relX, relY, relWidth, relHeight)
        renderer {
            scissor(relX, relY, relWidth, relHeight) {
                renderComponent()
            }
        }
    }

    abstract fun renderComponent()

    open fun mouseMoved(mouseX: Float, mouseY: Float) {
        if (isMouseInsideBoundingBox() && !wasInside) {
            wasInside = true
            onMouseEnter()
        } else if (wasInside) {
            onMouseLeave()
            wasInside = false
        }
    }

    open fun mouseClicked(mouseX: Float, mouseY: Float) {
        if (this is UIFocusable<*> && !isFocused() && isMouseInsideBoundingBox()) {
            UICore.instance.focus(this)
        }
    }

    open fun mouseReleased(mouseX: Float, mouseY: Float) {

    }

    open fun keyPressed(key: UIKey, character: Char) {

    }

    open fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {

    }

    open fun onMouseEnter() {

    }

    open fun onMouseLeave() {

    }

    operator fun UIUnit?.unaryPlus() = this.getX()

    operator fun UIUnit?.unaryMinus() = this.getY()

    @JvmOverloads
    fun UIUnit?.getX(ignore: Boolean = false): Float = this.getX(getParentWidth(), ignore)

    @JvmOverloads
    fun UIUnit?.getX(width: Float, ignore: Boolean = false): Float =
            calculateUnitX(this, width, ignore)

    fun calculateUnitX(unit: UIUnit?, width: Float, ignore: Boolean): Float = if (unit == null) 0f else {
        if (!ignore && unit is UIOperationUnit) {
            unit.getX(width, true) + unit.otherUnit.getX(width)
        } else {
            when (unit.type) {
                PIXELS, PXANIMRELATIVE -> unit.value
                RELATIVE, RELANIMRELATIVE -> unit.value * width
                EM -> unit.value * (style.font?.fontSize ?: 0f)
                ASCENDER -> unit.value * (style.font?.getAscend() ?: 0f)
                DESCENDER -> unit.value * (style.font?.getDescend() ?: 0f)
                else -> throw UnsupportedOperationException("${unit.type} is not a valid type.")
            }
        }
    }

    @JvmOverloads
    fun UIUnit?.getY(ignore: Boolean = false): Float = this.getY(getParentHeight(), ignore)

    @JvmOverloads
    fun UIUnit?.getY(height: Float, ignore: Boolean = false) =
            calculateUnitY(this, height, ignore)

    fun calculateUnitY(unit: UIUnit?, height: Float, ignore: Boolean): Float = if (unit == null) 0f else {
        if (!ignore && unit is UIOperationUnit) {
            unit.getY(height, true) + unit.otherUnit.getY(height)
        } else {
            when (unit.type) {
                PIXELS, PXANIMRELATIVE -> unit.value
                RELATIVE, RELANIMRELATIVE -> unit.value * height
                EM -> unit.value * (style.font?.fontSize ?: 0f)
                ASCENDER -> unit.value * (style.font?.getAscend() ?: 0f)
                DESCENDER -> unit.value * (style.font?.getDescend() ?: 0f)
                else -> throw UnsupportedOperationException("${unit.type} is not a valid type.")
            }
        }
    }

    fun getParentX() = if (parent != null)
            if (parent is UIFrame && (parent as UIFrame).style.clipContent) 0f else parent!!.x else 0f

    fun getParentY() = if (parent != null)
            if (parent is UIFrame && (parent as UIFrame).style.clipContent) 0f else parent!!.y else 0f

    fun getParentWidth() =
            if (parent != null) parent!!.width else UICore.width

    fun getParentHeight() =
            if (parent != null) parent!!.height else UICore.height

    fun getAnchorX() =
            calculateUnitX(style.anchor.x, width, false)

    fun getAnchorY() =
            calculateUnitY(style.anchor.y, height, false)

    fun isMouseInside() =
            (getMouseX() > x) &&
            (getMouseY() > y) &&
            (getMouseX() < x + width) &&
            (getMouseY() < y + height)

    /**
     * Returns true if the mouse is inside the rel x, y, width and height
     */
    fun isMouseInsideBoundingBox() =
            (getMouseX() > relX) &&
            (getMouseY() > relY) &&
            (getMouseX() < relX + relWidth) &&
            (getMouseY() < relY + relHeight)

    fun isFocused(): Boolean = UICore.instance.focusedComponent == this

    private fun getMouseX(): Float =
            UICore.mouseX - if (parent != null && parent is UIFrame) parent!!.relX else 0f

    private fun getMouseY(): Float =
            UICore.mouseY - if (parent != null && parent is UIFrame) parent!!.relY else 0f
}