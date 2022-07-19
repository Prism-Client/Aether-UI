package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.frame.UIFrameLayout
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.impl.property.UIPadding
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.extensions.px
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
class UIAutoLayout @JvmOverloads constructor(layoutDirection: UILayoutDirection = UILayoutDirection.Horizontal) : UIFrameLayout<UIFrameSheet>(), UICopy<UIAutoLayout> {
    /**
     * Defines the direction/axis which the list should flow. (Horizontal or Vertical).
     */
    var layoutDirection: UILayoutDirection = layoutDirection

    /**
     * The alignment which the components within are aligned to based on the empty space within the layout.
     */
    var componentAlignment: UIAlignment = TOPLEFT

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
     * The spacing between each component within the layout.
     */
    var componentSpacing: UIUnit? = null

    private var layoutWidth: Float = 0f
    private var layoutHeight: Float = 0f

    // -- Shorthands -- //

    /**
     * Sets the [horizontalResizing] and [verticalResizing] t the given [mode].
     */
    infix fun resize(mode: ResizingMode) = resize(mode, mode)

    /**
     * Sets the [horizontalResizing] and [verticalResizing] to [horizontal] and [vertical] respectively.
     */
    fun resize(horizontal: ResizingMode, vertical: ResizingMode): UIAutoLayout {
        horizontalResizing = horizontal
        verticalResizing = vertical
        return this
    }

    /**
     * Sets the [horizontalResizing] and [verticalResizing] to hug.
     */
    fun hug() = resize(ResizingMode.Hug)

    infix fun space(value: Number) = space(px(value))

    infix fun space(unit: UIUnit?): UIAutoLayout {
        componentSpacing = unit
        return this
    }

    // -- Core -- //

    override fun updateLayout() {
        if (components.isEmpty())
            return

        // Calculate the padding and spacing
        val top = -layoutPadding?.paddingTop
        val right = +layoutPadding?.paddingRight
        val bottom = -layoutPadding?.paddingBottom
        val left = +layoutPadding?.paddingLeft
        val spacing = if (layoutDirection == UILayoutDirection.Horizontal) +componentSpacing else -componentSpacing

        // Calculate the size of this layout based on the component twice. The first time is to get the initial size
        // of the layout, which might change if ran again because the components might depend on the size of this.
        // The second time is to get the final size of the layout, which is the size of the components.
        for (i in 0 until 2) {
            // Calculate the layout size
            calculateLayoutSize(spacing)

            // Adjust the width and/or height of the component based on the calculated
            // size, and ensure that the size is at least the size prior to this.
            if (horizontalResizing == ResizingMode.Hug) width = (layoutWidth + left + right)//.coerceAtLeast(width)
            if (verticalResizing == ResizingMode.Hug) height = (layoutHeight + top + bottom)//.coerceAtLeast(height)

            // Update
            calculateBounds()
            updateAnchorPoint()
            updatePosition()
            updateBounds()
            updateStyle()

            components.forEach { it.update() }
        }

        val w = layoutWidth
        val h = layoutHeight

        // Calculate the initial position based on the alignment
        var x = this.x + left
        var y = this.y + top

        // Update the other direction's alignment
        if (layoutDirection == UILayoutDirection.Horizontal) {
            x += when (componentAlignment) {
                TOPCENTER, CENTER, BOTTOMCENTER -> (width - (w + left + right)) / 2f
                TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> (width - (w + left + right))
                else -> 0f
            }
        } else if (layoutDirection == UILayoutDirection.Vertical) {
            y += when (componentAlignment) {
                MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - (h + top + bottom)) / 2f
                BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> (height - (h + top + bottom))
                else -> 0f
            }
        }

        for (c in components) {
            c.overridden = true
            if (layoutDirection == UILayoutDirection.Horizontal) {
                c.x = x
                c.y = y + when (componentAlignment) {
                    TOPLEFT, TOPCENTER, TOPRIGHT -> 0f
                    MIDDLELEFT, CENTER, MIDDLERIGHT -> (height - c.height - top - bottom) / 2f
                    BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> (height - c.height - left - right)
                    else -> 0f
                } + c.marginTop - c.marginBottom
                x += c.width + spacing
            } else if (layoutDirection == UILayoutDirection.Vertical) {
                c.x = x + when (componentAlignment) {
                    TOPLEFT, MIDDLELEFT, BOTTOMLEFT -> 0f
                    TOPCENTER, CENTER, BOTTOMCENTER -> (width - c.width - left - right) / 2f
                    TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> (width - c.width - left - right)
                    else -> 0f
                } + c.marginLeft - c.marginRight
                c.y = y
                y += c.height + spacing
            }
            c.update()
        }
    }

    private fun calculateLayoutSize(spacing: Float) {
        var w = 0f
        var h = 0f

        for (i in components.indices) {
            if (horizontalResizing == ResizingMode.Hug) {
                w = if (layoutDirection == UILayoutDirection.Horizontal) {
                    w + components[i].relWidth + if (i < components.size - 1) spacing else 0f
                } else {
                    components[i].relWidth.coerceAtLeast(w)
                }
            }
            if (verticalResizing == ResizingMode.Hug) {
                h = if (layoutDirection == UILayoutDirection.Vertical) {
                    h + components[i].relHeight + if (i < components.size - 1) spacing else 0f
                } else {
                    components[i].relHeight.coerceAtLeast(h)
                }
            }
        }

        layoutWidth = w
        layoutHeight = h
    }

    override fun requestUpdate() {
        super.requestUpdate()
        update()
    }

    /**
     * Copy the properties of this layout to a new one (excluding components).
     */
    override fun copy(): UIAutoLayout = UIAutoLayout(layoutDirection).also {
        // UIAutoLayout properties
        it.horizontalResizing = horizontalResizing
        it.verticalResizing = verticalResizing
        it.spacingMode = spacingMode
        it.layoutPadding = layoutPadding?.copy()
        it.componentAlignment = componentAlignment

        // UIListLayout properties
        it.componentSpacing = componentSpacing?.copy()

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