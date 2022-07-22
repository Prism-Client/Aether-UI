package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.frame.UIFrameLayout
import net.prismclient.aether.ui.component.util.interfaces.UILayout
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.renderer.impl.scrollbar.UIScrollbar
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIFocusable

/**
 * [UIContainer] is the default implementation for [UIFrame] and [UILayout]. It introduces scrollbars which automatically
 * resize to content being added/removed; furthermore, it is considered the base class of layouts. The container does nothing
 * to the layouts. It is also considered to be a [UIFocusable], so when the mouse is scrolled within the container the focused
 * component will become this.
 *
 * @author sen
 * @since 1.0
 */
open class UIContainer<T : UIContainerSheet> : UIFrameLayout<T>(), UIFocusable, UILayout {
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

    open fun updateScrollbar() {
        style.verticalScrollbar.update(this)
        style.horizontalScrollbar.update(this)
    }

    open fun renderScrollbar() {
        style.verticalScrollbar.render()
        style.horizontalScrollbar.render()
    }

    override fun renderContent() {
        if (style.useFBO) {
//        if (style.useFBO && (requiresUpdate || !style.optimizeRenderer)) {
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
        if (relWidth == 0f || relHeight == 0f)
            return
        renderer {
            if (style.useFBO) {
                color(-1)
                path {
                    imagePattern(fbo!!.imagePattern, relX, relY, relWidth, relHeight, 0f, 1f)
                    rect(relX, relY, relWidth, relHeight, style.background?.radius)
                }.fillPaint()
            } else {
                if (style.clipContent) {
                    scissor(relX, relY, relWidth, relHeight) {
                        translate(
                            -(style.horizontalScrollbar.value * expandedWidth),
                            -(style.verticalScrollbar.value * expandedHeight)
                        ) {
                            components.forEach(UIComponent<*>::render)
                        }
                    }
                } else {
                    translate(
                        -(style.horizontalScrollbar.value * expandedWidth),
                        -(style.verticalScrollbar.value * expandedHeight)
                    ) {
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
            updateParentLayout()
//            components.forEach { it.requestUpdate() }
        }
    }

    override fun mousePressed(event: UIMouseEvent) {
        super.mousePressed(event)
        if (style.verticalScrollbar.mousePressed() || style.horizontalScrollbar.mousePressed()) focus()
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
//        if (style.verticalScrollbar.selected || style.horizontalScrollbar.selected)
//            requestUpdate()
    }

    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        if (isFocused()) {
            style.verticalScrollbar.value -= ((scrollAmount * scrollSensitivity) / style.verticalScrollbar.cachedHeight)
            mouseMoved(mouseX, mouseY)
        }
        super.mouseScrolled(mouseX, mouseY, scrollAmount)
    }

    @Suppress("UNCHECKED_CAST")
    override fun createsStyle(): T = UIContainerSheet() as T
}

open class UIContainerSheet : UIFrameSheet() {
    /**
     * Describes when to introduce the scrollbar
     *
     * @see Overflow
     */
    var overflowX: Overflow = Overflow.Auto

    /**
     * Describes when to introduce the scrollbar
     *
     * @see Overflow
     */
    var overflowY: Overflow = Overflow.Auto

    var scrollBehaviour: ScrollBehaviour = ScrollBehaviour.Fixed

    var verticalScrollbar: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Vertical)
    var horizontalScrollbar: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Horizontal)

    /**
     * Creates a DSL block for the vertical scrollbar
     */
    inline fun verticalScrollbar(block: UIScrollbar.() -> Unit) = verticalScrollbar.block()

    /**
     * Creates a DSL block for the horizontal scrollbar
     */
    inline fun horizontalScrollbar(block: UIScrollbar.() -> Unit) = horizontalScrollbar.block()

    /**
     * [Overflow] defines what the vertical, and horizontal scrollbars
     * are supposed to do when content leaves the screen. Check the enum
     * documentation for more information.
     */
    enum class Overflow {
        /**
         * Does not introduce a scrollbar on the given axis
         */
        None,

        /**
         * Creates a scrollbar on the given axis regardless if content leaves the window
         */
        Always,

        /**
         * Like scroll, but only adds the scrollbar on the given axis if content leaves the window
         */
        Auto
    }

    /**
     * [ScrollBehaviour] defines how the encompassing container behaves when the mouse is scrolled.
     */
    enum class ScrollBehaviour {
        /**
         * Clamps the content
         */
        Fixed,

        /**
         * Introduces elasticity to the encompassing container.
         */
        Elastic
    }

    override fun apply(sheet: UIStyleSheet): UIContainerSheet {
        // Override the default apply function because
        // this is an inheritable class.
        this.immutableStyle = sheet.immutableStyle
        this.name = sheet.name

        this.background = sheet.background?.copy()
        this.font = sheet.font?.copy()

        this.x = sheet.x?.copy()
        this.y = sheet.y?.copy()
        this.width = sheet.width?.copy()
        this.height = sheet.height?.copy()

        this.padding = sheet.padding?.copy()
        this.margin = sheet.margin?.copy()
        this.anchor = sheet.anchor?.copy()
        this.clipContent = sheet.clipContent

        // Frame properties
        if (sheet is UIContainerSheet) {
            this.useFBO = sheet.useFBO
            this.optimizeRenderer = sheet.optimizeRenderer
            this.overflowX = sheet.overflowX
            this.overflowY = sheet.overflowY
            this.verticalScrollbar = sheet.verticalScrollbar.copy()
            this.horizontalScrollbar = sheet.horizontalScrollbar.copy()
        }

        return this
    }

    override fun copy(): UIContainerSheet = UIContainerSheet().apply(this)
}