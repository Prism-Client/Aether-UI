package net.prismclient.aether

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.interfaces.UIFocusable
import net.prismclient.aether.ui.defaults.UIDefaults
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.builder.UIRendererDSL
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.util.UIKey

open class UICore(renderer: UIRenderer) {
    companion object {
        @JvmStatic
        lateinit var instance: UICore
        @JvmStatic
        var activeScreen: UIScreen? = null
            set(value) {
                instance.focusedComponent = null
                field = value
            }
        var width = 0f
        var height = 0f
        var mouseX = 0f
        var mouseY = 0f
        var contentScaleX = 0f
        var contentScaleY = 0f

        fun apply(style: UIDefaults) {
            UIDefaults.instance = style
        }
    }

    var focusedComponent: UIComponent<*>? = null

    var shiftHeld: Boolean = false
    var ctrlHeld: Boolean = false

    init {
        instance = this
        UIProvider.initialize(renderer)
    }

    open fun beginFrame(screenWidth: Float, screenHeight: Float, devicePxRatio: Float) {
        UIRendererDSL.instance.beginFrame(screenWidth, screenHeight, devicePxRatio)
    }

    open fun endFrame() {
        UIRendererDSL.instance.endFrame()
    }

    open fun renderContent() {
        activeScreen?.renderContent()
    }

    open fun render(screenWidth: Float, screenHeight: Float) {
        activeScreen?.render()
    }

    open fun mouseMoved(mouseX: Float, mouseY: Float) {
        activeScreen?.mouseMoved(mouseX, mouseY)
    }

    open fun mousePressed(mouseX: Float, mouseY: Float) {
        if ((focusedComponent?.isMouseInsideBoundingBox()) == false) {
            (focusedComponent!! as UIFocusable<*>).removeFocus()
            focusedComponent = null
        }

        activeScreen?.mousePressed(mouseX, mouseY)
    }

    open fun mouseReleased(mouseX: Float, mouseY: Float) {
        activeScreen?.mouseReleased(mouseX, mouseY)
    }

    open fun keyPressed(key: UIKey, character: Char) {
        activeScreen?.keyPressed(key, character)
    }

    open fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        activeScreen?.mouseScrolled(mouseX, mouseY, scrollAmount)
    }

    open fun update() {
        activeScreen?.update()
    }

    fun focus(component: UIComponent<*>) {
        if (component is UIFocusable<*>) {
            focusedComponent = component
            component.focus()
        } else {
            println("$component is not a instance of a UIFocusable")
        }
    }
}