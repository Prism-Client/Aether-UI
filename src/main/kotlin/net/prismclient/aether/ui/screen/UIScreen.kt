package net.prismclient.aether.ui.screen

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.UIFrame
//import net.prismclient.aether.ui.renderer.builder.UIBuilder
import net.prismclient.aether.ui.util.UIKey

abstract class UIScreen {
    var components: ArrayList<UIComponent<*>> = ArrayList()
    var frames: ArrayList<UIFrame<*>> = ArrayList()

    init {
//        UIBuilder.start()
        initialize()
//        components = UIBuilder.create()
//        UIBuilder.reset()
//        components.forEach(UIComponent::initialize)
    }

    // protected inline fun style(block: UIStyle.() -> Unit) = UIStyle.block()

//    protected inline fun build(block: UIBuilder.() -> Unit) = UIBuilder.block()

    abstract fun initialize()

    open fun update() {
        components.forEach(UIComponent<*>::update)
        frames.forEach(UIFrame<*>::update)
    }

    open fun renderContent() {
        for (i in 0 until frames.size) frames[i].renderContent()
    }

    open fun render() {
        for (i in 0 until components.size) components[i].render()
    }

    open fun mouseMoved(mouseX: Float, mouseY: Float) {
        components.forEach { it.mouseMoved(it.getMouseX(), it.getMouseY()) }
    }

    open fun mousePressed(mouseX: Float, mouseY: Float) {
        components.forEach { it.mouseClicked(it.getMouseX(), it.getMouseY()) }
    }

    open fun mouseReleased(mouseX: Float, mouseY: Float) {
        components.forEach { it.mouseReleased(it.getMouseX(), it.getMouseY()) }
    }

    open fun keyPressed(key: UIKey, character: Char) {
        components.forEach { it.keyPressed(key, character) }
    }

    open fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        components.forEach { it.mouseScrolled(it.getMouseX(), it.getMouseY(), scrollAmount) }
    }

}