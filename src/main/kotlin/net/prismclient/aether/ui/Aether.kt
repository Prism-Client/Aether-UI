package net.prismclient.aether.ui

import net.prismclient.aether.ui.Aether.Properties
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.controller.UIController
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.input.UIKey.*
import net.prismclient.aether.ui.util.input.UIKeyAction.*
import net.prismclient.aether.ui.util.input.UIModifierKey
import net.prismclient.aether.ui.util.interfaces.UIFocusable
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * [Aether] is the core of the Aether UI. It is responsible for managing the entirety
 * of the library. It controls and manages what to do with input and rendering. It holds
 * what would be the equivalent of the components within a UIScreen. Most functions within
 * this class should be invoked to properly render and handle input. See the docs for more
 * information. The components and frames are stored here, and not in a screen. Most input
 * is also handled here, such as bubbling and scroll calculations.
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
open class Aether(renderer: UIRenderer) {
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

    /**
     * Used to track the time between each frame framebuffer update. Frames with
     * useFBO enabled are automatically updated every second.
     */
    protected var lastUpdate: Long = 0

    init {
        instance = this
        Properties.renderer = renderer
        UIProvider.initialize(renderer)
    }

    /**
     * Invoked when an action on the window (such as resizing or moving) is performed. The
     * [width] and [height] of the screen must be passed. Leave [devicePxRatio] as 1.0 if you do
     * not care about retina displays. If you do, pass the content scale.
     */
    open fun update(width: Float, height: Float, devicePxRatio: Float) {
        updateSize(width, height, devicePxRatio)
        updateListeners?.forEach { it.value.run() }
        components?.forEach { it.update() }
        frames?.forEach { it.update() }
    }

    /**
     * This must be invoked before the rendering of the screen. It updates
     * all active frames that are in need of a frame redraw or update.
     */
    open fun renderFrames() {
        renderer {
            val shouldUpdate = if (lastUpdate + 1000L < System.currentTimeMillis()) {
                lastUpdate = System.currentTimeMillis()
                true
            } else false
            frames?.forEach {
                if (shouldUpdate) it.requestUpdate()
                it.renderContent()
            }
        }
    }

    /**
     * Invoked when the screen should be rendered.
     */
    open fun render() {
        renderer {
            if (activeScreen != null) {
                beginFrame(width, height, devicePxRatio)
                for (i in 0 until components!!.size) {
                    val component = components!![i]
                    if (component.visible) component.render()
                }
                endFrame()
            }
        }
    }

    /**
     * Invoked when the mouse is moved. Invokes all components regardless of their
     * eligibility to be focused or bubbled. The [Properties.mouseX] and [Properties.mouseY]
     * properties can be found in [Aether.Properties].
     */
    fun mouseMoved(mouseX: Float, mouseY: Float) {
        updateMouse(mouseX, mouseY)
        mouseMoveListeners?.forEach { it.value.run() }
        if (activeScreen != null) for (i in 0 until components!!.size) components!![i].mouseMoved(mouseX, mouseY)
    }

    /**
     * This method is invoked when the mouse is pressed down. [mouseButton] represents the
     * button which was pressed. 0, as the left button, 1 as the right, and 2 as the middle
     * respectively. [isRelease] should be true when the button is released
     *
     * This is a propagating event when [isRelease] is false so the most inner component will
     * have itUIComponent<T> invoked, and it will go up from there.
     *
     * @see mouseScrolled
     */
    fun mouseChanged(mouseButton: Int, isRelease: Boolean) {
        if (isRelease) {
            mouseReleasedListeners?.forEach { it.value.run() }
            components?.forEach { it.mouseReleased(mouseX, mouseY) }
            return
        }

        // Defocus the active component if it's not within the bounds of it
        if (focusedComponent != null) {
            if (!(focusedComponent as UIComponent<*>).isMouseInsideBounds()) {
                (focusedComponent as UIComponent<*>).defocus()
                focusedComponent = null
            }
        }

        mousePressedListeners?.forEach { it.value.run() }

        /**
         * Iterates through the children of a component and invokes
         * the mousePressed method if the component is within the
         * mouse coordinates. Index is the index of the component.
         */
        fun peek(list: ArrayList<UIComponent<*>>, index: Int, clickCount: Int): Boolean {
            var component: UIComponent<*>? = null

            for (i in index until list.size) {
                val child = list[i]
                val check = child.isMouseInsideBounds()
                if (child.isMouseInsideBounds() || !child.style.clipContent) {
                    if (child.childrenCount > 0) {
                        if (peek(
                                if (child is UIFrame) child.components else list,
                                i + if (child is UIFrame) 0 else 1,
                                clickCount
                            )
                        ) return true
                    }
                    if (check) component = child
                }
            }

            return if (component != null) {
                component.focus()
                component.mousePressed(UIMouseEvent(mouseX, mouseY, mouseButton, clickCount))
                true
            } else false
        }

        // Handle count
        val clickCount = 1

        var i = 0
        var c: UIComponent<*>? = null

        while (i < (components?.size ?: 0)) {
            val child = components!![i]

            if (child.isMouseInsideBounds()) {
                if (child.childrenCount > 0) {
                    if (peek(
                            if (child is UIFrame) child.components else components!!,
                            if (child is UIFrame) 0 else i,
                            clickCount
                        )
                    ) return
                    i += child.childrenCount
                }
                c = child
            }
            i++
        }
        c?.focus()
        c?.mousePressed(UIMouseEvent(mouseX, mouseY, mouseButton, clickCount))
    }

    /**
     * Invoked when a key has been pressed. This only invokes the focused component.
     *
     * @param character The key which was pressed or '\u0000'
     * @see updateModifierKey To update keys such as Shift, Alt, Tab etc...
     */
    fun keyPressed(character: Char) {
        keyPressListeners?.forEach { it.value.accept(character) }
        (focusedComponent as? UIComponent<*>)?.keyPressed(character)
    }

    /**
     * Invoked when the mouse wheel is scrolled. Invokes all components regardless
     * of their eligibility to be focused or bubbled.
     */
    open fun mouseScrolled(scrollAmount: Float) {
        tryFocus()
        mouseScrollListeners?.forEach { it.value.accept(scrollAmount) }
        components?.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
    }

    /**
     * The companion object of [Aether]. It contains properties useful to components
     * such as the width and height of the window. It also has utility functions such
     * as de-focusing the focused component and adding listeners to input.
     */
    companion object Properties {
        @JvmStatic
        var debug: Boolean = true

        @JvmStatic
        lateinit var instance: Aether
            protected set

        @JvmStatic
        lateinit var renderer: UIRenderer
            protected set

        @JvmStatic
        var activeScreen: UIScreen? = null
            protected set

        /**
         * The focused component (if applicable).
         *
         * @see focus
         * @see defocus
         * @see tryFocus
         */
        @JvmStatic
        var focusedComponent: UIFocusable? = null
            protected set

        /**
         * The width of the screen. It can be set via [update]
         */
        @JvmStatic
        var width: Float = 0f
            protected set

        /**
         * The width of the screen. It can be set via [update]
         */
        @JvmStatic
        var height: Float = 0f
            protected set

        /**
         * The device pixel ratio. It can be set via [update]. It is the equivalent of content scale.
         */
        @JvmStatic
        var devicePxRatio: Float = 1f
            protected set

        /**
         * The x position of the mouse relative to the screen
         */
        @JvmStatic
        var mouseX: Float = 0f
            protected set

        /**
         * The y position of the mouse relative to the screen
         */
        @JvmStatic
        var mouseY: Float = 0f
            protected set

        /**
         * Invoked whenever the layout needs to be updated. This can be when the screen
         * is resized or created. Invoked prior to components.
         */
        @JvmStatic
        var updateListeners: HashMap<String, Runnable>? = null
            protected set

        /**
         * The listeners for then the mouse is moved. Invoked prior to components.
         */
        @JvmStatic
        var mouseMoveListeners: HashMap<String, Runnable>? = null
            protected set

        /**
         * Invoked when the mouse is pressed. Invoked prior to components.
         */
        @JvmStatic
        var mousePressedListeners: HashMap<String, Runnable>? = null
            protected set

        /**
         * Invoked when the mouse is released. Invoked prior to components.
         */
        @JvmStatic
        var mouseReleasedListeners: HashMap<String, Runnable>? = null
            protected set

        /**
         * Invoked when a key is pressed. Invoked prior to components.
         */
        @JvmStatic
        var keyPressListeners: HashMap<String, Consumer<Char>>? = null
            protected set

        /**
         * Invoked when the mouse is scrolled. Invoked prior to components.
         */
        @JvmStatic
        var mouseScrollListeners: HashMap<String, Consumer<Float>>? = null
            protected set

        /**
         * Invoked when the screen is deleted. This is used to deallocate listeners added to UICore.
         */
        @JvmStatic
        var deallocationListeners: HashMap<String, Runnable>? = null
            protected set

        /**
         * The list of modifier keys. The value is if the key is pressed
         *
         * @see UIModifierKey
         */
        @JvmStatic
        var modifierKeys: EnumMap<UIModifierKey, Boolean> = EnumMap(UIModifierKey::class.java)

        @JvmStatic
        var modifierKeyListeners: HashMap<String, BiConsumer<UIModifierKey, Boolean>> = hashMapOf()

        init {
            // Populate the modifier keys
            UIModifierKey.values().forEach {
                modifierKeys[it] = true
            }
        }

        /**
         * Sets the [width], [height], and [devicePxRatio] to the given values
         *
         * @param devicePxRatio Must be at least 1f. If not, it will be set to 1f.
         */
        @JvmStatic
        fun updateSize(width: Float, height: Float, devicePxRatio: Float) {
            this.width = width
            this.height = height
            this.devicePxRatio = devicePxRatio.coerceAtLeast(1f)
        }

        /**
         * Sets the [mouseX] and [mouseY] to the given values
         */
        @JvmStatic
        fun updateMouse(mouseX: Float, mouseY: Float) {
            this.mouseX = mouseX
            this.mouseY = mouseY
        }

        /**
         * Updates the given [modifierKey] to the given [value].
         */
        @JvmStatic
        fun updateModifierKey(modifierKey: UIModifierKey, value: Boolean) {
            modifierKeys[modifierKey] = value
            modifierKeyListeners.forEach { it.value.accept(modifierKey, value) }
        }

        /**
         * Adds an update listener with the given [eventName].
         */
        @JvmStatic
        fun onUpdate(eventName: String, event: Runnable) {
            updateListeners = updateListeners ?: hashMapOf()
            updateListeners!![eventName] = event
        }

        /**
         * Adds a mouse move listener with the given [eventName]. The [mouseX] and [mouseY] properties
         * can be found within the companion of this class.
         */
        @JvmStatic
        fun onMouseMove(eventName: String, event: Runnable) {
            mouseMoveListeners = mouseMoveListeners ?: hashMapOf()
            mouseMoveListeners!![eventName] = event
        }

        /**
         * Adds a mouse pressed listener with the given [eventName].
         */
        @JvmStatic
        fun onMousePressed(eventName: String, event: Runnable) {
            mousePressedListeners = mousePressedListeners ?: hashMapOf()
            mousePressedListeners!![eventName] = event
        }

        /**
         * Adds a mouse released listener with the given [eventName].
         */
        @JvmStatic
        fun onMouseReleased(eventName: String, event: Runnable) {
            mouseReleasedListeners = mouseReleasedListeners ?: hashMapOf()
            mouseReleasedListeners!![eventName] = event
        }

        /**
         * Adds a key pressed listener with the given [eventName].
         */
        @JvmStatic
        fun onKeyPressed(eventName: String, event: Consumer<Char>) {
            keyPressListeners = keyPressListeners ?: hashMapOf()
            keyPressListeners!![eventName] = event
        }

        /**
         * Adds a mouse scroll listener with the given [eventName].
         */
        @JvmStatic
        fun onMouseScrolled(eventName: String, event: Consumer<Float>) {
            mouseScrollListeners = mouseScrollListeners ?: hashMapOf()
            mouseScrollListeners!![eventName] = event
        }

        @JvmStatic
        fun onDeallocation(eventName: String, event: Runnable) {
            deallocationListeners = deallocationListeners ?: hashMapOf()
            deallocationListeners!![eventName] = event
        }

        /**
         * Adds an event listener to [modifierKeyListeners] which is invoked when a modifier key is enabled / disabled
         */
        @JvmStatic
        fun onModifierKeyChange(name: String, event: BiConsumer<UIModifierKey, Boolean>) {
            modifierKeyListeners[name] = event
        }

        /**
         * Sets the active screen to the given [screen] and creates [components] and [frames].
         */
        @JvmStatic
        fun displayScreen(screen: UIScreen) {
            if (activeScreen != null) deallocateComponents()

            activeScreen = screen
            instance.components = ArrayList()
            instance.frames = ArrayList()
            instance.controllers = ArrayList()
            screen.build()
            instance.update(width, height, devicePxRatio)
        }

        /**
         * Deallocates everything necessary and removes the active screen.
         */
        @JvmStatic
        fun closeScreen() {
            if (activeScreen != null) {
                deallocateComponents()
                activeScreen = null
                instance.components = null
                instance.frames = null
                instance.controllers = null
            }
        }

        /**
         * Focuses the component. Please use [UIComponent.focus] instead.
         */
        @JvmStatic
        fun focus(component: UIFocusable) {
            // Check if the given value is a valid instance of UIComponent
            try {
                component as UIComponent<*>
            } catch (castException: ClassCastException) {
                throw RuntimeException("When trying to focus, the provided value is not an instance of UIComponent. Make sure you are only using the UIFocus interface to focus UIComponents.")
            }
            focusedComponent = component
        }

        /**
         * Defocuses the component. Please use [UIComponent.defocus] instead.
         */
        @JvmStatic
        fun defocus() {
            focusedComponent = null
        }

        /**
         * Aether will try to attempt to focus a scrollbar (if available)
         */
        @JvmStatic
        fun tryFocus() {
            if (activeScreen == null) return

            fun peek(contain: UIContainer<*>): Boolean {
                var component: UIContainer<*>? = null
                for (i in 0 until contain.components.size) {
                    val container = contain.components[i] as? UIContainer<*> ?: continue
                    if (container.isMouseInsideBounds() && container.expandedHeight > 0f && container.style.overflowY != UIContainerSheet.Overflow.None) {
                        if (peek(container)) return true
                        component = container
                    }
                }
                return if (component != null) {
                    component!!.focus()
                    true
                } else false
            }

            var component: UIContainer<*>? = null

            // Find the deepest UIContainer within the mouse's bounds
            for (i in 0 until instance.frames!!.size) {
                // UIContainers are what control scrolling, so
                // if it is not an instance of it, skip and continue
                val container = instance.frames!![i] as? UIContainer<*> ?: continue
                if (container.isMouseInsideBounds() && container.expandedHeight > 0f && container.style.overflowY != UIContainerSheet.Overflow.None) {
                    // Iterate through the frame to see if there are more
                    // containers with it. If there are, it will pass true
                    // and this function will return, else this will be invoked
                    if (peek(container)) return
                    component = container
                }
            }

            // If a container was found, then focus it
            if (component != null) component!!.focus()
        }

        /**
         * Invoked when the screen is changed and there was an existing one.
         *
         * @throws NullPointerException If the components list of this is null
         */
        fun deallocateComponents() {
            UIProvider.resetStyles()
            instance.components!!.forEach { it.deallocate() }
            deallocationListeners?.forEach { it.value.run() }
            deallocationListeners?.clear()
        }
    }
}