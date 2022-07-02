package net.prismclient.aether.ui.component.type.layout.auto

import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.unit.UIUnit

import net.prismclient.aether.ui.component.util.enums.UIAlignment.*

/**
 * [UIAutoLayout] is a layout which is designed to mimic the behavior of Figma's auto
 * layout. It acts somewhat like a list layout, where components are ordered in a certain
 * direction, however, depending on the properties, it also will resize based on the
 * components within it. The property [UIListLayout.listOrder] is unused.
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
     * [spacingMode] determines how the spacing between components should be handled. [SpacingMode.Packed]
     * is handled as expected; where [componentSpacing] dictates the spaces between components. However,
     * [SpacingMode.SpaceBetween] evenly spaces the component based on the leftover space which is defined
     * by the width/height properties within the style sheet, or 0 if the horizontal/vertical resizing mode is
     * [ResizingMode.Fixed].
     */
    var spacingMode: SpacingMode = SpacingMode.Packed

    /**
     * The padding of the layout, which is the spacing between the top, right, bottom, and
     * left of the layout. This adjusts only the layout, and not the component.
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
        println(relX)
        var x = x + getParentX() + +layoutPadding?.paddingLeft
        var y = y + getParentY() + -layoutPadding?.paddingTop + when (componentAlignment) {
            TOPLEFT -> 0f
            MIDDLELEFT -> height / 2f
            BOTTOMLEFT -> height
            else -> 0f
        }
        var w = 0f
        var h = 0f // if auto = the entire row of alignment is ussed
        val layoutSpacing = if (spacingMode == SpacingMode.SpaceBetween) {
            var spacing = 0f
            for (i in components.indices)
                spacing += if (listDirection == ListDirection.Vertical) {
                    components[i].relWidth
                } else {
                    components[i].relHeight
                }
            spacing / (components.size - 1).coerceAtLeast(1)
        } else {
            if (listDirection == ListDirection.Vertical) +componentSpacing else -componentSpacing
        }

        for (i in components.indices) {
            val component = components[i]
            component.style.anchor(componentAlignment)

            component.x = x + w
            component.y = y + h
            component.updateBounds()

            if (listDirection == ListDirection.Vertical)
                w += layoutSpacing + component.relWidth
            else
                h += layoutSpacing + component.relHeight
        }

        if (horizontalResizing == ResizingMode.Hug)
            width = w.coerceAtLeast(width)
        if (verticalResizing == ResizingMode.Hug)
            height = h.coerceAtLeast(height)
        updateBounds()
    }

    /**
     * Defines what the layout should do on the x and y-axis pertaining to size.
     */
    enum class ResizingMode {
        /**
         * The layout will resize based on the size of the components. The minimum value is the size of this.
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