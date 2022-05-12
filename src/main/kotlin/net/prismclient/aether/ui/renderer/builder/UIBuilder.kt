package net.prismclient.aether.ui.renderer.builder

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.style.UIStyleSheet
import java.util.Stack

/**
 * [UIBuilder] is a DSL class to make it easier to define components.
 *
 * @author sen
 * @since 12/5/2022
 */
object UIBuilder {
    private var components: ArrayList<UIComponent<*>>? = null
    private val frameStack = Stack<UIFrame>()
    private val styleStack = Stack<String>()
    var activeComponent: UIComponent<*>? = null
    var activeStyle: String = ""

    fun create() {
        components = ArrayList()
    }

    fun finalize() {
        components = null
        componentStack.clear()
        styleStack.clear()
        activeStyle = ""
    }

    fun addComponent(component: UIComponent<*>) {

    }

    /** Layout components **/

    /** General components **/

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