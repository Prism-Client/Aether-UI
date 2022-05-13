package net.prismclient.aether.ui.renderer.impl.scrollbar

import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UICopy
import net.prismclient.aether.ui.util.extensions.renderer

class UIScrollbar(val type: Scrollbar) : UICopy<UIScrollbar> {
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

    var shouldRender = false
        private set
    var selected = false
        private set
    var offsetX = 0f
    var offsetY = 0f

    fun update(container: UIContainer<*>) {
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

        cachedX = container.relX + container.calculateUnitX(x, container.relWidth, false)
        cachedY = container.relY + container.calculateUnitY(y, container.relHeight, false)
        cachedWidth = container.calculateUnitX(width, container.relWidth, false)
        cachedHeight = container.calculateUnitY(height, container.relHeight, false)

        // Update the width/height of the scrollbar
        if (type == Scrollbar.Vertical) {
            cachedHeight *= (cachedHeight / (container.expandedHeight + cachedHeight))
        } else if (type == Scrollbar.Horizontal) {
            cachedWidth *= (cachedWidth / (container.expandedWidth + cachedWidth))
        }
    }

    fun render() {
        if (!shouldRender) return
        background?.render(cachedX, cachedY, cachedWidth, cachedHeight)
        renderer {
            color(color)
            rect(cachedX, cachedY, cachedWidth, cachedHeight, radius)
            border?.render(cachedX, cachedY, cachedWidth, cachedHeight, radius)
        }
    }

    fun mousePressed(mouseX: Float, mouseY: Float) {
        if (mouseX >= cachedX && mouseY >= cachedY && mouseX <= cachedX + cachedWidth && mouseY <= cachedY + cachedHeight) {
            selected = true
        }
    }

    fun release() {
        selected = false
    }

    fun mouseMoved(mouseX: Float, mouseY: Float) {
        if (selected) {

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
        it.x = x?.copy()
        it.y = y?.copy()
        it.width = width?.copy()
        it.height = height?.copy()
    }
}