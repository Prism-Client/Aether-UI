package net.prismclient.aether.ui

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.controller.UIController
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIFocusable

/**
 * [UICore] is the core of the Aether UI. It is responsible for managing the entirety
 * of the library. Most functions within this class should be invoked to properly render
 * and handle input. See the docs for more information. The components and frames are stored
 * here, and not in a screen. Most input is also handled here, such as bubbling and scroll
 * calculations.
 *
 * The companion class, [Properties], stores the general―properties―such as the size of
 * the window (provided by the implementation), and the position of the mouse. There are
 * also helper functions such as [Properties.displayScreen] to display the screen and also
 * functions [Properties.updateSize] and [Properties.updateMouse] to update the values without
 * invoking the [update], and [mouseMoved] functions.
 *
 * @author sen
 * @since 1.0
 * @see <a href="https://aether.prismclient.net/getting-started">UICore documentation</a>
 * @see UIProvider
 */
open class UICore(val renderer: UIRenderer) {
    /**
     * All components that are not nested within a frame
     *
     * @see frame
     */
    var components: ArrayList<UIComponent<*>>? = null
        protected set

    /**
     * All active frames. The frame is also added to the component list (If not nested).
     *
     * @see component
     */
    var frames: ArrayList<UIFrame<*>>? = null
        protected set

    /**
     * All active controllers.
     */
    var controllers: ArrayList<UIController<*>>? = null
        protected set
    
    init {
        instance = this
        UIProvider.initialize(renderer)
    }

    /**
     * Invoked when an action on the window (such as resizing or moving) is performed. The
     * [width] and [height] of the screen must be passed. Leave [devicePxRatio] as 1.0 if you do
     * not care about retina displays. If you do, pass the content scale.
     */
    open fun update(width: Float, height: Float, devicePxRatio: Float) {
        updateSize(width, height, devicePxRatio)
        components?.forEach { it.update() }
        frames?.forEach { it.update() }
        UIProvider.updateAnimationCache()
    }

    /**
     * This must be invoked before the rendering of the screen. It updates all active frames.
     */
    open fun renderFrames() {
        renderer {
            frames?.forEach {
                beginFrame(width, height, devicePxRatio)
                it.renderContent()
                endFrame()
            }
        }
    }

    /**
     * Invoked when the screen should be rendered.
     */
    open fun render() {
        renderer {
            beginFrame(width, height, devicePxRatio)
            if (activeScreen != null) {
                for (i in 0 until components!!.size) components!![i].render()
            }
            endFrame()
        }
    }

    /**
     * Invoked when the mouse is moved
     */
    fun mouseMoved(mouseX: Float, mouseY: Float) {
        updateMouse(mouseX, mouseY)

        // If the focused component isn't null, defocus the focused component
        // if the mouse is not within it's bounds
        if (focusedComponent != null) {
            if (!(focusedComponent as UIComponent<*>).check()) {
                (focusedComponent as UIComponent<*>).defocus()
                focusedComponent = null
            }
        }

        if (activeScreen != null) {
            for (i in 0 until components!!.size) components!![i].mouseMoved(mouseX, mouseY)
        }
    }

    /**
     * Invoked when the mouse was pressed down
     */
    fun mousePressed(mouseButton: Int, isRelease: Boolean) {
        if (isRelease) { // TODO: Mouse release propagation
            components?.forEach { it.mouseReleased(mouseX, mouseY) }
            return
        }

        /**
         * Iterates through the children of a component and invokes
         * the mousePressed method if the component is within the
         * mouse coordinates. Index is the index of the component.
         */
        fun peek(list: ArrayList<UIComponent<*>>, index: Int): Boolean {
            var component: UIComponent<*>? = null

            for (i in index until list.size) {
                val child = list[i]
                val check = child.check()
                if (child.check() || !child.style.clipContent) {
                    if (child.childrenCount > 0) {
                        if (peek(if (child is UIFrame) child.components else list, i + if (child is UIFrame) 0 else 1)) return true
                    }
                    if (check)
                        component = child
                }
            }

            return if (component != null) {
                component.mousePressed(UIMouseEvent(mouseX, mouseY, mouseButton, isRelease, 0))
                true
            } else false
        }

        var i = 0
        var c: UIComponent<*>? = null

        while (i < (components?.size ?: 0)) {
            val child = components!![i]

            if (child.check()) {
                if (child.childrenCount > 0) {
                    if (peek(if (child is UIFrame) child.components else components!!, if (child is UIFrame) 0 else i)) return
                    i += child.childrenCount
                }
                c = child
            }
            i++
        }
        c?.mousePressed(UIMouseEvent(mouseX, mouseY, 0, isRelease, 0))
    }

    fun keyPressed(key: Char) {
        // TODO: Implement
    }

    /**
     * Invoked when the mouse wheel is scrolled
     */
    open fun mouseScrolled(scrollAmount: Float) {
        tryFocus()
        components?.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
    }

    /**
     * The companion object of [UICore]. It contains properties useful to components
     * such as the width and height of the window.
     */
    companion object Properties {
        lateinit var instance: UICore
            protected set
        
        var activeScreen: UIScreen? = null
            protected set

        /**
         * The focused component (if applicable).
         *
         * @see focus
         * @see defocus
         * @see tryFocus
         */
        var focusedComponent: UIFocusable<*>? = null
            protected set

        /**
         * The width of the screen. It can be set via [update]
         */
        var width: Float = 0f
            protected set

        /**
         * The width of the screen. It can be set via [update]
         */
        var height: Float = 0f
            protected set

        /**
         * The device pixel ratio. It can be set via [update]. It is the equivalent of content scale.
         */
        var devicePxRatio: Float = 1f
            protected set

        /**
         * The x position of the mouse relative to the screen
         */
        var mouseX: Float = 0f
            protected set

        /**
         * The y position of the mouse relative to the screen
         */
        var mouseY: Float = 0f
            protected set

        /**
         * Sets the [width], [height], and [devicePxRatio] to the given values
         *
         * @param devicePxRatio Must be at least 1f. If not, it will be set to 1f.
         */
        fun updateSize(width: Float, height: Float, devicePxRatio: Float) {
            this.width = width
            this.height = height
            this.devicePxRatio = devicePxRatio.coerceAtLeast(1f)
        }

        /**
         * Sets the [mouseX] and [mouseY] to the given values
         */
        fun updateMouse(mouseX: Float, mouseY: Float) {
            this.mouseX = mouseX
            this.mouseY = mouseY
        }

        /**
         * Sets the active screen to the given [screen] and creates [components] and [frames].
         */
        fun displayScreen(screen: UIScreen) {
            activeScreen = screen
            instance.components = ArrayList()
            instance.frames = ArrayList()
            instance.controllers = ArrayList()

            screen.build()
            instance.frames = UIComponentDSL.getFrames()
            instance.components = UIComponentDSL.get()
            UIComponentDSL.finalize()
            instance.update(width, height, devicePxRatio)
        }

        /**
         * Focuses the component. Please use [UIComponent.focus] instead.
         */
        fun focus(component: UIFocusable<*>) {
            focusedComponent = component
        }

        /**
         * Defocuses the component. Please use [UIComponent.defocus] instead.
         */
        fun defocus() {
            focusedComponent = null
        }

        /**
         * Aether will try to attempt to focus a scrollbar (if available)
         */
        fun tryFocus() {
            if (activeScreen == null)
                return

            fun peek(contain: UIContainer<*>): Boolean {
                var component: UIContainer<*>? = null
                for (i in 0 until contain.components.size) {
                    val container = contain.components[i] as? UIContainer<*> ?: continue
                    if (container.check()) {
                        if (peek(container))
                            return true
                        component = container
                    }
                }
                return if (component != null) {
                    component!!.focus()
                    true
                } else false
            }

            var component: UIContainer<*>? = null

            for (i in 0 until instance.frames!!.size) {
                val frame = instance.frames!![i] as? UIContainer<*> ?: continue
                if (frame.check()) {
                    if (peek(frame)) return
                    component = frame
                }
            }

            if (component != null)
                component!!.focus()
        }
    }
}