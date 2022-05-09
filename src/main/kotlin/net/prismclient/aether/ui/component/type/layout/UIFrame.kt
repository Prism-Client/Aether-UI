package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.util.interfaces.UILayout
import net.prismclient.aether.ui.renderer.builder.UIRendererDSL
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.style.impl.UIFrameSheet
import net.prismclient.aether.ui.util.UIKey
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIFrame] is a "viewport" for components. The frame holds a list of
 * children which are rendered. It is the superclass for components like
 * layouts, such as a list, or grid layout. Unlike other layout components,
 * [UIFrame] does not control the components inside it, and the components are
 * free to act however they want. Because of how [UIFrame] is designed, more
 * custom rendering features can be applied (such as shaders), by extending the
 * class and applying your own code.
 *
 * [UIFrame] works by rendering all the content to an FBO if content clipping is
 * enabled. If disabled, everything is rendered when the render method for this
 * component is invoked.
 *
 * @author sen
 * @since 4/30/2022
 */
open class UIFrame(style: String) : UIComponent<UIFrameSheet>(style), UILayout {
    protected val components = ArrayList<UIComponent<*>>()

    protected val renderer = UIRendererDSL.instance.render
    protected lateinit var framebuffer: UIContentFBO

    var frameWidth: Float = 0f
        protected set
    var frameHeight: Float = 0f
        protected set

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
        updateFramebuffer()
        components.forEach(UIComponent<*>::update)
    }

    override fun updateBounds() {
        super.updateBounds()
        updateFramebuffer()
    }

    open fun createFramebuffer() {
        if (this::framebuffer.isInitialized)
            renderer.deleteContentFBO(framebuffer)
        if (relWidth >= 1f && relHeight >= 1f)
            framebuffer = renderer.createContentFBO(frameWidth, frameHeight)
    }

    open fun updateFramebuffer() {
        frameWidth = style.frameWidth.getX()
        frameHeight = style.frameHeight.getY()
        if (style.clipContent) {
            if (!this::framebuffer.isInitialized || frameWidth != framebuffer.width || frameWidth != framebuffer.height) {
                createFramebuffer()
            }
        }
    }

    open fun renderContent() {
        if (!style.clipContent)
            return
        updateAnimation()
        if (!this::framebuffer.isInitialized)
            createFramebuffer()
        // If frame size is less than or equal to 0 skip render, as FBO couldn't be created
        if (relWidth < 1f || relHeight < 1f)
            return
        renderer {
            renderContent(framebuffer) {
                components.forEach(UIComponent<*>::render)
            }
        }
    }

    override fun render() {
        // Remove updating animation as animations are updated when the content is rendered
        if (!style.clipContent)
            updateAnimation()
        style.background?.render(relX, relY, relWidth, relHeight)
        renderer {
            scissor(relX, relY, relWidth, relHeight) {
                renderComponent()
            }
        }
    }

    override fun renderComponent() {
        if (!style.clipContent) {
            components.forEach(UIComponent<*>::render)
            return
        }
        renderer {
            // If frame size is less than or equal to 0 skip render, as FBO couldn't be created
            if (relWidth >= 1f || relHeight >= 1f) {
                renderFbo(
                    framebuffer,
                    relX,
                    relY,
                    frameWidth,
                    frameHeight,
                    style.contentRadius?.topLeft ?: 0f,
                    style.contentRadius?.topRight ?: 0f,
                    style.contentRadius?.bottomRight ?: 0f,
                    style.contentRadius?.bottomLeft ?: 0f
                )
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