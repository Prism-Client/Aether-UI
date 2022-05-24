package net.prismclient.aether.ui.component.type.layout.styles

import net.prismclient.aether.ui.renderer.impl.scrollbar.UIScrollbar
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIContainerSheet]
 *
 * @author sen
 * @since 5/12/2022
 */
open class UIContainerSheet : UIFrameSheet() {
    var overflowX: Overflow = Overflow.Auto
    var overflowY: Overflow = Overflow.Auto

    var verticalScrollbar: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Vertical)
    var horizontalScrollbar: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Horizontal)

    inline fun verticalScrollbar(block: UIScrollbar.() -> Unit) = verticalScrollbar.block()

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

    override fun copy(): UIStyleSheet {
        val it = UIContainerSheet()

        it.apply(this)

        it.frameWidth = frameWidth?.copy()
        it.frameHeight = frameHeight?.copy()

        it.clipContent = clipContent
        it.contentRadius = contentRadius

        it.overflowX = overflowX
        it.overflowY = overflowY

        it.verticalScrollbar = verticalScrollbar.copy()
        it.horizontalScrollbar = horizontalScrollbar.copy()

        return it
    }
}