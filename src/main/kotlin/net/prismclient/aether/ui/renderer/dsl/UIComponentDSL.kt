package net.prismclient.aether.ui.renderer.dsl

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.type.input.UITextField
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.button.UICheckbox
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.style.UIStyleSheet
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
    private var frameStack: Stack<UIFrame<*>>? = null
    private var styleStack: Stack<String>? = null
    private var activeComponent: UIComponent<*>? = null
    private var activeFrame: UIFrame<*>? = null
    var activeStyle: String? = null
        private set

    /**
     * Must be invoked before calling any other functions or a NPE might be thrown
     */
    fun create() {
        components = ArrayList()
        frames = ArrayList()
        frameStack = Stack()
        styleStack = Stack()
    }

    /**
     * Frees any resources after creating the components.
     */
    fun finalize() {
        components = null
        frames = null
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
        if (activeComponent != null)
            component.parent = activeComponent
        if (components == null || frames == null) return
        if (component is UIFrame<*>)
            frames!!.add(component)
        if (activeFrame != null) {
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
        // If the active component is not null, then this
        // component's parent should be the active component
//        if (activeComponent != null) {
//            component.parent = activeComponent
//            insertComponent(component)
//            return
//        }

        insertComponent(component)

        // If the component is an instance of UIFrame<*>, add
        // it as the active frame, and to the frame stack
        if (component is UIFrame<*>) {
            activeFrame = component
            frameStack!!.add(component)
        } else {
            activeComponent = component
        }
    }

    /**
     * Removes the component as the active component / frame.
     */
    fun popComponent(component: UIComponent<*>) {
        if (components == null || frames == null) return
        if (component is UIFrame<*>) {
            if (frameStack!!.size != 0) {
                frameStack!!.pop()
                activeFrame = if (frameStack!!.size > 0) {
                    frameStack!!.peek()
                } else null
            }
        }
        activeComponent = null
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
    inline fun <T : UIStyleSheet> style(sheet: T, name: String, block: T.() -> Unit) =
        net.prismclient.aether.ui.util.extensions.style(sheet, name, block)

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
    inline fun h1(text: String, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, "h1"), block)

    @JvmOverloads
    inline fun h2(text: String, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, "h2"), block)

    @JvmOverloads
    inline fun h3(text: String, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, "h3"), block)

    @JvmOverloads
    inline fun p(text: String, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, "p"), block)

    /** Button **/
    @JvmOverloads
    inline fun button(text: String, style: String? = activeStyle, block: UIButton<UIStyleSheet>.() -> Unit = {}) =
        component(UIButton<UIStyleSheet>(text, style!!), block)

    inline fun checkbox(
        checked: Boolean = false,
        selectedImageName: String = "checkbox",
        imageStyle: String,
        style: String? = null,
        block: UICheckbox.() -> Unit
    ) =
        UIComponentDSL.checkbox(checked, selectedImageName, "", imageStyle, style, block)

    inline fun checkbox(
        checked: Boolean = false,
        selectedImageName: String = "checkbox",
        deselectedImageName: String = "",
        imageStyle: String,
        style: String? = activeStyle,
        block: UICheckbox.() -> Unit
    ) =
        component(UICheckbox(checked, selectedImageName, deselectedImageName, imageStyle, style!!), block)

    /** Input **/
    @JvmOverloads
    inline fun slider(
        value: Float,
        min: Float,
        max: Float,
        step: Float,
        style: String? = activeStyle,
        block: UISlider.() -> Unit = {}
    ) =
        component(UISlider(value, min, max, step, style!!), block)

    @JvmOverloads
    inline fun textField(
        text: String,
        placeholder: String,
        inputFlavor: UITextField.TextFlavor,
        maxLength: Int = -1,
        style: String? = activeStyle,
        block: UITextField.() -> Unit
    ) =
        component(UITextField(text, placeholder, inputFlavor, maxLength, style!!), block)

    /** Other **/
    @JvmOverloads
    inline fun image(
        imageName: String,
        imageLocation: String,
        style: String? = activeStyle,
        block: UIImage.() -> Unit = {}
    ) =
        component(UIImage(imageName, imageLocation, style!!), block)

    /** Layout **/
    @JvmOverloads
    inline fun container(style: String? = activeStyle, block: UIContainer<UIContainerSheet>.() -> Unit) =
        component(UIContainer(style!!), block)

    @JvmOverloads
    inline fun list(
        listDirection: UIListLayout.ListDirection,
        listOrientation: UIListLayout.ListOrientation,
        style: String? = activeStyle,
        block: UIListLayout.() -> Unit
    ) =
        component(UIListLayout(listDirection, listOrientation, style!!), block)

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
}