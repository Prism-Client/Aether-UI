package net.prismclient.aether.ui.renderer.dsl

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.controller.UIController
import net.prismclient.aether.ui.component.controller.impl.selection.UISelectableController
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.button.UICheckbox
import net.prismclient.aether.ui.component.type.input.button.UIImageButton
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.interfaces.UIDependable
import java.util.*

/**
 * [UIComponentDSL] is a DSL builder for defining components on the screen. Components should be implemented this way
 *
 * @author sen
 * @since 12/5/2022
 */
object UIComponentDSL {
    private var components: ArrayList<UIComponent<*>>? = null
    private var frames: ArrayList<UIFrame<*>>? = null
    private var componentStack: Stack<UIComponent<*>>? = null
    private var frameStack: Stack<UIFrame<*>>? = null
    private var styleStack: Stack<String>? = null
    private var activeComponent: UIComponent<*>? = null
    private var activeFrame: UIFrame<*>? = null
    var activeController: UIController<*>? = null
    var activeStyle: String? = null
        private set
    var withinComponentInit = false
        private set
    var ignore = false

    // TODO: Validate component styles

    /**
     * Must be invoked before calling any other functions or a NPE might be thrown
     */
    fun create() {
        components = ArrayList()
        frames = ArrayList()
        componentStack = Stack()
        frameStack = Stack()
        styleStack = Stack()
    }

    /**
     * Frees any resources after creating the components.
     */
    fun finalize() {
        components = null
        frames = null
        componentStack = null
        frameStack = null
        styleStack = null
        activeComponent = null
        activeFrame = null
        activeStyle = null
    }

    /**
     * Inserts the component into the active frame or the components array.
     */
    private fun insertComponent(component: UIComponent<*>) {
        if (activeComponent != null) component.parent = activeComponent
        if (components == null || frames == null) return
        if (component is UIFrame<*>) {
            frames!!.add(component)
        }
        if (activeFrame != null) {
            if (activeComponent == null) {
                component.parent = activeFrame
            }
            activeFrame!!.addComponent(component)
        } else {
            components!!.add(component)
        }
    }

    /**
     * Adds the component to the stack and / or array. Called
     * by the inner methods of this class.
     */
    fun pushComponent(component: UIComponent<*>) {
        if (activeController != null && !withinComponentInit && !ignore) {
            activeController!!.addComponent(component)
        }
        withinComponentInit = true

        insertComponent(component)

        // If the component is an instance of UIFrame<*>, add
        // it as the active frame, and to the frame stack
        if (component is UIFrame<*>) {
            activeFrame = component
            frameStack!!.add(component)
        } else {
            componentStack!!.push(component)
            activeComponent = component
        }
    }

    /**
     * Removes the component as the active component / frame.
     */
    fun popComponent(component: UIComponent<*>) {
        withinComponentInit = false
        if (components == null || frames == null) return
        if (component is UIFrame<*>) {
            if (frameStack!!.size != 0) {
                frameStack!!.pop()
                activeFrame = if (frameStack!!.size > 0) {
                    frameStack!!.peek()
                } else null
            }
        }
        if (activeComponent != null)
            componentStack!!.pop()
        activeComponent = if (!componentStack!!.isEmpty()) componentStack!!.peek() else null
    }

    /** Style **/
    fun applyStyle(name: String) {
        activeStyle = name
    }

    /**
     * Creates a DSL block for a style. When finished, the previous style is restored.
     */
    inline fun applyStyle(name: String, block: UIComponentDSL.() -> Unit) {
        val cachedStyle = activeStyle
        applyStyle(name)
        block.invoke(this)
        cachedStyle?.run { applyStyle(this) }
    }

    /**
     * Creates a new [UIStyleSheet], the default of [applyStyle]
     */
    inline fun style(name: String, block: UIStyleSheet.() -> Unit) = style(UIStyleSheet(), name, block)

    /**
     * Creates a new style from the given style sheet.
     *
     * @param sheet An instance of whatever style sheet is intended to be created
     * @param name The name of the style sheet
     * @param block The DSL block to configure [T]
     */
    inline fun <T : UIStyleSheet> style(sheet: T, name: String, block: T.() -> Unit) = net.prismclient.aether.ui.util.extensions.style(sheet, name, block)

    /**
     * Adds a dependable class. To create a dependable class, make a class
     * which extends [UIDependable]. See [UIDependable] doc for more information.
     *
     * Use this class as: `dependsOn(::ClassName)`
     *
     * @see UIDependable
     */
    inline fun <T : UIDependable> dependsOn(clazz: () -> T) {
        clazz().load()
    }

    /**
     * Java alternative to [dependsOn]. An instance of [UIDependable] must be passed.
     *
     * @see dependsOn
     */
    fun dependsOn(clazz: UIDependable) {
        clazz.load()
    }

    /**
     * A method to handle the creation of [UIController]
     */
    fun control(controller: UIController<*>?) {
        activeController = controller
    }

    /**
     * Anything within the block ignores being added to the active controller.
     */
    inline fun ignore(block: UIComponentDSL.() -> Unit) {
        ignore = true
        block.invoke(this)
        ignore = false
    }

    /**
     * Creates a controller block
     *
     * @param C The controller class
     * @param T The component for the controller class
     * @see UIController
     */
    inline fun <C : UIController<T>, T : UIComponent<*>> controller(controller: C, block: C.() -> Unit) {
        control(controller)
        controller.block()
        control(null)
        UICore.instance.controllers!!.add(controller)
    }

    /**
     * Creates a selectable controller. This allows for one active component at a time.
     *
     * @param T The filter for the component. The given value, and it's subclasses are the only valid type.
     */
    inline fun <T : UIComponent<*>> selectable(block: UISelectableController<T>.() -> Unit) = controller(UISelectableController(), block)

    /** Components **/

    /**
     * Creates a new component [T] with [block] as the
     * DSL. Automatically pushes and pops the component
     */
    inline fun <T : UIComponent<*>> component(component: T, block: T.() -> Unit): T {
        pushComponent(component)
        component.also(block)
        component.initialize()
        popComponent(component)
        return component
    }

    /** Label Components **/
    @JvmOverloads
    inline fun text(text: String, style: String? = activeStyle, block: UILabel.() -> Unit = {}) = component(UILabel(text, style!!), block)

    @JvmOverloads
    inline fun label(text: String, style: String? = activeStyle, block: UILabel.() -> Unit = {}) = text(text, style, block)

    @JvmOverloads
    inline fun h1(text: String, block: UILabel.() -> Unit = {}) = text(text, "h1", block)

    @JvmOverloads
    inline fun h2(text: String, block: UILabel.() -> Unit = {}) = text(text, "h2", block)

    @JvmOverloads
    inline fun h3(text: String, block: UILabel.() -> Unit = {}) = text(text, "h3", block)

    @JvmOverloads
    inline fun p(text: String, block: UILabel.() -> Unit = {}) = text(text, "p", block)

    /** Button **/
    @JvmOverloads
    inline fun button(text: String, style: String? = activeStyle, block: UIButton<UIStyleSheet>.() -> Unit = {}) = component(UIButton(text, style!!), block)

    @JvmOverloads
    inline fun button(text: String, style: String? = activeStyle, imageName: String, imagesStyle: String, block: UIImageButton.() -> Unit = {}) =
        component(UIImageButton(imageName, imagesStyle, text, style!!), block)

    inline fun checkbox(checked: Boolean = false, selectedImageName: String = "checkbox", imageStyle: String, style: String? = null, block: UICheckbox.() -> Unit) = checkbox(checked, selectedImageName, "", imageStyle, style, block)

    inline fun checkbox(checked: Boolean = false, selectedImageName: String = "checkbox", deselectedImageName: String = "", imageStyle: String, style: String? = activeStyle, block: UICheckbox.() -> Unit) = component(UICheckbox(checked, selectedImageName, deselectedImageName, imageStyle, style!!), block)

    /** Input **/
    @JvmOverloads
    inline fun slider(value: Float, min: Float, max: Float, step: Float, style: String? = activeStyle, block: UISlider.() -> Unit = {}) = component(UISlider(value, min, max, step, style!!), block)

    /** Other **/

    @JvmOverloads
    inline fun image(imageName: String, style: String? = activeStyle, block: UIImage.() -> Unit = {}) = component(UIImage(imageName, style!!), block)

    inline fun image(imageName: String, imageLocation: String, style: String? = activeStyle, block: UIImage.() -> Unit = {}) = component(UIImage(imageName, imageLocation, style!!), block)

    /** Layout **/
    @JvmOverloads
    inline fun container(style: String? = activeStyle, block: UIContainer<UIContainerSheet>.() -> Unit) = component(UIContainer(style!!), block)

    @JvmOverloads
    inline fun list(listDirection: UIListLayout.ListDirection, listOrientation: UIListLayout.ListOrientation, style: String? = activeStyle, block: UIListLayout.() -> Unit) = component(UIListLayout(listDirection, listOrientation, style!!), block)

    /**
     * Returns an ArrayList of components created
     */
    fun get(): ArrayList<UIComponent<*>> = components!!

    /**
     * Returns an ArrayList of frames created
     */
    fun getFrames(): ArrayList<UIFrame<*>> = frames!!

    /**
     * Returns true if the Component DSL builder is active
     */
    fun isActive() = components != null



    /**
     * Creates a DSL block for creating components. When started and completed, the stacks will be allocated/cleared
     *
     * @see ubuild
     */
    @JvmStatic
    inline fun build(block: UIComponentDSL.() -> Unit) = net.prismclient.aether.ui.util.extensions.create(block)

    /**
     * Unsafe version of [build]. Does not allocate/deallocate the stacks, thus nothing will be reset
     */
    @JvmStatic
    inline fun ubuild(block: UIComponentDSL.() -> Unit) = UIComponentDSL.block()
}