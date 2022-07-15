package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.component.type.layout.UIListLayout.ListOrder.Backwards
import net.prismclient.aether.ui.component.type.layout.UIListLayout.ListOrder.Forward
import net.prismclient.aether.ui.unit.UIUnit

/**
 * [UIListLayout] is a layout class which stacks components in a Given
 * direction and orientation.
 *
 * @author sen
 * @since 5/20/2022
 */
open class UIListLayout @JvmOverloads constructor(
    var listDirection: ListDirection = ListDirection.Vertical,
    var listOrder: ListOrder = Forward,
) : UIContainer<UIContainerSheet>() {
    /**
     * The spacing between each component in the layout.
     */
    var componentSpacing: UIUnit? = null

    override fun updateLayout() {
        var x = if (style.useFBO) 0f else x + getParentX()
        var y = if (style.useFBO) 0f else y + getParentY()
        val spacing = if (listDirection == ListDirection.Horizontal) +componentSpacing else -componentSpacing

        if (listOrder == Forward) {
            for (i in 0 until components.size) {
                val component = components[i]

                // Some component's parent might be components within
                // this list layout. In that case it should update based
                // on it's parent on not this list layout
                if (component.parent != this)
                    continue

                component.overridden = false
                component.updatePosition()
                component.overridden = true

                if (listDirection == ListDirection.Vertical) {
                    component.y = y + component.marginTop
                    y += component.relHeight + component.marginTop + component.marginBottom + spacing
                } else if (listDirection == ListDirection.Horizontal) {
                    component.x = x + component.marginLeft
                    x += component.relWidth + component.marginLeft + component.marginRight + spacing
                }
            }
        } else {
            for (i in components.size - 1 downTo 0) {
                val component = components[i]

                if (component.parent != this)
                    continue

                component.overridden = false
                component.updatePosition()
                component.overridden = true

                if (listDirection == ListDirection.Vertical) {
                    component.y = y + component.marginTop
                    y += component.relHeight + component.marginTop + component.marginBottom - spacing
                } else if (listDirection == ListDirection.Horizontal) {
                    component.x = x + component.marginLeft
                    x += component.relWidth + component.marginLeft + component.marginRight - spacing
                }
            }
        }

        for (c in components)
            c.update()
    }

    /**
     * Defines which direction the list is rendered in
     */
    enum class ListDirection {
        /**
         * Renders the list horizontally
         */
        Horizontal,

        /**
         * Renders the list vertically
         */
        Vertical
    }

    /**
     * Defines the orientation which the lists order from. For example if
     * it is [Backwards], the last component is ordered first, and the first
     * component is ordered last.
     */
    enum class ListOrder {
        /**
         * The default, orders the list as the first component first, and the last component last
         */
        Forward,

        /**
         * The opposite of [Forward], the last component is ordered first, and the first component is ordered last
         */
        Backwards
    }
}