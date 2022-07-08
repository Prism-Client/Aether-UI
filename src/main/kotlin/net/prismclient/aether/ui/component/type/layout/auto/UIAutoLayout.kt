package net.prismclient.aether.ui.component.type.layout.auto

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIFrameSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.util.interfaces.UICopy

/**
 * [UIAutoLayout] is a layout which is designed to mimic the behavior of Figma's auto
 * layout. It acts somewhat like a list layout, where components are ordered in a certain
 * direction, however, depending on the properties, it also will resize based on the
 * components within it. The property [UIListLayout.listOrder] is unused.
 *
 * Due to how this class may be used many times, a copy method is provided to avoid the need
 * to manually declare it each time. The properties of this are copied excluding the [components].
 * The style is referenced from [UIProvider], so changes made directly to the style of this component
 * via the style block, or any other way will not be applied to the copied version of the component.
 * Furthermore, the parent property of [UIComponent] is ignored. Furthermore, it is suggested NOT to use
 * the [UIFrameSheet.useFBO] as it can take up an unnecessarily large amount of memory, it is instead
 * suggested to simply contain the layouts within another container with the property enabled if an FBO
 * is desired to be used.
 *
 * @author sen
 * @since 1.1
 */
class UIAutoLayout @JvmOverloads constructor(
    listDirection: ListDirection = ListDirection.Horizontal,
    style: String?
) : UIListLayout(listDirection, ListOrder.Forward, style), UICopy<UIAutoLayout> {
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
    var componentAlignment: UIAlignment = TOPLEFT

    override fun updateLayout() {
        if (components.isEmpty()) return

        // Calculate the padding and spacing
        val top = -layoutPadding?.paddingTop
        val right = +layoutPadding?.paddingRight
        val bottom = -layoutPadding?.paddingBottom
        val left = +layoutPadding?.paddingLeft
        val spacing = if (listDirection == ListDirection.Horizontal) +componentSpacing else -componentSpacing

        // Calculate the width of the layout
        var w = 0f
        var h = 0f

        for (i in components.indices) {
            if (horizontalResizing == ResizingMode.Hug) {
                w = if (listDirection == ListDirection.Horizontal) {
                    w + components[i].relWidth + if (i < components.size - 1) spacing else 0f
                } else {
                    components[i].relWidth.coerceAtLeast(w)
                }
            }
            if (verticalResizing == ResizingMode.Hug) {
                h = if (listDirection == ListDirection.Vertical) {
                    h + components[i].relHeight + if (i < components.size - 1) spacing else 0f
                } else {
                    components[i].relHeight.coerceAtLeast(h)
                }
            }
        }

        // Adjust the width and/or height of the component based on the calculated
        // size, and ensure that the size is at least the size prior to this.
        if (horizontalResizing == ResizingMode.Hug)
            width = (w + left + right).coerceAtLeast(width)
        if (verticalResizing == ResizingMode.Hug)
            height = (h + top + bottom).coerceAtLeast(height)

        // Update
        calculateBounds()
        updateAnchorPoint()
        updatePosition()
        updateBounds()
        updateStyle()

        // Calculate the initial position based on the alignment
        var x = this.x + getParentX() + left
        var y = this.y + getParentY() + top

        // Update the other direction's alignment
        if (listDirection == ListDirection.Horizontal) {
            x += when (componentAlignment) {
                TOPCENTER, CENTER, BOTTOMCENTER -> (width - (w + left + right)) / 2f
                TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> (width - (w + left + right))
                else -> 0f
            }
        } else if (listDirection == ListDirection.Vertical) {
            y += when (componentAlignment) {
                MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - (h + top + bottom)) / 2f
                BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> (height - (h + top + bottom))
                else -> 0f
            }
        }

        for (c in components) {
            c.overridden = true
            if (listDirection == ListDirection.Horizontal) {
                c.x = x
                c.y = y + when (componentAlignment) {
                    TOPLEFT, TOPCENTER, TOPRIGHT -> 0f
                    MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - c.height - top - bottom) / 2f
                    BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> (height - c.height - left - right)
                    else -> 0f
                }
                x += c.width + spacing
            } else if (listDirection == ListDirection.Vertical) {
                c.x = x + when (componentAlignment) {
                    TOPLEFT, MIDDLELEFT, BOTTOMLEFT -> 0f
                    TOPCENTER, CENTER, BOTTOMCENTER -> (width - c.width - left - right) / 2f
                    TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> (width - c.width - left - right)
                    else -> 0f
                }
                c.y = y
                y += c.height + spacing
            }
            c.update()
        }
    }

    override fun requestUpdate() {
        super.requestUpdate()
        update()
    }

    /**
     * Copy the properties of this layout to a new one (excluding components).
     */
    override fun copy(): UIAutoLayout = UIAutoLayout(listDirection, style.name).also {
        // UIAutoLayout properties
        it.horizontalResizing = horizontalResizing
        it.verticalResizing = verticalResizing
        it.spacingMode = spacingMode
        it.layoutPadding = layoutPadding?.copy()
        it.componentAlignment = componentAlignment

        // UIListLayout properties
        it.componentSpacing = componentSpacing?.copy()

        // UIContainer properties
        it.scrollSensitivity = scrollSensitivity

        // UIComponent
        it.visible = visible
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