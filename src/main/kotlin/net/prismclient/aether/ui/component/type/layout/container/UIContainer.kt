package net.prismclient.aether.ui.component.type.layout.container

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.interfaces.UILayout
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIFocusable

/**
 * [UIContainer] is the default implementation for [UIFrame]. It introduces
 * scrollbars which automatically resize to content being added/removed. It is
 * considered to be a [UIFocusable], so when the mouse is scrolled within the
 * container the focused component will become this.
 *
 *
 *
 * @author sen
 * @since 5/12/2022
 */
open class UIContainer<T : UIContainerSheet>(style: String?) : UIFrame<T>(style), UIFocusable, UILayout {
    /**
     * How sensitive the scrolling will be
     */
    var scrollSensitivity: Float = 10f

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

    override fun update() {
        super.update()
        updateLayout()

        // Calculate the distance of the components
        // and find the largest of them on both axes
        var w = 0f
        var h = 0f

        for (i in 0 until components.size) {
            val c = components[i]
            w = (c.relX + c.relWidth + c.marginRight).coerceAtLeast(w)
            h = (c.relY + c.relHeight + c.marginBottom).coerceAtLeast(h)
        }

        val x = if (style.useFBO) 0f else relX
        val y = if (style.useFBO) 0f else relY

        expandedWidth = (w - relWidth - x).coerceAtLeast(0f)
        expandedHeight = (h - relHeight - y).coerceAtLeast(0f)

        updateScrollbar()
    }

    override fun updateLayout() {
        components.forEach { it.update() }
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
        if (style.useFBO && (requiresUpdate || !style.optimizeRenderer)) {
            renderer {
                if (fbo == null) updateFBO()
                fbo!!.renderToFramebuffer {
                    translate(
                        -(style.horizontalScrollbar.value * expandedWidth),
                        -(style.verticalScrollbar.value * expandedHeight)
                    ) {
                        components.forEach(UIComponent<*>::render)
                    }
                }
            }
            requiresUpdate = false
        }
    }

    override fun renderComponent() {
        if (style.useFBO) {
            renderer {
                path {
                    imagePattern(fbo!!.imagePattern, relX, relY, relWidth, relHeight, 0f, 1f)
                    rect(relX, relY, relWidth, relHeight)
                }.fillPaint()
            }
        } else {
            renderer {
                if (style.clipContent) {
                    scissor(relX, relY, relWidth, relHeight) {
                        translate(-(style.horizontalScrollbar.value * expandedWidth), -(style.verticalScrollbar.value * expandedHeight)) {
                            components.forEach(UIComponent<*>::render)
                        }
                    }
                } else {
                    translate(-(style.horizontalScrollbar.value * expandedWidth), -(style.verticalScrollbar.value * expandedHeight)) {
                        components.forEach(UIComponent<*>::render)
                    }
                }
            }
        }
    }

    override fun render() {
        super.render()
        renderScrollbar()
    }

    override fun updateAnimation() {
        if (animations != null) {
            animations!!.forEach { it.value.update() }
            animations!!.entries.removeIf { it.value.isCompleted }
            if (animations!!.isEmpty())
                animations = null
            updateLayout()
            components.forEach { it.requestUpdate() }
        }
    }

    override fun mousePressed(event: UIMouseEvent) {
        super.mousePressed(event)
//        if (style.verticalScrollbar.mousePressed() || style.horizontalScrollbar.mousePressed()) focus()
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
        style.verticalScrollbar.mouseMoved()
        style.horizontalScrollbar.mouseMoved()
        if (style.verticalScrollbar.selected || style.horizontalScrollbar.selected)
            requestUpdate()
    }

    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        if (isFocused()) {
            style.verticalScrollbar.value -= ((scrollAmount * scrollSensitivity) / style.verticalScrollbar.cachedHeight)
            mouseMoved(mouseX, mouseY)
        }
        super.mouseScrolled(mouseX, mouseY, scrollAmount)
    }
}