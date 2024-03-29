package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.event.input.UIMouseEvent
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.interfaces.UIFocusable
import net.prismclient.aether.ui.util.name
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
abstract class UIFrame<T : UIFrameSheet> : UIComponent<T>(), UIFocusable {
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
        // Deallocate any existing framebuffer
        if (fbo != null) {
            Aether.renderer.deleteFBO(fbo!!)
            fbo = null
        }
        if (style.useFBO) {
            if ((fbo != null && (fbo!!.width != relWidth || fbo!!.height != relHeight)) || fbo == null) {
                fbo = Aether.renderer.createFBO(relWidth, relHeight)
            }
        }
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
        if (!components.remove(component)) warn("Failed to remove $component, as it was not found within the list.")
        else {
            component.parent = null
        }
    }

    /**
     * Renders the components within this frame.
     */
    open fun renderContent() {
        if (style.useFBO) {
//        if (style.useFBO && (requiresUpdate || !style.optimizeRenderer)) {
            if (fbo == null) {
                updateFBO()
            }
            renderer {
                fbo!!.renderToFramebuffer {
                    components.forEach(UIComponent<*>::render)
                }
            }
        }
//        requiresUpdate = false
    }

    override fun render() {
        updateAnimation()
        style.background?.render()
        renderComponent()
    }

    override fun renderComponent() {
        if (relWidth == 0f || relHeight == 0f)
            return
        renderer {
            if (style.useFBO) {
                color(-1)
                path {
                    imagePattern(fbo!!.imagePattern, relX, relY, relWidth, relHeight, 0f, 1f)
                    rect(relX, relY, relWidth, relHeight, style.background?.radius)
                }.fillPaint()
            } else {
                if (style.clipContent) scissor(relX, relY, relWidth, relHeight) {
                    components.forEach(UIComponent<*>::render)
                }
                else components.forEach(UIComponent<*>::render)
            }
        }
    }

    override fun deallocate() {
        components.forEach { it.deallocate() }
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        components.forEach { it.mouseMoved(mouseX, mouseY) }
    }

    override fun mousePressed(event: UIMouseEvent) {
        super.mousePressed(event)
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        components.forEach { it.mouseReleased(mouseX + relX, mouseY + relY) }
    }

    override fun keyPressed(character: Char) {
        super.keyPressed(character)
        keyPressListeners?.forEach { it.value.accept(this, character) }
    }

    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
        super.mouseScrolled(mouseX, mouseY, scrollAmount)
        components.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createsStyle(): T = UIFrameSheet() as T
}

open class UIFrameSheet : UIStyleSheet() {
    /**
     * If true, the frame will use an FBO to render content.
     */
    var useFBO: Boolean = false

    /**
     * If true certain optimizations will be applied when
     * rendering. This only works with [useFBO] as true.
     */
    var optimizeRenderer: Boolean = false
        set(value) {
            if (!useFBO && value) warn("attempted to set optimizeRenderer when useFBO is false. ($name)")
            field = value
        }

    override fun apply(sheet: UIStyleSheet): UIFrameSheet {
        // Override the default apply function because
        // this is an inheritable class.
        this.immutableStyle = sheet.immutableStyle
        this.name = sheet.name

        this.background = sheet.background?.copy()
        this.font = sheet.font?.copy()

        this.x = sheet.x?.copy()
        this.y = sheet.y?.copy()
        this.width = sheet.width?.copy()
        this.height = sheet.height?.copy()

        this.padding = sheet.padding?.copy()
        this.margin = sheet.margin?.copy()
        this.anchor = sheet.anchor?.copy()
        this.clipContent = sheet.clipContent

        // Frame properties
        if (sheet is UIFrameSheet) {
            this.useFBO = sheet.useFBO
            this.optimizeRenderer = sheet.optimizeRenderer
        }

        return this
    }

    override fun copy(): UIFrameSheet = UIFrameSheet().name(name).apply(this)
}