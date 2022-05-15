package net.prismclient.aether.ui.renderer.impl.scrollbar

import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.limit
import net.prismclient.aether.ui.util.extensions.renderer

class UIScrollbar(val type: Scrollbar) : UICopy<UIScrollbar> {
    private lateinit var component: UIContainer<*>

    var color = -1
    var border: UIBorder? = null
    var radius: UIRadius? = null

    var background: UIBackground? = null

    var x: UIUnit? = null
    var y: UIUnit? = null
    var width: UIUnit? = null
    var height: UIUnit? = null

    var cachedX = 0f
    var cachedY = 0f
    var cachedWidth = 0f
    var cachedHeight = 0f

    var value = 0f
    var shouldRender = false
    var selected = false
    private var sliderSize = 0f // Width/Height of the overall slider
    private var mouseOffset = 0f

    fun update(container: UIContainer<*>) {
        component = container

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

        // Update the width/height of the scrollbar
        if (type == Scrollbar.Vertical) {
            sliderSize = cachedHeight
            cachedHeight *= (cachedHeight / (container.expandedHeight + cachedHeight))
//            leftoverSpace = (1f - (cachedHeight / (container.expandedHeight + cachedHeight))) * container.relHeight
//            value = (cachedHeight / (container.expandedHeight + cachedHeight))
//            cachedHeight *= (cachedHeight / (container.expandedHeight + cachedHeight))
        } else if (type == Scrollbar.Horizontal) {
            sliderSize = cachedWidth
            cachedWidth *= (cachedWidth / (container.expandedWidth + cachedWidth))
//            leftoverSpace = (1f - (cachedWidth / (container.expandedWidth + cachedWidth))) * container.relWidth
//            value = (cachedWidth / (container.expandedWidth + cachedWidth))
//            cachedWidth *= (cachedWidth / (container.expandedWidth + cachedWidth))
        }
    }

    fun render() {
        if (!shouldRender) return

        var x = this.cachedX
        var y = this.cachedY
        var w = this.cachedWidth
        var h = this.cachedHeight

        if (type == Scrollbar.Vertical) {
            background?.render(x, y, cachedWidth, sliderSize)
            y += (sliderSize - cachedHeight) * value
        } else {
            background?.render(x, y, sliderSize, cachedHeight)
            x += (sliderSize - cachedHeight) * value
        }
        renderer {
            color(color)
            rect(x, y, w, h, radius)
            border?.render(x, y, w, h, radius)
        }
    }

    // TODO: Fix scrollbar calculation
    fun mousePressed(mouseX: Float, mouseY: Float) {
        var x = cachedX
        var y = cachedY
        var w = cachedWidth
        var h = cachedHeight
        if (type == Scrollbar.Vertical) {
            y += (sliderSize - cachedHeight) * value
            h = sliderSize
        } else {
            x += (sliderSize - cachedHeight) * value
            w = sliderSize
        }
        // Check if inside the bound box of the scorllbar
        if (mouseX >= cachedX && mouseY >= cachedY && mouseX <= cachedX + w && mouseY <= cachedY + h) {
            // If inside the actual scrollbar
            if (mouseX >= x && y >= y && mouseX <= x + cachedWidth && mouseY <= y + cachedHeight) {
                selected = true
                mouseOffset = if (type == Scrollbar.Vertical) {
                    mouseY - cachedY
                } else {
                    mouseX - cachedX
                }
            } else {
                if (type == Scrollbar.Vertical) {
                    value = ((mouseY - mouseOffset)) / (sliderSize - cachedHeight)
                } else {
                    value = ((mouseX - mouseOffset)) / (sliderSize - cachedWidth)
                }
                value = value.limit()
            }
        }
    }

    fun release() {
        selected = false
    }

    fun mouseMoved(mouseX: Float, mouseY: Float) {
        if (selected) {
            if (type == Scrollbar.Vertical) {
                value = ((mouseY - mouseOffset) - cachedY) / (sliderSize - cachedHeight)
            } else {
                value = ((mouseX - mouseOffset) - cachedX) / (sliderSize - cachedWidth)
            }
            value = value.limit()
        }
    }

    inline fun border(block: UIBorder.() -> Unit) {
        border = UIBorder()
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