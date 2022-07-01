package net.prismclient.aether.ui.component.type.layout.auto

import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.unit.UIUnit

/**
 * [UIAutoLayout] is a layout which is designed to mimic the behavior of Figma's auto
 * layout. It acts somewhat like a list layout, where components are ordered in a certain
 * direction, however, depending on the properties, it also will resize based on the
 * components within it.
 *
 * @author sen
 * @since 1.1
 */
class UIAutoLayout(style: String?) : UIListLayout(ListDirection.Vertical, ListOrder.Forward, style) {
    /**
     * Defines how the width should be sized. [ResizingMode.Hug] resizes based on the components and
     * the padding and spacing properties, and [ResizingMode.Fixed] acts like a normal component.
     *
     * @see verticalResizing
     */
    var horizontalResizing: ResizingMode = ResizingMode.Fixed

    /**
     * Defines how the height should be sized. [ResizingMode.Hug] resizes based on the components
     * and the padding and spacing properties, and [ResizingMode.Fixed] acts like a normal component.
     *
     * @see horizontalResizing
     */
    var verticalResizing: ResizingMode = ResizingMode.Fixed

    /**
     * [spacingMode] determines how the spacing between components should be handled. If [SpacingMode.Packed]
     * the spacing is handled as expected; where [componentSpacing] dictates the spaces between components. However,
     * the [SpacingMode.SpaceBetween] evenly spaces the component based on the leftover space which is defined
     * by the width/height properties within the style sheet, or 0 if the horizontal/vertical resizing mode is
     * [ResizingMode.Fixed].
     */
    var spacingMode: SpacingMode = SpacingMode.Packed

    /**
     * The padding of the layout, which is the spacing between the top, right, bottom, and left of the layout.
     */
    var layoutPadding: UIPadding? = null

    /**
     * The direction to align the components within the layout.
     */
    var componentAlignment: UIAlignment = UIAlignment.TOPLEFT

    /**
     * The spacing between the individual components
     */
    var componentSpacing: UIUnit? = null

    override fun updateLayout() {
        
    }

    /**
     * Defines what the layout should do on the x and y-axis pertaining to size.
     */
    enum class ResizingMode {
        /**
         * The layout will resize based on the size of the components.
         */
        Hug,

        /**
         * The layout will follow the size of the style's width and height (like other components).
         */
        Fixed
    }

    /**
     * Defines how the spacing between components should be handled.
     */
    enum class SpacingMode {
        /**
         * Spaces the components based on [componentSpacing].
         */
        Packed,

        /**
         * Like the auto CSS grid layout property: spaces the components evenly based on the leftover
         * space. If the resize mode is Hug, the spacing is 0, as there is no extra space.
         */
        SpaceBetween
    }
}