package net.prismclient.aether.ui.renderer.impl.scrollbar

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.shape.UIShape
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.limit
import net.prismclient.aether.ui.util.extensions.renderer

class UIScrollbar(val type: Scrollbar) : UIShape() {
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

    override fun update(component: UIComponent<*>) {
        this.component = component
        val container = component as UIContainer<*>

        // Check based on the overflow if the scrollbar should be rendered or not
        shouldRender = if (type == Scrollbar.Vertical) {
            when (container.style.overflowX) {
                UIContainerSheet.Overflow.None -> false
                UIContainerSheet.Overflow.Scroll -> true
                UIContainerSheet.Overflow.Auto -> container.expandedWidth > 0f
            }
        } else {
            when (container.style.overflowX) {
                UIContainerSheet.Overflow.None -> false
                UIContainerSheet.Overflow.Scroll -> true
                UIContainerSheet.Overflow.Auto -> container.expandedHeight > 0f
            }
        }

        // Update the values
        cachedX = container.relX + container.calculateUnitX(x, container.relWidth, false)
        cachedY = container.relY + container.calculateUnitY(y, container.relHeight, false)
        cachedWidth = container.calculateUnitX(width, container.relWidth, false)
        cachedHeight = container.calculateUnitY(height, container.relHeight, false)

        if (type == Scrollbar.Vertical) {
            sliderSize = cachedHeight * (cachedHeight / (container.expandedHeight + cachedHeight))
        } else if (type == Scrollbar.Horizontal) {
            sliderSize = cachedWidth * (cachedWidth / (container.expandedWidth + cachedWidth))
        }
    }

    override fun render() {
        if (!shouldRender) return

        var x = cachedX
        var y = cachedY
        var w = cachedWidth
        var h = cachedHeight

        background?.render(x, y, w, h)

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

    fun mousePressed(mouseX: Float, mouseY: Float) {
        if (!shouldRender || (((component!! as UIContainer<*>).expandedWidth <= 0f && type == Scrollbar.Horizontal) || ((component!! as UIContainer<*>).expandedHeight <= 0f && type == Scrollbar.Vertical))) return

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

        if (component!!.isWithinBounds(x, y, w, h)) {
            selected = true
            mouseOffset = if (type == Scrollbar.Vertical) mouseY - y else mouseX - x
        }
    }

    fun mouseMoved(mouseX: Float, mouseY: Float) {
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

    @JvmOverloads
    inline fun background(color: Int, block: UIBackground.() -> Unit = {}) = background { this.color = color; this.block() }

    inline fun background(block: UIBackground.() -> Unit) {
        background = background ?: UIBackground()
        background!!.block()
    }

    inline fun border(block: UIBorder.() -> Unit) {
        border = border ?: UIBorder()
        border!!.block()
    }

    /**
     * Describes the type of scrollbar
     *
     * @author sen
     * @since 5/12/2022
     */
    enum class Scrollbar {
        Vertical, Horizontal
    }

    override fun copy(): UIScrollbar = UIScrollbar(type).also {
        it.color = color
        it.radius = radius?.copy()
        it.border = border?.copy()
        it.background = background?.copy()
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
    }
}