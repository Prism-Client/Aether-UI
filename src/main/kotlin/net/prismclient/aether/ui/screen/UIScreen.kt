package net.prismclient.aether.ui.screen

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.UIFrame
//import net.prismclient.aether.ui.renderer.builder.UIBuilder
import net.prismclient.aether.ui.renderer.builder.UIStyle
import net.prismclient.aether.ui.util.UIKey

abstract class UIScreen {
    var components: ArrayList<UIComponent<*>> = ArrayList()
    var frames: ArrayList<UIFrame> = ArrayList()

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
        frames.forEach(UIFrame::update)
    }

    open fun renderContent() {
        frames.forEach(UIFrame::renderContent)
    }

    open fun render() {
        components.forEach(UIComponent<*>::render)
    }

    open fun mouseMoved(mouseX: Float, mouseY: Float) {
        components.forEach { it.mouseMoved(mouseX, mouseY) }
    }

    open fun mousePressed(mouseX: Float, mouseY: Float) {
        components.forEach { it.mouseClicked(mouseX, mouseY) }
    }

    open fun mouseReleased(mouseX: Float, mouseY: Float) {
        components.forEach { it.mouseReleased(mouseX, mouseY) }
    }

    open fun keyPressed(key: UIKey, character: Char) {
        components.forEach { it.keyPressed(key, character) }
    }

    open fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        components.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
    }

}