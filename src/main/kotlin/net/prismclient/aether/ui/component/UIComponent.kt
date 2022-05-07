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
    var style: T
    var parent: UIComponent<*>? = null
    var animation: UIAnimation? = null

    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f
    var relX = 0f
    var relY = 0f
    var relWidth = 0f
    var relHeight = 0f

    init {
        this.style = UIProvider.getStyle(style, false) as T
    }

    open fun update() {

        // Update the position and size
        updatePosition()
        updateSize()

        // Update the style
        updateStyle()
    }

    open fun updatePosition() {
        width = +style.width
        height = -style.height
        relWidth = width + +style.padding?.paddingRight
        relHeight = height + -style.padding?.paddingBottom
    }

    open fun updateSize() {
        x = +style.x + getParentX()
        y = -style.y + getParentY()
        relX = x - +style.padding?.paddingLeft
        relY = y - -style.padding?.paddingTop
    }

    open fun updateStyle() {
        // Update font if not null
        style.font?.update(x + style.font!!.x.getX(width, false), y + style.font!!.y.getY(height, false))
    }

    open fun updateAnimation() {
        animation?.update()
    }

    open fun render() {
        style.background?.render(relX, relY, relWidth, relHeight)
        renderer {
            scissor(relX, relY, relWidth, relHeight) {
                renderComponent()
            }
        }
    }

    abstract fun renderComponent()

    open fun mouseMoved(mouseX: Float, mouseY: Float) {

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

    operator fun UIUnit?.unaryPlus() = this.getX()

    operator fun UIUnit?.unaryMinus() = this.getY()

    @JvmOverloads
    fun UIUnit?.getX(ignore: Boolean = false): Float = this.getX(getParentWidth(), ignore)

    @JvmOverloads
    fun UIUnit?.getX(width: Float, ignore: Boolean = false): Float = if (this == null) 0f else {
        if (!ignore && this is UIOperationUnit) { this.getX(width, true) + this.otherUnit.getX(width) } else {
            when (this.type) {
                PIXELS -> this.value
                RELATIVE -> this.value * width
                EM -> this.value * (style.font?.fontSize ?: 0f)
                ASCENDER -> this.value * (style.font?.getAscend() ?: 0f)
                DESCENDER -> this.value * (style.font?.getDescend() ?: 0f)
                else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
            }
        }
    }

    @JvmOverloads
    fun UIUnit?.getY(ignore: Boolean = false): Float = this.getY(getParentHeight(), ignore)

    @JvmOverloads
    fun UIUnit?.getY(height: Float, ignore: Boolean = false): Float = if (this == null) 0f else {
        if (!ignore && this is UIOperationUnit) {
            this.getY(height, true) + this.otherUnit.getY(height)
        } else {
            when (this.type) {
                PIXELS -> this.value
                RELATIVE -> this.value * height
                EM -> this.value * (style.font?.fontSize ?: 0f)
                ASCENDER -> this.value * (style.font?.getAscend() ?: 0f)
                DESCENDER -> this.value * (style.font?.getDescend() ?: 0f)
                else -> throw UnsupportedOperationException("${this.type} is not a valid type.")
            }
        }
    }

    fun getParentX() = if (parent != null)
        if (parent is UIFrame && (parent as UIFrame).style.clipContent) 0f else parent!!.x else 0f

    fun getParentY() = if (parent != null)
        if (parent is UIFrame && (parent as UIFrame).style.clipContent) 0f else parent!!.y else 0f

    fun getParentWidth() = if (parent != null) parent!!.width else UICore.width

    fun getParentHeight() = if (parent != null) parent!!.height else UICore.height

    fun isMouseInside() =
            (UICore.mouseX > x) && (UICore.mouseY > y) && (UICore.mouseX < x + width) && (UICore.mouseY < y + height)

    /**
     * Returns true if the mouse is inside the rel x, y, width and height
     */
    fun isMouseInsideBoundingBox() =
        (UICore.mouseX > relX) && (UICore.mouseY > relY) && (UICore.mouseX < relX + relWidth) && (UICore.mouseY < relY + relHeight)

    fun isFocused(): Boolean = UICore.instance.focusedComponent == this
}