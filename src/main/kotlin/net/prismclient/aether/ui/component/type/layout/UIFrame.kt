package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.interfaces.UILayout
import net.prismclient.aether.ui.renderer.builder.UIRendererDSL
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.impl.UIFrameSheet
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIFrame] is a clickable box which content is rendered
 * inside. Unlike other layout components, such as containers,
 * the frame is designed for everything, and does not control
 * layout for its components within. It also allows for more
 * advanced component content clipping (such as rounded rectangles),
 * and even custom shapes using your own custom UIFrame implementation.
 *
 * Note: Backgrounds are rendered inside the renderContent method
 * instead of the UIBackground class to force the background to be rendered
 * into the content instead of outside it.
 *
 * @author sen
 * @since 4/30/2022
 */
open class UIFrame(style: String) : UIComponent<UIFrameSheet>(style), UILayout {
    protected val components = ArrayList<UIComponent<*>>()

    protected val renderer = UIRendererDSL.instance.render
    protected lateinit var framebuffer: UIContentFBO

    override fun addComponent(component: UIComponent<*>) {
        components.add(component)
        component.parent = this
    }

    override fun removeComponent(component: UIComponent<*>) {
        components.remove(component)
        component.parent = null
    }

    override fun update() {
        super.update()
        if (style.clipContent) {
            if (!this::framebuffer.isInitialized || relWidth != framebuffer.width || relHeight != framebuffer.height) {
                createFramebuffer()
            }
        }
        components.forEach(UIComponent<*>::update)
    }

    open fun createFramebuffer() {
        if (this::framebuffer.isInitialized) {
            renderer.deleteContentFBO(framebuffer)
        }
        framebuffer = renderer.createContentFBO(relWidth, relHeight)
    }

    open fun renderContent() {
        if (!style.clipContent)
            return
        if (!this::framebuffer.isInitialized) {
            createFramebuffer()
        }
        renderer {
            renderContent(framebuffer) {
                if (style.background != null) {
                    color(style.background!!.color)
                    rect(0f, 0f, framebuffer.width, framebuffer.height)
                }
                components.forEach(UIComponent<*>::render)
            }
        }
    }

    override fun render() {
        renderComponent()
    }

    override fun renderComponent() {
        if (!style.clipContent) {
            style.background?.render(relX, relY, relWidth, relHeight)
            components.forEach(UIComponent<*>::render)
            return
        }
        renderer {
            renderFbo(
                framebuffer,
                relX,
                relY,
                relWidth,
                relHeight,
                style.contentRadius?.topLeft ?: 0f,
                style.contentRadius?.topRight ?: 0f,
                style.contentRadius?.bottomRight ?: 0f,
                style.contentRadius?.bottomLeft ?: 0f
            )
            if (style.background!!.border != null) {
                outline(style.background!!.border!!.borderWidth, style.background!!.border!!.borderColor) {
                    rect(
                        relX,
                        relY,
                        relWidth,
                        relHeight,
                        style.background!!.radius.topLeft,
                        style.background!!.radius.topRight,
                        style.background!!.radius.bottomRight,
                        style.background!!.radius.bottomLeft
                    )
                }
            }
        }
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        components.forEach { it.mouseMoved(mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float) {
        super.mouseClicked(mouseX, mouseY)
        components.forEach { it.mouseClicked(mouseX, mouseY) }
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        components.forEach { it.mouseReleased(mouseX, mouseY) }
    }

    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        super.mouseScrolled(mouseX, mouseY, scrollAmount)
        components.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
    }

    override fun keyPressed(key: UIKey, character: Char) {
        super.keyPressed(key, character)
        components.forEach { it.keyPressed(key, character) }
    }
}