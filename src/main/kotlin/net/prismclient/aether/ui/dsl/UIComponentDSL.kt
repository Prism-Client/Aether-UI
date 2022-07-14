package net.prismclient.aether.ui.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.controller.UIController
import net.prismclient.aether.ui.component.controller.impl.selection.UISelectableController
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.UIListLayout
import net.prismclient.aether.ui.component.type.layout.container.UIContainerSheet
import net.prismclient.aether.ui.dsl.UIComponentDSL.activeStyle
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.Block
import net.prismclient.aether.ui.util.interfaces.UIDependable
import java.util.*

/**
 * [UIComponentDSL] is a DSL builder for creating components. It has presets for components
 * and removes the need to manually create blocks for styles and components. Components and
 * Stacks are considered "push/pop-able" properties; multiple of itself, whilst the style and
 * controller properties can only be one property, or null.
 *
 * All component functions expect a style, else the style of [activeStyle] will be applied to it.
 *
 * @author sen
 * @since 1.0
 */
object UIComponentDSL {
    /**
     * The components which are automatically added/removed to support nesting
     */
    @JvmStatic
    var componentStack: Stack<UIComponent<*>>? = null

    /**
     * The frames which are automatically added/removed to support nesting
     */
    @JvmStatic
    var frameStack: Stack<UIFrame<*>>? = null

    @JvmStatic
    var activeController: UIController<*>? = null

    @JvmStatic
    var activeStyle: String? = null

    /**
     * If true, the component will not be added to the [activeController] (if applicable).
     *
     * @see ignore
     */
    @JvmStatic
    var ignoreController: Boolean = false

    /**
     * Allocates the stacks. Must be invoked prior to defining any components to avoid an exception.
     *
     * @see complete
     */
    @JvmStatic
    fun begin() {
        componentStack = Stack()
        frameStack = Stack()
    }

    /**
     * Clears any references to any variables within this class.
     *
     * @see begin
     */
    @JvmStatic
    fun complete() {
        componentStack = null
        frameStack = null

        activeStyle = null
    }

    /**
     * Updates the active component and / or frame as well as the stacks pertaining to those properties.
     */
    @JvmStatic
    fun updateState(component: UIComponent<*>) {
        check()
        if (component is UIFrame) frameStack!!.push(component)
        else componentStack!!.push(component)
    }


    @JvmStatic
    fun restoreState(component: UIComponent<*>) {
        check()
        // Restore the frame if the component is the frame
        if (component is UIFrame) {
            if (frameStack!!.isNotEmpty()) frameStack!!.pop()
            return
        }
        if (componentStack!!.isNotEmpty()) componentStack!!.pop()
    }

    /**
     * Applies any properties pertaining to state and updates the state to the component's needs.
     */
    @JvmStatic
    inline fun <reified T : UIComponent<*>> pushComponent(component: T) {
        check()
        val activeComponent = getActiveComponent()
        val activeFrame = getActiveFrame()
        if (activeComponent != null) component.parent = activeComponent
        else if (activeFrame != null) activeFrame.addComponent(component)
        else Aether.instance.components!!.add(component)
        if (component is UIFrame<*>) Aether.instance.frames!!.add(component)
        if (activeController != null && !ignoreController && activeController?.filter?.isInstance(component) == true) {
            activeController!!.addComponent(component)
        }
        updateState(component)
    }

    /**
     * Restores the state to the previous component, or null.
     */
    @JvmStatic
    fun popComponent(component: UIComponent<*>) {
        check()
        restoreState(component)
    }

    /**
     * Creates DSL block for the provided component, [T] where it is inserted into the stack,
     * invokes the block, and to initialize function of the component, and popped.
     *
     * @return T The component
     */
    inline fun <reified T : UIComponent<*>> component(component: T, style: String?, block: Block<T>): T {
        pushComponent(component)
        component.applyStyle(style)
        component.block()
        component.initialize()
        popComponent(component)
        return component
    }

    /**
     * Sets the active controller to the given [controller] and accepts the block which applies the
     * controller to all the components within it. If the component within the [block] is not an
     * instance of [T], an error will be thrown.
     *
     * @see ignore
     */
    inline fun <C : UIController<T>, T : UIComponent<*>> controller(controller: C, block: Block<C>) {
        Aether.instance.controllers!!.add(controller)
        activeController = controller
        controller.block()
        activeController = null
    }

    /**
     * Creates a block where the [activeController] is ignored.
     */
    inline fun ignore(block: Block<UIComponentDSL>): UIComponentDSL {
        ignoreController = true
        block(this)
        ignoreController = false
        return this
    }

    /**
     * Creates a block where the style is set to the given value.
     */
    inline fun style(styleName: String, block: Block<UIComponentDSL>) {
        // Technically this function supports nesting, soooooo the documentation
        // is "technically" wrong but whatever. In the case of controllers, it
        // doesn't actually make sense to have the ability to nest them.
        val previousStyle = activeStyle
        activeStyle = styleName
        block(this)
        block.invoke(this)
        activeStyle = previousStyle
    }

    /**
     * Loads the [dependable].
     */
    fun include(dependable: UIDependable) = dependable.load()

    fun getActiveComponent(): UIComponent<*>? = if (componentStack.isNullOrEmpty()) null else componentStack!!.peek()

    fun getActiveFrame(): UIFrame<*>? = if (frameStack.isNullOrEmpty()) null else frameStack!!.peek()

    /**
     * Throws an exception if the properties have not been initialized.
     */
    fun check() {
        if (componentStack == null || frameStack == null) throw UninitializedPropertyAccessException()
    }

    // -- Components -- //

    /**
     * Creates a [UILabel] with the provided [text].
     *
     * @see label
     */
    @JvmOverloads
    inline fun text(text: String, style: String? = activeStyle, block: Block<UILabel> = {}) =
        component(UILabel(text), style, block)

    /**
     * An alternative to [text]. Creates a [UILabel] with the provided [text].
     *
     * @see text
     */
    @JvmOverloads
    inline fun label(text: String, style: String? = activeStyle, block: Block<UILabel> = {}) =
        component(UILabel(text), style, block)

    /**
     * Creates a [UIButton] with the provided [text], like a label.
     */
    inline fun button(text: String, style: String? = activeStyle, block: Block<UIButton> = {}) =
        component(UIButton(text), style, block)

    /**
     * Creates a [UISlider] with the given [value] which stays within the [range] and steps by the
     * value given to [step].
     */
    @JvmOverloads
    inline fun slider(
        value: Number,
        range: ClosedFloatingPointRange<Double>,
        step: Number,
        style: String? = activeStyle,
        block: Block<UISlider> = {}
    ) = component(UISlider(value.toDouble(), range, step.toDouble()), style, block)

    /**
     * Creates a [UIImage] with the [imageName] as the image to be rendered.
     */
    @JvmOverloads
    inline fun image(imageName: String, style: String? = activeStyle, block: Block<UIImage> = {}) =
        component(UIImage(imageName), style, block)

    /**
     * Creates a [UIContainer]. Anything within the block will be added to the component list within this.
     */
    @JvmOverloads
    inline fun container(style: String? = activeStyle, block: Block<UIContainer<UIContainerSheet>> = {}) =
        component(UIContainer(), style, block)

    /**
     * Creates a [UIListLayout] with the given [listDirection], which defines the direction that it
     * lays out in (vertical or horizontal), and the given [listOrder] defining which direction
     * the list is ordered in.
     */
    @JvmOverloads
    inline fun list(
        listDirection: UIListLayout.ListDirection,
        listOrder: UIListLayout.ListOrder = UIListLayout.ListOrder.Forward,
        style: String? = activeStyle,
        block: Block<UIListLayout> = {}
    ) = component(UIListLayout(listDirection, listOrder), style, block)

    /**
     * Creates a vertical list via [list].
     *
     * @see list
     */
    inline fun verticalList(
        listOrder: UIListLayout.ListOrder = UIListLayout.ListOrder.Forward,
        style: String? = activeStyle,
        block: Block<UIListLayout> = {}
    ) = list(UIListLayout.ListDirection.Vertical, listOrder, style, block)

    /**
     * Creates a horizontal list via [list].
     *
     * @see list
     */
    inline fun horizontalList(
        listOrder: UIListLayout.ListOrder = UIListLayout.ListOrder.Forward,
        style: String? = activeStyle,
        block: Block<UIListLayout> = {}
    ) = list(UIListLayout.ListDirection.Horizontal, listOrder, style, block)

    /**
     * Creates a copy of the given layout and creates a normal block of [UIAutoLayout] where
     * components can be defined.
     */
    @JvmOverloads
    inline fun autoLayout(layout: UIAutoLayout, block: Block<UIAutoLayout> = {}) = component(layout.copy(), activeStyle, block)

    /**
     * Creates an [UIAutoLayout] from the given [listDirection]. [UIAutoLayout] are designed to mimic Figma's
     * auto layout feature. See [UIAutoLayout] for more information.
     *
     * @see UIAutoLayout
     */
    @JvmOverloads
    inline fun autoLayout(
        listDirection: UIListLayout.ListDirection,
        style: String? = null,
        block: Block<UIAutoLayout> = {}
    ) = component(UIAutoLayout(listDirection), style, block)

    /**
     * Creates a [UISelectableController] which is a controller that has a single selected
     * component, while all the others components are considered deselected.
     *
     * @see controller
     * @see UISelectableController
     */
    @JvmOverloads
    inline fun <reified T : UIComponent<*>> selectable(block: Block<UISelectableController<T>> = {}) =
        controller(UISelectableController(T::class), block)
}