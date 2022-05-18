package net.prismclient.aether.ui.renderer.dsl

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.slider.UISlider
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.style.UIStyleSheet
import java.util.*

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
    private val frameStack = Stack<UIFrame<*>>()
    private val styleStack = Stack<String>()
    var activeComponent: UIComponent<*>? = null
    var activeStyle: String? = null

    fun create() {
        components = ArrayList()
    }

    fun finalize() {
        components = null
        frameStack.clear()
        styleStack.clear()
        activeStyle = ""
    }

    fun addComponent(component: UIComponent<*>) {

    }

    /** Style **/
    fun style(name: String) {
        activeStyle = name
    }

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
    inline fun <T : UIComponent<*>> component(component: T, block: T.() -> Unit): T {
        if (component is UIFrame<*>) {

        }
        // TODO add to list
        return component.also(block)
    }

    /* Label Components */
    inline fun h1(text: String, block: UILabel.() -> Unit) =
        component(UILabel(text, "h1"), block)

    inline fun h2(text: String, block: UILabel.() -> Unit) =
        component(UILabel(text, "h2"), block)

    inline fun h3(text: String, block: UILabel.() -> Unit) =
        component(UILabel(text, "h3"), block)

    inline fun button(text: String, style: String? = activeStyle, block: UIButton<UIStyleSheet>.() -> Unit) =
        component(UIButton<UIStyleSheet>(text, style!!), block)

    inline fun slider(value: Float, min: Float, max: Float, step: Float, style: String? = activeStyle, block: UISlider.() -> Unit) =
        component(UISlider(value, min, max, step, style!!), block)

    /**
     * Returns an ArrayList of components created
     */
    fun get(): ArrayList<UIComponent<*>> = components!!
}

//    var components: ArrayList<UIComponent> = ArrayList()
//    private val componentStack = Stack<UIComponent>()
//    private val styleQueue = Stack<String>()
//    var activeComponent: UIComponent? = null
//    var activeStyle = "Default"
//
//    fun start() {
//        reset()
//        pushStyle("Default")
//    }
//
//    fun create(): ArrayList<UIComponent> = components
//
//    fun reset() {
//        components = ArrayList()
//        styleQueue.clear()
//    }
//
//    fun pushComponent(component: UIComponent) {
//        componentStack.push(component)
//        activeComponent = component
//    }
//
//    fun popComponent() {
//        componentStack.pop()
//        activeComponent = if (componentStack.isEmpty()) null else componentStack.peek()
//    }
//
//    fun addComponent(component: UIComponent) {
//        if (activeComponent == null) {
//            components.add(component)
//        } else {
//            component.parent = activeComponent
//            if (activeComponent is UIContainer) {
//                (activeComponent as UIContainer).addChild(component); return
//            }
//            components.add(component)
//        }
//    }
//
//    fun pushStyle(name: String) {
//        styleQueue.push(name)
//        activeStyle = name
//    }
//
//    fun popStyle() {
//        styleQueue.pop()
//        activeStyle = if (styleQueue.isEmpty()) "Default" else styleQueue.peek()
//    }
//
//    inline fun style(style: String, block: UIBuilder.() -> Unit) {
//        pushStyle(style)
//        this.block()
//        popStyle()
//    }
//
//    inline fun container(style: String = "", block: UIBuilder.() -> Unit): UIContainer {
//        val container = UIContainer(+style)
//        addComponent(container)
//        pushComponent(container)
//        this.block()
//        popComponent()
//        return container
//    }
//
//    inline fun grid(
//        style: String = "",
//        layout: UIGridLayoutComponent.() -> Unit,
//        block: UIBuilder.() -> Unit
//    ): UIGridLayoutComponent {
//        val grid = UIGridLayoutComponent(+style)
//        addComponent(grid)
//        pushComponent(grid)
//        grid.layout()
//        this.block()
//        popComponent()
//        return grid
//    }
//
//    inline fun verticalList(
//        style: String,
//        layout: UIVerticalListComponent.() -> kotlin.Unit,
//        block: UIBuilder.() -> Unit
//    ): UIVerticalListComponent {
//        val list = UIVerticalListComponent(+style)
//        addComponent(list)
//        pushComponent(list)
//        list.layout()
//        this.block()
//        popComponent()
//        return list
//    }
//
//    inline fun horizontalList(
//        style: String,
//        layout: UIHorizontalListComponent.() -> kotlin.Unit,
//        block: UIBuilder.() -> Unit
//    ): UIHorizontalListComponent {
//        val list = UIHorizontalListComponent(+style)
//        addComponent(list)
//        pushComponent(list)
//        list.layout()
//        this.block()
//        popComponent()
//        return list
//    }
//
//    fun blank(): UIBlankComponent = UIBlankComponent().also { addComponent(it) }
//
//    inline fun generic(style: String = "", block: UIGenericComponent.() -> Unit = {}): UIGenericComponent {
//        val generic = UIGenericComponent(+style)
//        addComponent(generic)
//        generic.block()
//        return generic
//    }
//
//    inline fun label(text: String, style: String, block: UITextButtonComponent.() -> Unit = {}): UITextButtonComponent =
//        button(text, style, block)
//
//    inline fun button(
//        text: String, style: String = "", block: UITextButtonComponent.() -> Unit = {}
//    ): UITextButtonComponent {
//        val button = UITextButtonComponent(text, +style)
//        addComponent(button)
//        button.style(style)
//        button.block()
//        return button
//    }
//
//    inline fun image(
//        image: String,
//        imageLocation: String = "/images/$image.png",
//        style: String = "",
//        block: UIImageComponent.() -> Unit = {}
//    ): UIImageComponent {
//        val imageComponent = UIImageComponent(image, imageLocation, style = +style)
//        addComponent(imageComponent)
//        imageComponent.style(style)
//        imageComponent.block()
//        return imageComponent
//    }
//
//    inline fun imageButton(
//        text: String,
//        image: String,
//        imageStyle: String,
//        style: String = "",
//        block: UIImageTextButtonComponent.() -> Unit = {}
//    ): UIImageTextButtonComponent {
//        val imageButton = UIImageTextButtonComponent(image, text = text, style = +style)
//        addComponent(imageButton)
//        imageButton.image.style(imageStyle)
//        imageButton.block()
//        return imageButton
//    }
//
//    inline fun imageButton(
//        text: String,
//        image: String,
//        imageLocation: String,
//        imageStyle: String,
//        style: String = "",
//        block: UIImageTextButtonComponent.() -> Unit = {}
//    ): UIImageTextButtonComponent {
//        val imageButton = UIImageTextButtonComponent(image, imageLocation, text, +style)
//        addComponent(imageButton)
//        imageButton.image.style(imageStyle)
//        imageButton.block()
//        return imageButton
//    }
//
//    inline fun textField(
//        text: String = "",
//        placeHolder: String = "",
//        style: String = "",
//        caretStyle: String = "",
//        block: UITextFieldComponent.() -> Unit = {}
//    ): UITextFieldComponent {
//        val textField = UITextFieldComponent(text, placeHolder, +style, caretStyle)
//        addComponent(textField)
//        textField.block()
//        return textField
//    }
//
//    inline fun checkbox(
//        imageName: String,
//        imageLocation: String = "/images/$imageName.png",
//        style: String = "",
//        block: UICheckBoxComponent.() -> Unit = {}
//    ): UICheckBoxComponent {
//        val checkbox = UICheckBoxComponent(imageName, imageLocation, +style)
//        addComponent(checkbox)
//        checkbox.block()
//        return checkbox
//    }
//
//    operator fun String.unaryPlus(): String = this.ifEmpty { activeStyle }
//}