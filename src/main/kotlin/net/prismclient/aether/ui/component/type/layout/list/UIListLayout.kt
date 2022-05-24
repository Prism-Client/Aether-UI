package net.prismclient.aether.ui.component.type.layout.list

import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout.ListOrientation.Backwards
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout.ListOrientation.Forward
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet

/**
 * [UIListLayout] is a layout class which stacks components in a Given
 * direction and orientation.
 *
 * @author sen
 * @since 5/20/2022
 */
open class UIListLayout @JvmOverloads constructor(
    var listDirection: ListDirection = ListDirection.Vertical,
    var listOrientation: ListOrientation = Forward,
    style: String
) : UIContainer<UIContainerSheet>(style) {

    override fun updateLayout() {
        var x = 0f
        var y = 0f

        if (listOrientation == Forward) {
            for (i in 0 until components.size) {
                val component = components[i]

                component.overrided = true

                if (listDirection == ListDirection.Vertical) {
                    component.y = y + component.marginTop
                    y += component.relHeight + component.marginTop + component.marginBottom
                } else if (listDirection == ListDirection.Horizontal) {
                    component.x = x + component.marginLeft
                    x += component.relWidth + component.marginLeft + component.marginRight
                }
                component.update()
            }
        } else {
            for (i in components.size - 1 downTo 0) {
                val component = components[i]

                component.overrided = true

                if (listDirection == ListDirection.Vertical) {
                    component.y = y + component.marginTop
                    y += component.relHeight + component.marginTop + component.marginBottom
                } else if (listDirection == ListDirection.Horizontal) {
                    component.x = x + component.marginLeft
                    x += component.relWidth + component.marginLeft + component.marginRight
                }
                component.update()
            }
        }
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
    enum class ListOrientation {
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