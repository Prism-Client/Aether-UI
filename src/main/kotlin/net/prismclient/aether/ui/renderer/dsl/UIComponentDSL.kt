package net.prismclient.aether.ui.renderer.dsl

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.type.input.UITextField
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.style.UIStyleSheet
import java.util.*
import kotlin.collections.ArrayList

/**
 * [UIComponentDSL] is a DSL builder for defining components on the screen.
 *
 * @author sen
 * @since 12/5/2022
 *
 * @see net.prismclient.aether.ui.util.extensions.create
 */
object UIComponentDSL {
    private var components: ArrayList<UIComponent<*>>? = null
    private var frames: ArrayList<UIFrame<*>>? = null
    private val frameStack = Stack<UIFrame<*>>()
    private val styleStack = Stack<String>()
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
    }

    /**
     * Frees any resources after creating the components.
     */
    fun finalize() {
        frameStack.clear()
        styleStack.clear()
        activeComponent = null
        activeFrame = null
        components = null
        frames = null
        activeStyle = null
    }

    /**
     * Adds the component to the stack and / or array. Called
     * by the inner methods of this class.
     */
    fun pushComponent(component: UIComponent<*>) {
        // If the active component is not null, then this
        // component's parent should be the active component
        if (activeComponent != null) {
            component.parent = activeComponent
            insertComponent(component)
            return
        }

        insertComponent(component)

        // If the component is an instance of UIFrame<*>, add
        // it as the active frame, and to the frame stack
        if (component is UIFrame<*>) {
            activeFrame = component
            frameStack.add(component)
        } else {
            activeComponent = component
        }
    }

    /**
     * Removes the component as the active component / frame.
     */
    fun popComponent(component: UIComponent<*>) {
        if (component is UIFrame<*>) {
            frameStack.pop()
            activeFrame = if (frameStack.size > 0) {
                frameStack.peek()
            } else null
        }
        activeComponent = null
    }

    /**
     * Inserts the component into the active frame or the components array.
     */
    private fun insertComponent(component: UIComponent<*>) {
        if (component is UIFrame<*>)
            frames!!.add(component)
        if (activeFrame != null) {
            activeFrame!!.addComponent(component)
        } else {
            components!!.add(component)
        }
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
        popComponent(component)
        return component
    }

    /** Label Components **/

    inline fun h1(text: String, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, "h1"), block)

    inline fun h2(text: String, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, "h2"), block)

    inline fun h3(text: String, block: UILabel.() -> Unit = {}) =
        component(UILabel(text, "h3"), block)

    inline fun p(text: String, block: UILabel.() -> Unit) =
        component(UILabel(text, "p"), block)

    /** Button **/

    inline fun button(text: String, style: String? = activeStyle, block: UIButton<UIStyleSheet>.() -> Unit = {}) =
        component(UIButton<UIStyleSheet>(text, style!!), block)

    /** Input **/

    inline fun slider(value: Float, min: Float, max: Float, step: Float, style: String? = activeStyle, block: UISlider.() -> Unit = {}) =
        component(UISlider(value, min, max, step, style!!), block)

    inline fun textField(text: String, placeholder: String, inputFlavor: UITextField.TextFlavor, maxLength: Int = -1, style: String? = activeStyle, block: UITextField.() -> Unit) =
        component(UITextField(text, placeholder, inputFlavor, maxLength, style!!), block)

    /** Other **/
    inline fun image(imageName: String, imageLocation: String, style: String? = activeStyle, block: UIImage.() -> Unit = {}) =
        component(UIImage(imageName, imageLocation, style!!), block)

    /** Layout **/

    inline fun container(style: String? = activeStyle, block: UIContainer<UIContainerSheet>.() -> Unit) =
            component(UIContainer(style!!), block)

    /**
     * Returns an ArrayList of components created
     */
    fun get(): ArrayList<UIComponent<*>> = components!!

    fun getFrames(): ArrayList<UIFrame<*>> = frames!!
}