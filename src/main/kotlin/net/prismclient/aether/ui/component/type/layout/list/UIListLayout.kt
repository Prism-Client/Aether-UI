package net.prismclient.aether.ui.component.type.layout.list

import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout.ListOrder.Backwards
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout.ListOrder.Forward
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.interfaces.UILayout

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
    style: String?
) : UIContainer<UIContainerSheet>(style) {
    override fun updateLayout() {
        var x = if (style.clipContent) 0f else x + getParentX()
        var y = if (style.clipContent) 0f else y + getParentY()

        if (listOrder == Forward) {
            for (i in 0 until components.size) {
                val component = components[i]

                // Some component's parent might be components within
                // this list layout. In that case it should update based
                // on it's parent on not this list layout
                if (component.parent != this)
                    continue

                if (listDirection == ListDirection.Vertical) {
                    component.y = y + component.marginTop
                    y += component.relHeight + component.marginTop + component.marginBottom
                } else if (listDirection == ListDirection.Horizontal) {
                    component.x = x + component.marginLeft
                    x += component.relWidth + component.marginLeft + component.marginRight
                }
            }
        } else {
            for (i in components.size - 1 downTo 0) {
                val component = components[i]

                if (component.parent != this)
                    continue

                if (listDirection == ListDirection.Vertical) {
                    component.y = y + component.marginTop
                    y += component.relHeight + component.marginTop + component.marginBottom
                } else if (listDirection == ListDirection.Horizontal) {
                    component.x = x + component.marginLeft
                    x += component.relWidth + component.marginLeft + component.marginRight
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