package net.prismclient.aether.ui.screen

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.util.UIDependable
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.create

abstract class UIScreen {
    var components: ArrayList<UIComponent<*>> = ArrayList()
    var frames: ArrayList<UIFrame<*>> = ArrayList()

    init {
        initialize()

        components = UIComponentDSL.get()
        frames = UIComponentDSL.getFrames()
        UIComponentDSL.finalize()
        update()
    }

    protected inline fun build(block: UIComponentDSL.() -> Unit) = create(block)

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
     * Invoked when the screen is first created. A build block should be invoked
     * to create components, else unexpected, and unwanted actions might occur.
     *
     * @see UIComponentDSL
     * @see build
     */
    abstract fun initialize()

    /**
     * Invoked on screen creation, and when the display is changed.
     */
    open fun update() {
        components.forEach(UIComponent<*>::update)
        frames.forEach(UIFrame<*>::update)
    }

    /**
     * Rendered before everything else. (Including the actual game)
     */
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
        for (i in 0 until components.size) {
            if (i < components.size) {
                components[i].mouseClicked(mouseX, mouseY)
            }
        }
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