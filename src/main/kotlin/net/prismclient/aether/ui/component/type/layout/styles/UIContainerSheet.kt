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
    var overflowY: Overflow = Overflow.None

    var scrollbarX: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Vertical)
    var scrollbarY: UIScrollbar = UIScrollbar(UIScrollbar.Scrollbar.Horizontal)

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
        Scroll,
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

        it.scrollbarX = scrollbarX.copy()
        it.scrollbarY = scrollbarY.copy()

        return it
    }
}