package net.prismclient.aether.ui.renderer.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.controller.UIController
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.button.UICheckbox
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL.activeStyle
import net.prismclient.aether.ui.style.UIStyleSheet
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
    var componentStack: Stack<UIComponent<*>>? = null

    /**
     * The frames which are automatically added/removed to support nesting
     */
    var frameStack: Stack<UIFrame<*>>? = null

    var activeController: UIController<*>? = null
    var activeStyle: String? = null

    /**
     * If true, the component will not be added to the [activeController] (if applicable).
     *
     * @see ignore
     */
    var ignoreController: Boolean = false

    /**
     * Allocates the stacks. Must be invoked prior to defining any components to avoid an exception.
     *
     * @see complete
     */
    fun begin() {
        componentStack = Stack()
        frameStack = Stack()
    }

    /**
     * Clears any references to any variables within this class.
     *
     * @see begin
     */
    fun complete() {
        componentStack = null
        frameStack = null

        activeStyle = null
    }

    /**
     * Updates the active component and / or frame as well as the stacks pertaining to those properties.
     */
    fun updateState(component: UIComponent<*>) {
        check()
        if (component is UIFrame) frameStack!!.push(component)
        else componentStack!!.push(component)
    }

    /**
     * Restores the state based on the component. The component given is expected to be
     * the active component or the active frame; the most recently pushed component.
     */
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
    fun pushComponent(component: UIComponent<*>) {
        check()
        val activeComponent = getActiveComponent()
        val activeFrame = getActiveFrame()
        if (activeComponent != null)
            component.parent = activeComponent
        else if (activeFrame != null)
            activeFrame.addComponent(component)
        else Aether.instance.components!!.add(component)
        if (component is UIFrame) Aether.instance.frames!!.add(component)
        if (activeController != null && !ignoreController) activeController!!.addComponent(component)
        updateState(component)
    }

    /**
     * Restores the state to the previous component, or null.
     */
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
    inline fun <T : UIComponent<*>> component(component: T, block: T.() -> Unit): T {
        pushComponent(component)
        component.block()
        component.initialize()
        popComponent(component)
        return component
    }

    /**
     * Sets the active controller to the given [controller] and accepts the block which applies the
     * controller to all the components within it. If the component within the [block] is not an
     * instance of [T], a message will be printed.
     *
     * @see ignore
     */
    inline fun <T : UIComponent<*>> controller(
        controller: UIController<T>,
        block: UIController<T>.() -> Unit
    ): UIController<T> {
        Aether.instance.controllers!!.add(controller)
        activeController = controller
        controller.block()
        activeController = null
        return controller
    }

    /**
     * Creates a block where the [activeController] is ignored.
     */
    inline fun ignore(block: UIComponentDSL.() -> Unit): UIComponentDSL {
        ignoreController = true
        block(this)
        ignoreController = false
        return this
    }

    /**
     * Creates a block where the style is set to the given value.
     */
    inline fun style(styleName: String, block: UIComponentDSL.() -> Unit) {
        // Technically this function supports nesting, soooooo the documentation
        // is "technically" wrong but whatever. In the case of controllers, it
        // doesn't actually make sense to have the ability to nest them.
        val previousStyle = activeStyle
        activeStyle = styleName
        block(this)
        block.invoke(this)
        activeStyle = previousStyle
    }

    private fun getActiveComponent(): UIComponent<*>? =
        if (componentStack.isNullOrEmpty()) null else componentStack!!.peek()

    private fun getActiveFrame(): UIFrame<*>? = if (frameStack.isNullOrEmpty()) null else frameStack!!.peek()

    /**
     * Throws an exception if the properties have not been initialized.
     */
    private fun check() {
        if (componentStack == null || frameStack == null) throw UninitializedPropertyAccessException()
    }

    /* Components */

    /**
     * Creates a [UILabel] with the provided [text].
     *
     * @see label
     */
    @JvmOverloads
    inline fun text(text: String, style: String? = activeStyle, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, style), block)

    /**
     * An alternative to [text]. Creates a [UILabel] with the provided [text].
     *
     * @see text
     */
    @JvmOverloads
    inline fun label(text: String, style: String? = activeStyle, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, style), block)

    /**
     * Creates a [UIButton] with the provided [text], like a label.
     */
    inline fun button(text: String, style: String? = activeStyle, block: UIButton<UIStyleSheet>.() -> Unit = {}) =
        component(UIButton(text, style), block)

    /**
     * Creates a [UICheckbox] from the given [selectedImageName], [deselectedImageName] and [imageStyle].
     */
    @JvmOverloads
    inline fun checkbox(
        checked: Boolean = false,
        selectedImageName: String = "checkbox",
        deselectedImageName: String = "",
        imageStyle: String,
        style: String? = activeStyle,
        block: UICheckbox.() -> Unit
    ) = component(UICheckbox(checked, selectedImageName, deselectedImageName, imageStyle, style), block)

    /**
     * Creates a [UISlider] with the [value] as the value of the slider, [min] as the minimum
     * value, [max] as the maximum value, and [step] as the distance to step by.
     */
    @JvmOverloads
    inline fun slider(
        value: Float,
        min: Float,
        max: Float,
        step: Float,
        style: String? = activeStyle,
        block: UISlider.() -> Unit = {}
    ) =
        component(UISlider(value, min, max, step, style), block)

    /**
     * Creates a [UIImage] with the [imageName] as the image to be rendered.
     */
    @JvmOverloads
    inline fun image(imageName: String, style: String? = activeStyle, block: UIImage.() -> Unit = {}) =
        component(UIImage(imageName, style), block)

    /**
     * Creates a [UIContainer]. Anything within the block will be added to the component list within this.
     */
    @JvmOverloads
    inline fun container(style: String? = activeStyle, block: UIContainer<UIContainerSheet>.() -> Unit = {}) =
        component(UIContainer(style), block)

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
        block: UIListLayout.() -> Unit = {}
    ) =
        component(UIListLayout(listDirection, listOrder, style), block)
}