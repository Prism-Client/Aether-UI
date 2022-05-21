package net.prismclient.aether.ui.component.type.layout.list

import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout.ListOrientation.Backwards
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
        var listOrientation: ListOrientation = ListOrientation.Forward,
        style: String
) : UIContainer<UIContainerSheet>(style) {

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