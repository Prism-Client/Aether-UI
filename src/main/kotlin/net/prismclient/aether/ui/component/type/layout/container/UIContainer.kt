package net.prismclient.aether.ui.component.type.layout.container

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIFocusable

/**
 * [UIContainer] is the superclass for all layouts. It introduces scrollbars
 * into the component, so content outside the frame can be viewed.
 *
 * [UIContainer] itself can also be created with [UIContainerSheet] as the sheet.
 *
 * @author sen
 * @since 5/12/2022
 */
open class UIContainer<T : UIContainerSheet>(style: String) : UIFrame<T>(style), UIFocusable<UIContainer<T>> {
    var verticalScrollbarSelected = false
        protected set
    var horizontalScrollbarSelected = false
        protected set

    /**
     * The expanded width determined by components that leave the component bounds
     */
    var expandedWidth = 0f
        protected set

    /**
     * The expanded height determined by components that leave the components bounds
     */
    var expandedHeight = 0f
        protected set

    override fun updateLayout() {}

    override fun update() {
        super.update()

        // Calculate the distance of the components
        // and find the largest of them on both axes
        var w = 0f
        var h = 0f

        for (i in 0 until components.size) {
            val c = components[i]
            w = (c.relX + c.relWidth + c.marginRight).coerceAtLeast(w)
            h = (c.relY + c.relHeight + c.marginBottom).coerceAtLeast(h)
        }

        val x = if (style.clipContent) 0f else relX
        val y = if (style.clipContent) 0f else relY

        expandedWidth = (w - relWidth - x).coerceAtLeast(0f)
        expandedHeight = (h - relHeight - y).coerceAtLeast(0f)

        updateScrollbar()
    }

    open fun updateScrollbar() {
        style.verticalScrollbar.update(this)
        style.horizontalScrollbar.update(this)
    }

    open fun renderScrollbar() {
        style.verticalScrollbar.render()
        style.horizontalScrollbar.render()
    }

    override fun renderContent() {
        if (!style.clipContent)
            return
        updateAnimation()
        updateFramebuffer()
        // If frame size is less than or equal to 0 skip render, as FBO couldn't be created
        if (relWidth < 1f || relHeight < 1f)
            return
        renderer {
            renderContent(framebuffer!!) {
                translate(
                    -(style.horizontalScrollbar.value * expandedWidth),
                    -(style.verticalScrollbar.value * expandedHeight)
                ) {
                    components.forEach(UIComponent<*>::render)
                }
            }
        }
    }

    override fun render() {
        // Overwrite the UIFrame renderer to apply the translations
        // and update the FBO if needed
        if (!style.clipContent)
            updateAnimation()
        style.background?.render(relX, relY, relWidth, relHeight)
        renderer {
            if (!style.clipContent) {
                translate(
                    -(style.horizontalScrollbar.value * expandedWidth),
                    -(style.verticalScrollbar.value * expandedHeight)
                ) {
                    renderComponent()
                }
            } else {
                scissor(relX, relY, relWidth, relHeight) {
                    renderComponent()
                }
            }
        }
        renderScrollbar()
    }

//    override fun mousePressed(mouseX: Float, mouseY: Float) {
//        super.mousePressed(
//            mouseX - (style.horizontalScrollbar.value * expandedWidth),
//            mouseY - (style.verticalScrollbar.value * expandedHeight)
//        )
//        style.verticalScrollbar.mousePressed(mouseX, mouseY)
//        style.horizontalScrollbar.mousePressed(mouseX, mouseY)
//    }
    override fun mousePressed(event: UIMouseEvent) {
        super.mousePressed(event)
        verticalScrollbarSelected = style.verticalScrollbar.mousePressed(event.mouseX, event.mouseY)
        horizontalScrollbarSelected = style.horizontalScrollbar.mousePressed(event.mouseX, event.mouseY)

        if (verticalScrollbarSelected || horizontalScrollbarSelected)
            captureFocus()
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(
            mouseX - (style.horizontalScrollbar.value * expandedWidth),
            mouseY - (style.verticalScrollbar.value * expandedHeight)
        )
        style.verticalScrollbar.release()
        style.horizontalScrollbar.release()
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(
            mouseX - (style.horizontalScrollbar.value * expandedWidth),
            mouseY - (style.verticalScrollbar.value * expandedHeight)
        )
        style.verticalScrollbar.mouseMoved(mouseX, mouseY)
        style.horizontalScrollbar.mouseMoved(mouseX, mouseY)
    }

    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        super.mouseScrolled(mouseX, mouseY, scrollAmount)
        if (isFocused()) {
            style.verticalScrollbar.value += ((scrollAmount * 4f) / style.verticalScrollbar.cachedHeight)
            mouseMoved(mouseX, mouseY)
        }
    }
}