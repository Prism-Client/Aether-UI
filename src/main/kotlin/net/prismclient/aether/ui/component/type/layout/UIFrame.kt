package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.component.util.interfaces.UILayout
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.component.type.layout.styles.UIFrameSheet
import net.prismclient.aether.ui.util.input.UIKey
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIFrame] is a "viewport" for components. The window holds a list of
 * children which are rendered. It is the superclass for components like
 * layouts, such as a list, or grid layout. If you are looking for simply
 * a window that does not control the layout of its components, take a look
 * at [UIContainer].
 *
 * [UIFrame] works by rendering all the content to an FBO if content clipping is
 * enabled. If disabled, everything is rendered when the render method for this
 * component is invoked.
 *
 * Because of how [UIFrame] is designed, more custom rendering features can be applied
 * (such as shaders), by extending the class and applying your own code.
 *
 * @author sen
 * @since 5/12/2022
 *
 * @see UIContainer
 */
abstract class UIFrame<T : UIFrameSheet>(style: String) : UIComponent<T>(style), UILayout {
    val components = ArrayList<UIComponent<*>>()
    protected var framebuffer: UIContentFBO? = null

    var frameWidth: Float = 0f
        protected set
    var frameHeight: Float = 0f
        protected set

    override fun addComponent(component: UIComponent<*>) {
        components.add(component)
//        component.parent = this
    }

    override fun removeComponent(component: UIComponent<*>) {
        components.remove(component)
//        component.parent = null
    }

    override fun update() {
        super.update()
        updateFramebuffer()
        components.forEach(UIComponent<*>::update)
        updateLayout()
    }

    open fun updateFrame() {
        updateFramebuffer()
        components.forEach(UIComponent<*>::update)
        updateLayout()
    }

    open fun createFramebuffer() {
        if (framebuffer != null) {
            UIRendererDSL.renderer.deleteContentFBO(framebuffer!!)
            framebuffer = null
        }
        if (relWidth >= 1f && relHeight >= 1f)
            framebuffer = UIRendererDSL.renderer.createContentFBO(frameWidth, frameHeight)
    }

    open fun updateFramebuffer() {
        frameWidth = +style.frameWidth
        frameHeight = -style.frameHeight
        if (style.clipContent) {
            if (framebuffer == null || frameWidth != framebuffer!!.width || frameHeight != framebuffer!!.height) {
                createFramebuffer()
            }
        }
    }

    open fun renderContent() {
        if (!style.clipContent)
            return
        updateAnimation()
        if (framebuffer != null)
            createFramebuffer()
        // If frame size is less than or equal to 0 skip render, as FBO couldn't be created
        if (relWidth < 1f || relHeight < 1f)
            return
        renderer {
            renderContent(framebuffer!!) {
                components.forEach(UIComponent<*>::render)
            }
        }
    }

    override fun render() {
        // Remove updating animation as animations are updated when the content is rendered
        if (!style.clipContent)
            updateAnimation()
        style.background?.render()
        renderer {
            if (!style.clipContent) {
                renderComponent()
                return
            }
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
            if (framebuffer == null) return
            // If frame size is less than or equal to 0 skip render, as FBO couldn't be created
            if (relWidth >= 1f || relHeight >= 1f) {
                renderer.renderFBO(
                        framebuffer!!,
                        relX,
                        relY,
                        frameWidth,
                        frameHeight,
                        style.background?.radius?.topLeft ?: 0f,
                        style.background?.radius?.topRight ?: 0f,
                        style.background?.radius?.bottomRight ?: 0f,
                        style.background?.radius?.bottomLeft ?: 0f
                )
            }
        }
    }

    override fun deallocate() {
        components.forEach { it.deallocate() }
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        components.forEach { it.mouseReleased(mouseX + relX, mouseY + relY) }
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        components.forEach { it.mouseMoved(mouseX, mouseY) }
    }

    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        super.mouseScrolled(mouseX, mouseY, scrollAmount)
        components.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
    }
}