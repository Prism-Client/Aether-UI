package net.prismclient.aether.ui.renderer.impl.scrollbar

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIColoredShape
import net.prismclient.aether.ui.util.extensions.calculate
import net.prismclient.aether.ui.util.extensions.limit
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.renderer

class UIScrollbar(val type: Scrollbar) : UIColoredShape() {
    var border: UIBorder? = null
    var radius: UIRadius? = null

    var background: UIBackground? = null

    var value = 0f
        set(value) {
            field = value.limit()
        }
    var shouldRender = false
    var selected = false
    private var sliderSize = 0f // Width/Height of the overall slider
    private var mouseOffset = 0f

    override fun update(component: UIComponent<*>?) {
        this.component = component
        val container = component as UIContainer<*>

        shouldRender()

        // Update the values
        cachedX = container.relX + calculate(x, container, container.relWidth, container.relHeight, false)
        cachedY = container.relY + calculate(y, container, container.relWidth, container.relHeight, true)
        cachedWidth = calculate(width, container, container.relWidth, container.relHeight, false)
        cachedHeight = calculate(height, container, container.relWidth, container.relHeight, true)

        if (type == Scrollbar.Vertical) {
            sliderSize = cachedHeight * (cachedHeight / (container.expandedHeight + cachedHeight))
        } else if (type == Scrollbar.Horizontal) {
            sliderSize = cachedWidth * (cachedWidth / (container.expandedWidth + cachedWidth))
        }

        background?.x = px(cachedX)
        background?.y = px(cachedY)
        background?.width = px(cachedWidth)
        background?.height = px(cachedHeight)
        background?.update(null)
    }

    fun shouldRender() {
        val container = component as UIContainer<*>

        // Check based on the overflow if the scrollbar should be rendered or not
        shouldRender = if (type == Scrollbar.Vertical) {
            when (container.style.overflowY) {
                UIContainerSheet.Overflow.None -> false
                UIContainerSheet.Overflow.Always -> true
                UIContainerSheet.Overflow.Auto -> container.expandedHeight > 0f
            }
        } else {
            when (container.style.overflowX) {
                UIContainerSheet.Overflow.None -> false
                UIContainerSheet.Overflow.Always -> true
                UIContainerSheet.Overflow.Auto -> container.expandedWidth > 0f
            }
        }
    }

    override fun render() {
        if (!shouldRender) return

        var x = cachedX
        var y = cachedY
        var w = cachedWidth
        var h = cachedHeight

        background?.render()

        if (type == Scrollbar.Vertical) {
            y += (h - sliderSize) * value
            h = sliderSize
        } else if (type == Scrollbar.Horizontal) {
            x += (w - sliderSize) * value
            w = sliderSize
        }

        renderer {
            color(color)
            rect(x, y, w, h, radius)
            border?.render(x, y, w, h, radius)
        }
    }

    /**
     * Invoked when the mouse is pressed. Returns true if the scrollbar was selected
     */
    fun mousePressed(): Boolean {
        if (!shouldRender || (((component!! as UIContainer<*>).expandedWidth <= 0f && type == Scrollbar.Horizontal) || ((component!! as UIContainer<*>).expandedHeight <= 0f && type == Scrollbar.Vertical))) return false

        var x = cachedX
        var y = cachedY
        var w = cachedWidth
        var h = cachedHeight

        if (type == Scrollbar.Vertical) {
            y += (h - sliderSize) * value
            h = sliderSize
        } else if (type == Scrollbar.Horizontal) {
            x += (w - sliderSize) * value
            w = sliderSize
        }

        val mouseX = component!!.getMouseX()
        val mouseY = component!!.getMouseY()

        if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
            selected = true
            mouseOffset = if (type == Scrollbar.Vertical) mouseY - y else mouseX - x
            return true
        }
        return false
    }

    fun mouseMoved() {
        val mouseX = component!!.getMouseX()
        val mouseY = component!!.getMouseY()
        if (selected) {
            value = if (type == Scrollbar.Vertical) {
                (mouseY - cachedY - mouseOffset) / (cachedHeight - sliderSize)
            } else {
                (mouseX - cachedX - mouseOffset) / (cachedWidth - sliderSize)
            }
        }
    }

    fun release() {
        selected = false
    }

    /**
     * A DSL block for creating a background to this [UIScrollbar]
     */
    inline fun background(block: UIBackground.() -> Unit) {
        background = background ?: UIBackground()
        background!!.block()
    }

    /**
     * Shorthand for adding a color, and radius (optional) for the background to this [UIScrollbar]
     */
    @JvmOverloads
    inline fun background(color: Int, radius: UIRadius? = background?.radius, block: UIBackground.() -> Unit = {}) =
        background { this.backgroundColor = color; this.radius = radius; this.block() }

    /**
     * A DSL block for creating a border to this [UIScrollbar]
     */
    inline fun border(block: UIBorder.() -> Unit) {
        border = border ?: UIBorder()
        border!!.block()
    }

    /**
     * Describes the direction of the scrollbar
     *
     * @author sen
     * @since 5/12/2022
     */
    enum class Scrollbar {
        Vertical, Horizontal
    }

    override fun copy(): UIScrollbar = UIScrollbar(type).also {
        it.apply(this)
        it.border = border?.copy()
        it.radius = radius?.copy()
        it.background = background?.copy()
    }
}