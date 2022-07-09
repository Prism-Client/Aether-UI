package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.styles.UIFrameSheet
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIFocusable
import net.prismclient.aether.ui.util.warn

/**
 * [UIFrame] in a single word is a "viewport," or frame, which holds a list of
 * components within it. It is the superclass of all layouts, and has support
 * for advanced features such as shaders. Behind the scenes, it uses am FBO to
 * render the content (unless disabled) via [UIContentFBO].
 *
 * When using an FBO certain opportunities arrive as the frames can be cached; thus
 * removing most of the overhead when rendering the frame. By default, the optimizations
 * are applied, and can be disabled by [UIFrameSheet.optimizeRenderer].
 *
 * Despite the possible optimizations that can occur when using [UIFrameSheet.useFBO], it is
 * suggested to use them in moderation. They take up quite a bit of memory; thus, limiting them
 * to only large frames which hold a large collections of components is the best option.
 *
 * @author sen
 * @since 1.0
 */
abstract class UIFrame<T : UIFrameSheet>(style: String?) : UIComponent<T>(style), UIFocusable {
    /**
     * The components of this frame.
     */
    val components: ArrayList<UIComponent<*>> = arrayListOf()

    /**
     * The FBO of this frame. It is allocated if [UIFrameSheet.useFBO] is true.
     */
    var fbo: UIContentFBO? = null
        protected set

    /**
     * If the frame is using an FBO, it isn't necessary to render every frame, as
     * components within it is not always updated every frame. This flag is used to
     * inform the frame that it needs to be updated because of a change in one of the
     * components.
     */
    var requiresUpdate: Boolean = true
        protected set

    /**
     * An internal clock to update the frame at least once per second
     */
    protected var lastUpdate: Long = System.currentTimeMillis()

    override fun update() {
        super.update()
        components.forEach { it.update() }
        updateFBO()
    }

    /**
     * Creates/Updates the FBO of this frame if necessary. It is invoked after the
     * frame has been updated, but prior to the first render.
     */
    open fun updateFBO() {
        if (style.useFBO)
            fbo = Aether.instance.renderer.createFBO(relWidth, relHeight)
    }

    /**
     * Adds the given component to [components] and sets the parent to this.
     */
    open fun addComponent(component: UIComponent<*>) {
        components.add(component)
        component.parent = this
    }

    /**
     * Removes the given component from [components] and sets the parent to null.
     */
    open fun removeComponent(component: UIComponent<*>) {
        if (!components.remove(component))
            warn("Failed to remove $component, as it was not found within the list.")
        else {
            component.parent = null
        }
    }

    /**
     * Renders the components within this frame.
     */
    open fun renderContent() {
        if (style.useFBO) {
            if (requiresUpdate || !style.optimizeRenderer) {
                if (fbo == null) updateFBO()
                renderer {
                    renderer.bindFBO(fbo!!)
                    beginFrame(fbo!!.width, fbo!!.height, fbo!!.contentScale)
                    components.forEach(UIComponent<*>::render)
                    endFrame()
                    Aether.instance.renderer.unbindFBO()
                }
                requiresUpdate = false
            }
        }
    }


    override fun render() {
        if (lastUpdate + 1000L < System.currentTimeMillis()) {
            requestUpdate()
            lastUpdate = System.currentTimeMillis()
        }
        style.background?.render()
        renderComponent()
        if (style.useFBO) {
            renderFBO()
        }
    }

    override fun renderComponent() {
        if (style.useFBO) {
            renderFBO()
        } else {
            if (style.clipContent)
                UIRendererDSL.scissor(relX, relY, relWidth, relHeight) {
                    components.forEach(UIComponent<*>::render)
                }
            else components.forEach(UIComponent<*>::render)
        }
    }

    override fun requestUpdate() {
        requiresUpdate = true
        super.requestUpdate()
    }

    override fun deallocate() {
        components.forEach { it.deallocate() }
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        components.forEach { it.mouseMoved(mouseX, mouseY) }
        if (this.isMouseInsideBounds()) requestUpdate()
    }

    override fun mousePressed(event: UIMouseEvent) {
        super.mousePressed(event)
        requestUpdate()
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        components.forEach { it.mouseReleased(mouseX + relX, mouseY + relY) }
        requestUpdate()
    }

    override fun keyPressed(character: Char) {
        super.keyPressed(character)
        keyPressListeners?.forEach { it.value.accept(this, character) }
        requestUpdate()
    }

    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        super.mouseScrolled(mouseX, mouseY, scrollAmount)
        components.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
        requestUpdate()
    }

    protected fun renderFBO() {
        renderer {
            path {
                renderer.imagePattern(fbo!!.id,  relX, relY, relWidth, relHeight, 0f, 1f)
                rect(relX, relY, relWidth, relHeight)
            }.fillPaint()
        }
    }
}