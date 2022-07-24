package net.prismclient.aether.ui.component

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.type.frame.UIFrameLayout
import net.prismclient.aether.ui.component.type.layout.UIContainer
import net.prismclient.aether.ui.component.type.layout.UIContainerSheet
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.UIFrameSheet
import net.prismclient.aether.ui.component.util.interfaces.UILayout
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.Block
import net.prismclient.aether.ui.util.extensions.calculate
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIFocusable
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * [UIComponent] is the superclass of all components. It accepts a [style], a
 * string linked to an instance of [UIStyleSheet] specific to the component. A
 * [UIStyleSheet] holds properties such as the desired x and y positions, background,
 * and other properties depending on the type of [UIStyleSheet]. It is an inheritable
 * class, and certain components may require a specific type of it to work correctly.
 *
 * [UIComponent] also has [x], [y], [width] and [height] properties, known as the
 * absolute values. The [relX], [relY], [relWidth] and [relHeight], are known as relative
 * values. The relative values are the normal property, except with the bounds of the
 * component calculated and applied to it via the [calculateBounds] function. Bounds
 * include the padding and margin properties of the component. Classes such as [UIBackground]
 * render the background to the relative properties, while other classes such as [UIFont__]
 * render to the absolute properties.
 *
 * @author sen
 * @since 1.0
 */
@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "LeakingThis")
abstract class UIComponent<T : UIStyleSheet> {
    /**
     * The style of the component.
     */
    lateinit var style: T

    /**
     * The parent of this component. If the parent is set, the origin point (0, 0) is the parent's position
     */
    var parent: UIComponent<*>? = null
        set(value) {
            if (field != null) field!!.childrenCount--
            if (value != null) value.childrenCount++
            field = value
        }
    var animations: HashMap<String, UIAnimation<T>>? = null

    /**
     * If true, the position will not update when this component is invoked. Generally this is automatically
     * enabled when added to layouts.
     */
    var overridden = false

    /**
     * If false the component will not be rendered.
     */
    var visible = true

    /** Component plot properties **/
    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f
    var relX = 0f
    var relY = 0f
    var relWidth = 0f
    var relHeight = 0f

    /** Cached Properties **/
    var paddingTop = 0f
    var paddingRight = 0f
    var paddingBottom = 0f
    var paddingLeft = 0f

    var marginTop = 0f
    var marginRight = 0f
    var marginBottom = 0f
    var marginLeft = 0f

    var anchorX = 0f
    var anchorY = 0f

    /**
     * Is true if the mouse was inside this component
     */
    var wasInside = false

    /**
     * The amount of children that this component has (increased/decreased by the setting of parent)
     */
    var childrenCount = 0

    /** Listeners **/
    /**
     * The listeners for when the component is first initialized. This is only invoked once.
     *
     * @see onInitialization
     */
    var initializationListeners: HashMap<String, Consumer<UIComponent<T>>>? = null
        protected set

    /**
     * The listeners for when the component is updated. This is generally called when the
     * screen is resized.
     *
     * @see update
     * @see onUpdate
     */
    var updateListeners: HashMap<String, Consumer<UIComponent<T>>>? = null
        protected set

    /**
     * The listeners for focus/defocus events. The first parameter of the [BiConsumer] is the
     * component and the second is if the component was focused
     *
     * @see onFocus
     */
    var focusListeners: HashMap<String, BiConsumer<UIComponent<T>, Boolean>>? = null
        protected set

    /**
     * The listeners for then the mouse is moved. This is not a bubbling event.
     *
     * @see mouseMoveListeners
     */
    var mouseMoveListeners: HashMap<String, Consumer<UIComponent<T>>>? = null
        protected set

    /**
     * The listeners for when the component is pressed. This is a bubbling event.
     *
     * [See Bubbling Events](https://aether.prismclient.net/components/bubbling-events)
     * @see mousePressedListeners
     */
    var mousePressedListeners: HashMap<String, Consumer<UIComponent<T>>>? = null
        protected set

    /**
     * The listeners for when the mouse is released. Despite being the opposite of
     * [mousePressedListeners], this is NOT a bubbling event.
     *
     * @see onMouseReleased
     */
    var mouseReleasedListeners: HashMap<String, Consumer<UIComponent<T>>>? = null
        protected set

    /**
     * The listeners for when the mouse enters this component. The mouse considered being inside
     * is if it passes the check for it being within these bounds and the parent(s) of this. This
     * is not a bubbling event.
     *
     * @see onMouseEnter
     */
    var mouseEnteredListeners: HashMap<String, Consumer<UIComponent<T>>>? = null
        protected set

    /**
     * The opposite of [mouseEnteredListeners]. Invoked when the mouse leaves this component. This
     * is not a bubbling event.
     *
     * @see onMouseLeave
     */
    var mouseLeaveListeners: HashMap<String, Consumer<UIComponent<T>>>? = null
        protected set

    /**
     * Invoked when a key is pressed. This must be focused or a parent of a component
     * that is focused to be invoked. This is not a bubbling event.
     *
     * @see UIFocusable
     * @see keyPressListeners
     */
    var keyPressListeners: HashMap<String, BiConsumer<UIComponent<T>, Char>>? = null
        protected set

    /**
     * Invoked when the mouse is scrolled. This must be focused or a parent of a component
     * that is focused to be invoked. This is a bubbling event.
     *
     * [See Bubbling Events](https://aether.prismclient.net/components/bubbling-events)
     * @see UIFocusable
     * @see onFocus
     */
    var mouseScrollListeners: HashMap<String, BiConsumer<UIComponent<T>, Float>>? = null
        protected set

    /**
     * Attempts to apply the style to the component. If the style is
     * empty, or null, a NullPointerException will be thrown when the
     * component is updated. A style must be defined at some point prior
     * to that invocation.
     *
     * To define a style at the component scope see [net.prismclient.aether.ui.util.style]
     *
     * @throws InvalidStyleSheetException If the style is not a valid style sheet of the given component
     */
    open fun applyStyle(style: String?) {
        if (style.isNullOrEmpty()) return

        // Attempt to apply the style provided to the component.
        // Throw a InvalidStyleException if the style is not valid.
        try {
            this.style = UIProvider.getStyle(style, false) as T
        } catch (exception: ClassCastException) {
            throw InvalidStyleSheetException(style, this)
        }
    }

    /**
     * Invoked one time, at initialization. This is when the component
     * is actively being added to the UIComponentDSL. This can be useful
     * for adding components to this one because the init method will place
     * the component before this one in the render list.
     */
    open fun initialize() {
        initializationListeners?.forEach { it.value.accept(this) }
    }

    /**
     * Invoked when the component is deleted because of the screen being closed. Any listeners attached to this
     * are automatically removed. However, listeners that have been added to UICore must be deleted.
     */
    open fun deallocate() {}

    /**
     * [update] is invoked when the component is created, and when the display
     * has been changed (resized). The component properties such as the position
     * and size should be calculated within these bounds.
     *
     * It is not ensured that the only time that the method is invoked is when the
     * display has been changed or when the component is created. Animations and layouts
     * might request for this method to be invoked.
     */
    open fun update() {
        if (!hasStyle()) {
            style = createsStyle()
        }

        calculateBounds()
        // Update the size, then the anchor, and then the position
        updateSize()
        updateAnchorPoint()
        updatePosition()

        // Update the relative values
        updateBounds()
        updateStyle()

        // Invoke the update listeners
        updateListeners?.forEach { it.value.accept(this) }
    }

    /**
     * Updates the styles anchor point. This should happen after [updateSize]
     * and before [updatePosition].
     */
    open fun updateAnchorPoint() {
        anchorX = calculate(style.anchor?.x, this, width, height, false)
        anchorY = calculate(style.anchor?.y, this, width, height, true)
    }

    /**
     * Invoked from the update method. If not [overridden], the component
     * will update its position relative to its parent
     */
    open fun updatePosition() {
        if (!overridden) {
            x = +style.x + getParentX() + marginLeft - anchorX
            y = -style.y + getParentY() + marginTop - anchorY
        }
    }

    /**
     * Updates the width and height of the component
     */
    open fun updateSize() {
        width = +style.width
        height = -style.height
    }

    /**
     * Calculates the padding and margin properties of the component. Should
     * be invoked after the component's position and size have been calculated.
     */
    open fun calculateBounds() {
        // Update padding
        paddingTop = -style.padding?.paddingTop
        paddingRight = +style.padding?.paddingRight
        paddingBottom = -style.padding?.paddingBottom
        paddingLeft = +style.padding?.paddingLeft

        // Update margin
        marginTop = -style.margin?.marginTop
        marginRight = +style.margin?.marginRight
        marginBottom = -style.margin?.marginBottom
        marginLeft = +style.margin?.marginLeft
    }

    /**
     * Updates the relative position and size of the component based on the
     * padding and margins of the component. This should be invoked after the
     * position, size, and bounds are calculated.
     */
    open fun updateBounds() {
        relX = x - paddingLeft
        relY = y - paddingTop
        relWidth = width + paddingLeft + paddingRight
        relHeight = height + paddingTop + paddingBottom
    }

    /**
     * Updates the shapes within the style sheet
     */
    open fun updateStyle() {
        style.background?.update(this)
        style.font?.update(this)
    }

    /**
     * Updates the active animation (if applicable)
     */
    open fun updateAnimation() {
        if (animations != null) {
            animations!!.forEach { it.value.update() }
            animations!!.entries.removeIf { it.value.isCompleted }
            if (animations!!.isEmpty()) {
                animations = null
            }
        }
    }

    /**
     * Render is the internal function of [UIComponent] which updates animations, renders the background,
     * and apply the content clipping to the component before calling [renderComponent]. Generally, this
     * should not be overridden unless there is a specific reason which is not covered by the [renderComponent].
     */
    open fun render() {
        if (!visible) return
        updateAnimation()
        style.background?.render()
        renderer {
            if (style.clipContent) {
                scissor(relX, relY, relWidth, relHeight) {
                    renderComponent()
                }
            } else renderComponent()
        }
    }

    /**
     * Invoked every time the component needs to be rendered. Any code to
     * render the component should be placed within this function.
     */
    abstract fun renderComponent()

    /**
     * Used to create a new instance of the style sheet provided, [T].
     */
    abstract fun createsStyle(): T

    /**
     * Returns true if [style] is intialized.
     */
    open fun hasStyle(): Boolean = this::style.isInitialized

    // -- Input -- //

    /**
     * Invoked when the mouse moves
     */
    open fun mouseMoved(mouseX: Float, mouseY: Float) {
        if (isMouseInsideBounds()) {
            if (!wasInside) {
                mouseEntered()
                wasInside = true
            }
//            requestUpdate()
        } else if (wasInside) {
            mouseLeft()
            wasInside = false
        }
        mouseMoveListeners?.forEach { it.value.accept(this) }
    }

    /**
     * Invoked when the mouse is pressed. This is a bubbling event.
     */
    open fun mousePressed(event: UIMouseEvent) {
        mousePressedListeners?.forEach { it.value.accept(this) }
        if (parent != null && !event.canceled) {
            event.propagationIndex++
            parent!!.mousePressed(event)
        }
    }

    /**
     * Invoked when the mouse is released.
     */
    open fun mouseReleased(mouseX: Float, mouseY: Float) {
        mouseReleasedListeners?.forEach { it.value.accept(this) }
    }

    /**
     * Invoked when the mouse enters this
     */
    protected open fun mouseEntered() {
        mouseEnteredListeners?.forEach { it.value.accept(this) }
    }

    /**
     * Invoked when the mouse leaves this
     */
    protected open fun mouseLeft() {
        mouseLeaveListeners?.forEach { it.value.accept(this) }
//        requestUpdate()
    }

    /**
     * Invoked when this is focused and a key is pressed
     */
    open fun keyPressed(character: Char) {
        keyPressListeners?.forEach { it.value.accept(this, character) }
    }

    /**
     * Invoked when the mouse is scrolled and this is focused
     */
    open fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        mouseScrollListeners?.forEach { it.value.accept(this, scrollAmount) }
    }

    // -- Event -- //

    /**
     * Invoked once on the initialization of the component.
     */
    @JvmOverloads
    open fun onInitialization(
        eventName: String = "Default-${initializationListeners?.size ?: 0}", event: Consumer<UIComponent<T>>
    ): UIComponent<T> {
        initializationListeners = initializationListeners ?: hashMapOf()
        initializationListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the component is updated. This is usually at the creation
     * and when the screen is resized. However, it is not ensured that it is
     * the only time that the method is invoked.
     */
    @JvmOverloads
    open fun onUpdate(
        eventName: String = "Default-${updateListeners?.size ?: 0}", event: Consumer<UIComponent<T>>
    ): UIComponent<T> {
        updateListeners = updateListeners ?: hashMapOf()
        updateListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the component is focused or de-focused. The [BiConsumer] is the component
     * and a boolean representing if it was focused or not focused.
     */
    @JvmOverloads
    open fun onFocus(
        eventName: String = "Default-${focusListeners?.size ?: 0}", event: BiConsumer<UIComponent<T>, Boolean>
    ): UIComponent<T> {
        focusListeners = focusListeners ?: hashMapOf()
        focusListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the mouse is moved
     */
    @JvmOverloads
    open fun onMouseMove(
        eventName: String = "Default-${mouseMoveListeners?.size ?: 0}", event: Consumer<UIComponent<T>>
    ): UIComponent<T> {
        mouseMoveListeners = mouseMoveListeners ?: hashMapOf()
        mouseMoveListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the mouse is pressed. This is a bubbling event.
     */
    @JvmOverloads
    open fun onMousePressed(
        eventName: String = "Default-${mousePressedListeners?.size ?: 0}", event: Consumer<UIComponent<T>>
    ): UIComponent<T> {
        mousePressedListeners = mousePressedListeners ?: hashMapOf()
        mousePressedListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the mouse is released.
     */
    @JvmOverloads
    open fun onMouseReleased(
        eventName: String = "Default-${mouseReleasedListeners?.size ?: 0}", event: Consumer<UIComponent<T>>
    ): UIComponent<T> {
        mouseReleasedListeners = mouseReleasedListeners ?: hashMapOf()
        mouseReleasedListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the mouse enters the component's bounding box.
     */
    @JvmOverloads
    open fun onMouseEnter(
        eventName: String = "Default-${mouseEnteredListeners?.size ?: 0}", event: Consumer<UIComponent<T>>
    ): UIComponent<T> {
        mouseEnteredListeners = mouseEnteredListeners ?: hashMapOf()
        mouseEnteredListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the mouse leaves the bounds of the component.
     */
    @JvmOverloads
    open fun onMouseLeave(
        eventName: String = "Default-${mouseEnteredListeners?.size ?: 0}", event: Consumer<UIComponent<T>>
    ): UIComponent<T> {
        mouseLeaveListeners = mouseLeaveListeners ?: hashMapOf()
        mouseLeaveListeners!![eventName] = event
        return this
    }

    @JvmOverloads
    open fun onKeyPress(
        eventName: String = "Default-${keyPressListeners?.size ?: 0}", event: BiConsumer<UIComponent<T>, Char>
    ): UIComponent<T> {
        keyPressListeners = keyPressListeners ?: hashMapOf()
        keyPressListeners!![eventName] = event
        return this
    }

    /**
     * Invoked when the mouse is scrolled.
     *
     * @param event The component and the amount scrolled
     */
    @JvmOverloads
    open fun onMouseScrolled(
        eventName: String = "Default-${mouseScrollListeners?.size ?: 0}", event: BiConsumer<UIComponent<T>, Float>
    ): UIComponent<T> {
        mouseScrollListeners = mouseScrollListeners ?: hashMapOf()
        mouseScrollListeners!![eventName] = event
        return this
    }

    /**
     * Returns the x position of the parent with consideration of it being a [UIFrame]
     */
    open fun getParentX() =
        if (parent != null) if (parent is UIFrame && ((parent as UIFrame).style as UIFrameSheet).useFBO) 0f else parent!!.x else 0f

    /**
     * Returns the y position of the parent with consideration of it being a [UIFrame]
     */
    open fun getParentY() =
        if (parent != null) if (parent is UIFrame && ((parent as UIFrame).style as UIFrameSheet).useFBO) 0f else parent!!.y else 0f

    /**
     * Returns the width of the component or the window if there is no parent
     */
    open fun getParentWidth() = if (parent != null) parent!!.width else Aether.width

    /**
     * Returns the height of the parent or the window if there is no parent
     */
    open fun getParentHeight() = if (parent != null) parent!!.height else Aether.height

    /**
     * Returns true if the mouse is inside the [relX], [relY], [relWidth], and [relHeight]
     */
    open fun isMouseInside() =
        (getMouseX() >= relX) && (getMouseY() >= relY) && (getMouseX() <= relX + relWidth) && (getMouseY() <= relY + relHeight)

    /**
     * Propagates through this, and up to check if the mouse is inside.
     * Returns true if the mouse is inside all the components that nest this.
     */
    open fun isMouseInsideBounds(): Boolean =
        if (parent != null && parent!!.style.clipContent && !parent!!.isMouseInsideBounds()) false else isMouseInside()

    /**
     * Returns the x position of the mouse with consideration to the parent
     */
    open fun getMouseX(): Float = Aether.mouseX - getParentXOffset()

    /**
     * Returns the y position of the mouse with consideration to the parent
     */
    open fun getMouseY(): Float = Aether.mouseY - getParentYOffset()

    /**
     * Returns the actual x position of this component rendered on screen. FBOs change the point of origin
     * back to 0, so the values that the component has might not reflect it's actual position on screen.
     */
    open fun getParentXOffset(): Float = if (parent is UIFrame) {
        ((if ((parent!!.style as UIFrameSheet).useFBO) {
            parent!!.relX
        } else 0f) + parent!!.getParentXOffset()) - if (parent is UIContainer) {
            (parent!!.style as UIContainerSheet).horizontalScrollbar.value * (parent as UIContainer).expandedWidth
        } else 0f
    } else 0f

    /**
     * Returns the actual y position of this component rendered on screen. FBOs change the point of origin
     * back to 0, so the values that the component has might not reflect it's actual position on screen.
     */
    open fun getParentYOffset(): Float = if (parent is UIFrame) {
        ((if ((parent!!.style as UIFrameSheet).useFBO) {
            parent!!.relY
        } else 0f) + parent!!.getParentYOffset()) - if (parent is UIContainer) {
            (parent!!.style as UIContainerSheet).verticalScrollbar.value * (parent as UIContainer).expandedHeight
        } else 0f
    } else 0f

    /**
     * Shorthand for calculating the x or width of this component
     */
    protected open operator fun UIUnit?.unaryPlus() = computeUnit(this, false)

    /**
     * Shorthand for calculation the y or height of this component
     */
    protected open operator fun UIUnit?.unaryMinus() = computeUnit(this, true)

    /**
     * Returns the computed version of the given unit based on this and the parent of this
     */
    open fun computeUnit(unit: UIUnit?, isY: Boolean) = calculate(unit, this, getParentWidth(), getParentHeight(), isY)

    /**
     * Returns true if this is an instance of [UIFocusable] and is focused
     */
    open fun isFocused() = if (this is UIFocusable) Aether.focusedComponent == this else false

    /**
     * Focuses this component if applicable
     */
    open fun focus() {
        if (this is UIFocusable) {
            Aether.focus(this)
            focusListeners?.forEach { it.value.accept(this, true) }
//            requestUpdate()
        }
    }

    /**
     * Removes the focus from this component
     */
    open fun defocus() {
        Aether.defocus()
        focusListeners?.forEach { it.value.accept(this, false) }
    }

    open fun updateParentLayout() {
        if (parent != null && parent is UILayout) {
            (parent!! as UILayout).updateLayout()
            parent!!.updateParentLayout()
        }
    }

    inline fun stylize(block: Block<T>) = style.block()

    /**
     * [UninitializedStyleSheetException] is thrown when the style sheet of
     * this was not initialized. The solution is to add a valid style sheet
     * to this component.
     *
     * @author sen
     * @since 1.0
     */
    class UninitializedStyleSheetException(component: UIComponent<*>?) :
        Exception("No stylesheet has been provided to $component")

    /**
     * [InvalidStyleSheetException] is thrown when a cast from
     * [UIStyleSheet] to [T] failed.
     *
     * The most common cause is that the provided style sheet is not an
     * instance of [T]. If the [UIComponent] is a custom implementation it
     * is possible that the copy function is not override in the class.
     *
     * @author sen
     * @since 1.0
     */
    class InvalidStyleSheetException(styleName: String, component: UIComponent<*>?) :
        Exception("Style of name \"$styleName\" is not a valid instance of $component. Is the style sheet passed an instance of the required style sheet for the component? If the style sheet is a custom implementation, please check if you have added the copy method which returns an instance of that style sheet.")
}