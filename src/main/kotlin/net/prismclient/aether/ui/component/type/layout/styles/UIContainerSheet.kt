package net.prismclient.aether.ui.component.type.layout.styles

import net.prismclient.aether.ui.renderer.impl.scrollbar.UIScrollbar
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIContainerSheet] is the corresponding style sheet for containers. It provides
 * properties for the scrollbar and when to introduce them
 *
 * @author sen
 * @since 5/12/2022
 * @see UIScrollbar
 * @see Overflow
 */
open class UIContainerSheet @JvmOverloads constructor(name: String = "") : UIFrameSheet(name) {
    /**
     * Describes when to introduce the scrollbar
     *
     * @see Overflow
     */
    var overflowX: Overflow = Overflow.Auto

    /**
     * Describes when to introduce the scrollbar
     *
     * @see Overflow
     */
    var overflowY: Overflow = Overflow.Auto

    var verticalScrollbar: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Vertical)
    var horizontalScrollbar: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Horizontal)

    /**
     * Creates a DSL block for the vertical scrollbar
     */
    inline fun verticalScrollbar(block: UIScrollbar.() -> Unit) = verticalScrollbar.block()

    /**
     * Creates a DSL block for the horizontal scrollbar
     */
    inline fun horizontalScrollbar(block: UIScrollbar.() -> Unit) = horizontalScrollbar.block()

    /**
     * [Overflow] defines what the vertical, and horizontal scrollbars
     * are supposed to do when content leaves the screen. Check the enum
     * documentation for more information.
     *
     * @author sen
     * @since 5/12/2022
     */
    enum class Overflow {
        /**
         * Does not introduce a scrollbar on the given axis
         */
        None,

        /**
         * Creates a scrollbar on the given axis regardless if content leaves the window
         */
        Always,

        /**
         * Like scroll, but only adds the scrollbar on the given axis if content leaves the window
         */
        Auto
    }

    override fun apply(sheet: UIStyleSheet): UIContainerSheet {
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
        if (sheet is UIContainerSheet) {
            this.useFBO = sheet.useFBO
            this.optimizeRenderer = sheet.optimizeRenderer
            this.overflowX = sheet.overflowX
            this.overflowY = sheet.overflowY
            this.verticalScrollbar = sheet.verticalScrollbar.copy()
            this.horizontalScrollbar = sheet.horizontalScrollbar.copy()
        }

        return this
    }

    override fun copy(): UIContainerSheet = UIContainerSheet(name).apply(this)
}