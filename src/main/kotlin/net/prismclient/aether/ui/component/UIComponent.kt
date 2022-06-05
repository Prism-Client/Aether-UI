package net.prismclient.aether.ui.component

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.type.layout.styles.UIFrameSheet
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.renderer.impl.background.UIBackground
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.type.UIOperationUnit
import net.prismclient.aether.ui.util.interfaces.UIFocusable
import net.prismclient.aether.ui.unit.util.*
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.calculateY
import net.prismclient.aether.ui.util.extensions.renderer
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
 * render the background to the relative properties, while other classes such as [UIFont]
 * render to the absolute properties.
 *
 * @author sen
 * @since 1.0
 */
@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate", "LeakingThis")
abstract class UIComponent<T : UIStyleSheet>(style: String) {
    lateinit var style: T
    var parent: UIComponent<*>? = null
        set(value) {
            if (field != null)
                field!!.childrenCount--
            if (value != null)
                value.childrenCount++
            field = value
        }
    var animation: UIAnimation<*>? = null
    var overridden = false
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

    /**
     * The animation for when this component is hovered over
     *
     * @see hover
     */
    var hoverAnimation: String? = null
        protected set

    /**
     * The animation when the mouse leaves the component
     *
     * @see hover
     */
    var leaveAnimation: String? = null
        protected set

    /** Listeners **/
    protected var initializationListeners: MutableList<Consumer<UIComponent<*>>>? = null
    protected var updateListeners: MutableList<Consumer<UIComponent<*>>>? = null

    protected var mousePressedListeners: MutableList<Consumer<UIComponent<*>>>? = null
    protected var mouseReleasedListeners: MutableList<Consumer<UIComponent<*>>>? = null
    protected var mouseEnteredListeners: MutableList<Consumer<UIComponent<*>>>? = null
    protected var mouseLeaveListeners: MutableList<Consumer<UIComponent<*>>>? = null

    /**
     * The listeners for focus/defocus events. The first parameter of the [BiConsumer] is the
     * component and the second is if the component was focused
     */
    protected var focusListeners: MutableList<BiConsumer<UIComponent<*>, Boolean>>? = null

    init {
        applyStyle(style)
    }

    /**
     * Attempts to apply the style to the component.
     *
     * @throws NullPointerException If the style is not found
     * @throws InvalidStyleSheetException If the style is not a valid style sheet of the given component
     */
    open fun applyStyle(style: String) {
        animation = null
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
        initializationListeners?.forEach { it.accept(this) }
    }

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
        calculateBounds()
        // Update the size, then the anchor, and then the position
        updateSize()
        updateAnchorPoint()
        updatePosition()

        // Update the relative values
        updateBounds()
        updateStyle()

        updateListeners?.forEach { it.accept(this) }
    }

    /**
     * Updates the styles anchor point. This should happen after [updateSize]
     * and before [updatePosition].
     */
    open fun updateAnchorPoint() {
        anchorX = calculateUnitX(style.anchor?.x, width, false)
        anchorY = calculateUnitY(style.anchor?.y, height, false)
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
        if (animation != null) {
            animation!!.update()
            if (animation!!.completed) {
                animation = null
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

    /** Input **/

    open fun mouseMoved(mouseX: Float, mouseY: Float) {
        if (isMouseInsideBoundingBox()) {
            if (!wasInside) {
                mouseEntered()
                wasInside = true
            }
        } else if (wasInside) {
            mouseLeft()
            wasInside = false
        }
    }

    open fun mousePressed(event: UIMouseEvent) {
        mousePressedListeners?.forEach { it.accept(this) }
        if (parent != null && !event.canceled) {
            event.propagationIndex++
            parent!!.mousePressed(event)
        }
    }

    open fun mouseReleased(mouseX: Float, mouseY: Float) {
        mouseReleasedListeners?.forEach { it.accept(this) }
    }

    open fun keyPressed(key: UIKey, character: Char) {

    }

    open fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {

    }

    protected open fun mouseEntered() {
        mouseEnteredListeners?.forEach { it.accept(this) }
    }

    protected open fun mouseLeft() {
        mouseLeaveListeners?.forEach { it.accept(this) }
    }

    /** Event **/

    /**
     * Invoked once on the initialization of the component.
     */
    open fun onInitialization(event: Consumer<UIComponent<*>>): UIComponent<T> {
        initializationListeners = initializationListeners ?: mutableListOf()
        initializationListeners?.add(event)
        return this
    }

    /**
     * Invoked when the component is updated. This is usually at the creation
     * and when the screen is resized. However, it is not ensured that it is
     * the only time that the method is invoked.
     */
    open fun onUpdate(event: Consumer<UIComponent<*>>): UIComponent<T> {
        updateListeners = updateListeners ?: mutableListOf()
        updateListeners?.add(event)
        return this
    }

    open fun onMousePressed(event: Consumer<UIComponent<*>>): UIComponent<T> {
        mousePressedListeners = mousePressedListeners ?: mutableListOf()
        mousePressedListeners!!.add(event)
        return this
    }

    open fun onMouseReleased(event: Consumer<UIComponent<*>>): UIComponent<T> {
        mouseReleasedListeners = mouseReleasedListeners ?: mutableListOf()
        mouseReleasedListeners!!.add(event)
        return this
    }

    /**
     * Invoked when the mouse enters the component's bounding box.
     */
    open fun onMouseEnter(event: Consumer<UIComponent<*>>): UIComponent<T> {
        mouseEnteredListeners = mouseEnteredListeners ?: mutableListOf()
        mouseEnteredListeners!!.add(event)
        allocateHoverListeners()
        return this
    }

    /**
     * Invoked when the mouse leaves the bounds of the component.
     */
    open fun onMouseLeave(event: Consumer<UIComponent<*>>): UIComponent<T> {
        mouseLeaveListeners = mouseLeaveListeners ?: mutableListOf()
        mouseLeaveListeners!!.add(event)
        allocateHoverListeners()
        return this
    }

    /**
     * Invoked when the component is focused or de-focused. The [BiConsumer] is the component
     * and a boolean representing if it was focused or not focused.
     */
    open fun onFocus(event: BiConsumer<UIComponent<*>, Boolean>): UIComponent<T> {
        focusListeners = focusListeners ?: mutableListOf()
        focusListeners!!.add(event)
        return this
    }

    /** Position Calculation **/

    operator fun UIUnit?.unaryPlus() = getX(this)

    operator fun UIUnit?.unaryMinus() = getY(this)

    @JvmOverloads
    fun getX(unit: UIUnit?, ignore: Boolean = false): Float = unit.getX(getParentWidth(), ignore)

    @JvmOverloads
    fun UIUnit?.getX(width: Float, ignore: Boolean = false): Float = calculateUnitX(this, width, ignore)

    fun calculateUnitX(unit: UIUnit?, width: Float, ignore: Boolean): Float = if (unit == null) 0f else {
        calculateX(unit, this, width, ignore)
    }

    @JvmOverloads
    fun calculateX(unit: UIUnit?, component: UIComponent<*>, width: Float = component.getParentWidth(), ignoreOperation: Boolean = false): Float {
        return if (unit == null) 0f else if (!ignoreOperation && unit is UIOperationUnit) net.prismclient.aether.ui.util.extensions.calculateX(unit, component, width)
        else when (unit.type) {
            PIXELS -> unit.value
            RELATIVE -> unit.value * width
            EM -> unit.value * (component.style.font?.fontSize ?: 0f)
            ASCENDER -> unit.value * (component.style.font?.getAscend() ?: 0f)
            DESCENDER -> unit.value * (component.style.font?.getDescend() ?: 0f)
            else -> throw RuntimeException("{${unit.type} is not a valid Unit Type.}")
        }
    }

    @JvmOverloads
    fun getY(unit: UIUnit?, ignore: Boolean = false): Float = unit.getY(getParentHeight(), ignore)

    @JvmOverloads
    fun UIUnit?.getY(height: Float, ignore: Boolean = false) = calculateUnitY(this, height, ignore)

    fun calculateUnitY(unit: UIUnit?, height: Float, ignore: Boolean): Float = if (unit == null) 0f else {
        calculateY(unit, this, height, ignore)
    }

    fun getParentX() = if (parent != null) if (parent is UIFrame && ((parent as UIFrame).style as UIFrameSheet).clipContent) 0f else parent!!.x else 0f

    fun getParentY() = if (parent != null) if (parent is UIFrame && ((parent as UIFrame).style as UIFrameSheet).clipContent) 0f else parent!!.y else 0f

    fun getParentWidth() = if (parent != null) parent!!.width else UICore.width

    fun getParentHeight() = if (parent != null) parent!!.height else UICore.height

//    fun getAnchorX() = calculateUnitX(style.anchor?.x, width, false)
//
//    fun getAnchorY() = calculateUnitY(style.anchor?.y, height, false)

    fun isMouseInside() = (getMouseX() >= relX) && (getMouseY() >= relY) && (getMouseX() <= relX + relWidth) && (getMouseY() <= relY + relHeight)

    /**
     * Returns true if the mouse is inside the rel x, y, width and height
     */
    fun isMouseInsideBoundingBox(): Boolean {
        val check = isMouseInside() //&& (parent == null || parent!!.isMouseInsideBoundingBox()) //&& (parent != null || !parent!!.style.clipContent)
        //&& if (parent != null) if (parent!!.style.clipContent) (parent!!.isMouseInsideBoundingBox()) else false else true
        if (parent != null) {
            if (!parent!!.isMouseInsideBoundingBox()) {
                return false
            }
        }

        return check
    }

    fun check(): Boolean = if (parent != null && parent!!.style.clipContent && !parent!!.check()) false else isMouseInside()


    //= ((getMouseX() >= relX) && (getMouseY() >= relY) && (getMouseX() <= relX + relWidth) && (getMouseY() <= relY + relHeight)) && (parent == null || (UICore.mouseX >= parent!!.relX) && (UICore.mouseY >= parent!!.relY) && (UICore.mouseX <= parent!!.relX + parent!!.relWidth) && (UICore.mouseY <= parent!!.relY + parent!!.relHeight)) //&& if (parent != null) if (parent!!.style.clipContent) (parent!!.isMouseInsideBoundingBox()) else false else true


    fun getMouseX(): Float = UICore.mouseX - getParentXOffset()

    fun getMouseY(): Float = UICore.mouseY - getParentYOffset()

    fun getParentXOffset(): Float {
        if (parent == null) return 0f

        return if (parent is UIFrame) {
            val clipContent = ((parent as UIFrame).style as UIFrameSheet).clipContent
            return (if (clipContent) {
                parent!!.relX
            } else 0f) + parent!!.getParentXOffset() - if (parent is UIContainer<*>) {
                (parent!!.style as UIContainerSheet).horizontalScrollbar.value * (parent as UIContainer).expandedWidth
            } else 0f
        } else 0f
    }

    fun getParentYOffset(): Float {
        if (parent == null) return 0f

        return if (parent is UIFrame) {
            val clipContent = ((parent as UIFrame).style as UIFrameSheet).clipContent
            return (if (clipContent) {
                parent!!.relY
            } else 0f) + parent!!.getParentYOffset() - if (parent is UIContainer<*>) {
                (parent!!.style as UIContainerSheet).verticalScrollbar.value * (parent as UIContainer).expandedHeight
            } else 0f
        } else 0f
    }

//    fun getParentYOffset(skip: Boolean = false): Float {
//        val scrollbar = if (!skip && parent is UIContainer) {
//            (parent!!.style as UIContainerSheet).verticalScrollbar.value * (parent as UIContainer).expandedHeight
//        } else 0f
//
//        return if (parent is UIFrame) {
//            if (((parent as UIFrame).style as UIFrameSheet).clipContent) {
//                return parent!!.relY
//            } else 0f
//        } else 0f - scrollbar// + getParentYOffset(true)
//    }

    fun isWithinBounds(x: Float, y: Float, width: Float, height: Float) = (x <= getMouseX() && y <= getMouseY() && x + width >= getMouseX() && y + height >= getMouseY())


    /** Other **/
    inline fun style(block: T.() -> Unit) = block.invoke(style)

    /**
     * Shorthand for loading animations for when the mouse hovers over the component
     */
    fun hover(hoverAnimation: String, leaveAnimation: String): UIComponent<*> {
        this.hoverAnimation = hoverAnimation
        this.leaveAnimation = leaveAnimation
        allocateHoverListeners()
        return this
    }

    /**
     * Adds the hover listeners if necessary
     */
    protected fun allocateHoverListeners() {
        if (mouseEnteredListeners == null) {
            onMouseEnter {
                UIProvider.dispatchAnimation(it.hoverAnimation, this)
            }
        }

        if (mouseLeaveListeners == null) {
            onMouseLeave {
                UIProvider.dispatchAnimation(it.leaveAnimation, this)
            }
        }
    }

    /**
     * Returns true if this is an instance of [UIFocusable] and is focused
     */
    fun isFocused() = if (this is UIFocusable<*>) UICore.focusedComponent == this else false

    fun focus() {
        if (this is UIFocusable<*>) {
            UICore.focus(this)
            focusListeners?.forEach { it.accept(this, true) }
        } else {
            println("Attempted to focus a non-focusable component")
        }
    }

    fun defocus() {
        UICore.defocus()
        focusListeners?.forEach { it.accept(this, false) }
    }

    /**
     * Sets the active focused component to this
     */
    fun captureFocus() {
        if (this is UIFocusable<*>) {
            UICore.focus(this)
        } else {
            println("Attempted to focus component despite not being focusable?")
        }
    }

    /**
     * [InvalidStyleSheetException] is thrown when a cast from
     * [UIStyleSheet] to [T] failed.
     *
     * The most common cause is that the provided style sheet is not an
     * instance of [T]. If the [UIComponent] is a custom implementation it
     * is possible that the copy function is not override in the class.
     *
     * @author sen
     * @since 5/25/2022
     */
    class InvalidStyleSheetException(styleName: String, component: UIComponent<*>?) : Exception("Style of name \"$styleName\" is not a valid instance of $component. Is the style sheet passed an instance of the required style sheet for the component? If the style sheet is a custom implementation, please check if you have added the copy method which returns an instance of that style sheet.")
}