package net.prismclient.aether.ui.component.type.layout.container

import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet

/**
 * [UIContainer] is the superclass for all layouts. It introduces scrollbars
 * into the component, so content outside of the frame can be viewed.
 *
 * [UIContainer] itself can also be created with [UIContainerSheet] as the sheet.
 *
 * @author sen
 * @since 5/12/2022
 */
open class UIContainer<T : UIContainerSheet>(style: String) : UIFrame<T>(style) {
    var horizontalScrollbarSelected = false
        protected set
    var verticalScrollbarSelected = false
        protected set

    var expandedWidth = 0f
    var expandedHeight = 0f

    override fun updateLayout() {}

    override fun update() {
        super.update()

        // Calculate the distance of the components
        // and find the largest of them on both axes
        var w = 0f
        var h = 0f

        for (i in 0 until components.size) {
            val c = components[i]
            w = (c.relX + c.relWidth).coerceAtLeast(w)
            h = (c.relY + c.relHeight).coerceAtLeast(h)
        }

        val x = if (style.clipContent) 0f else relX
        val y = if (style.clipContent) 0f else relY

        expandedWidth = (w - relWidth - x).coerceAtLeast(0f)
        expandedHeight = (h - relHeight - y).coerceAtLeast(0f)

        updateScrollbar()
    }

    open fun updateScrollbar() {
        style.scrollbarX.update(this)
        style.scrollbarY.update(this)
    }

    open fun renderScrollbar() {
        style.scrollbarX.render()
        style.scrollbarY.render()
    }

    override fun render() {
        super.render()
        renderScrollbar()
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float) {
        super.mouseClicked(mouseX, mouseY)
        style.scrollbarX.mousePressed(mouseX, mouseY)
        style.scrollbarY.mousePressed(mouseX, mouseY)
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        style.scrollbarX.release()
        style.scrollbarY.release()
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        style.scrollbarX.mouseMoved(mouseX, mouseY)
        style.scrollbarY.mouseMoved(mouseX, mouseY)
    }
}