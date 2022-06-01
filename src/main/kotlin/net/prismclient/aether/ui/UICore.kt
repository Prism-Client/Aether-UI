package net.prismclient.aether.ui

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UICore] is the core of the Aether UI. It is responsible for managing the entirety
 * of the library. There a specific functions which must be invoked to properly make user
 * input work. You can find the methods <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Components.md#UICore">here (or in the @see)</a>
 *
 * @author sen
 * @since 1.0
 * @see <a href="https://github.com/Prism-Client/Aether-UI/blob/production/docs/Components.md#UICore">UICore documentation</a>
 */
open class UICore(val renderer: UIRenderer) {
    /**
     * All active components. The list is automatically created.
     */
    var components: ArrayList<UIComponent<*>>? = null
        protected set

    /**
     * All active frames. The frame is also added to the component list.
     */
    var frames: ArrayList<UIFrame<*>>? = null
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
        frames?.forEach { it.renderContent() }
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
        Properties.mouseX = mouseX
        Properties.mouseY = mouseY
        if (activeScreen != null) {
            for (i in 0 until components!!.size) components!![i].mouseMoved(mouseX, mouseY)
        }
    }

    /**
     * Invoked when the mouse was pressed down
     */
    fun mousePressed(mouseButton: Int) {
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
                component.mousePressed(UIMouseEvent(mouseX, mouseY, mouseButton, 0))
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
        c?.mousePressed(UIMouseEvent(mouseX, mouseY, 0, 0))
    }

    /**
     * Invoked when the mouse was released after being pressed
     */
    fun mouseReleased() {
        for (i in 0 until components!!.size) components!![i].mouseReleased(mouseX, mouseY)
    }

    fun keyPressed(key: Char) {
        // TODO: Implement
    }

    /**
     * Invoked when the mouse wheel is scrolled
     */
    open fun mouseScrolled(scrollAmount: Float) {
        components?.forEach { it.mouseScrolled(scrollAmount, mouseX, mouseY) }
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

        fun updateSize(width: Float, height: Float, devicePxRatio: Float) {
            this.width = width
            this.height = height
            this.devicePxRatio = devicePxRatio
        }

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

            screen.build()
            instance.frames = UIComponentDSL.getFrames()
            instance.components = UIComponentDSL.get()
            UIComponentDSL.finalize()
            instance.update(width, height, devicePxRatio)
        }
    }
}