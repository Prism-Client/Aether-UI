package examples

import examples.deps.Generic
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.layout.UIFrame
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.px
import java.util.*
import kotlin.collections.ArrayList

/**
 * Auto Layouts are a neat feature which is designed to mimic the auto layout
 * of Figma. Here is an example screen which utilizes these auto layouts to create
 * a list of buttons.
 *
 * Auto Layouts, in layman terms, are a more advanced version of list layouts. It
 * gives spacing and padding properties as well as better alignment support which
 * list layouts and other layouts lack. It is one of the few [UIFrame] which are
 * designed to be used in mass amounts.
 *
 * @author sen
 * @since 7/3/2022
 * @see UIAutoLayout For more information about the specifics.
 */
class AutoLayouts : UIScreen {
    override fun build() {
        // Start a `create` block as usual to get a UIComponentDSL block
        create {
            // Include the UIDependable, so we can use the styles from it
            include(Generic())

            // There are two (suggested) ways to declare Auto Layouts. If intend
            // to use auto layout, once (uncommon), you can simply declare the
            // layout using `UIComponentDSL.component()

            /*
            component(UIAutoLayout(UIListLayout.ListDirection.Horizontal, null)) {
                 Define properties of the layout, and components...
            }
            */

            // Generally you will need to reuse the same layout so instead
            // define the layout as a variable instead.

            // ListDirection -> Vertical or Horizontal
            val layout = UIAutoLayout(UIListLayout.ListDirection.Horizontal, null)
                .style(UIContainerSheet(("someStyle"))) {
                    // Declare the style inline, as we only use it once
                    background(asRGBA(59, 145, 255), radiusOf(9f))
                    padding = paddingOf(10)
                }

            // Create a DSL block using this shorthand
            blockFrom(layout) {
                // Let's say we want an icon on the left, and text on the right
                // which is all centered to the middle component. Oh, and let's
                // give some padding to the edges, and let it automatically size itself
                // (We declare the components below)
                componentAlignment = UIAlignment.CENTER // Align the stuff to the center

                // Resize to the size of the layout. (Not) Fun Fact: Setting of the size
                // of the component ensures that this it at least that value.
                horizontalResizing = UIAutoLayout.ResizingMode.Hug
                verticalResizing = UIAutoLayout.ResizingMode.Hug

                // Space the components by 10px...
                componentSpacing = px(10)

                // Set the padding to 10
                // Alternatively paddingOf(Unit/Number, Unit/Number, Unit/Number, Unit/Number)
                layoutPadding = paddingOf(10)
            }

            // To use this, invoke the UIComponentDSL.autoLayout() function!
            list(UIListLayout.ListDirection.Vertical, UIListLayout.ListOrder.Forward) {
                componentSpacing = px(10)

                autoLayout(layout) {
                    // Define your components here!
                    image("ui", "icon24x")
                    text("User Interface", "generic-font")
                }.style {
                    margin {
                        marginTop = px(25)
                    }
                }

                val asd = autoLayout(layout) {
                    // Define your components here!
                    image("ui", "icon24x")
                    text("Some more user interface!!!!", "generic-font")
                }

                autoLayout(layout) {
                    // Define your components here!
                    text("Reversed icons???", "generic-font")
                    image("ui", "icon24x")
                }
            }.style(UIContainerSheet("autoLayoutList")) {
                size(400, 500)
//                background(asRGBA(0.95f, 0.95f, 0.95f, 1f), radiusOf(9f))
            }
        }
    }
}