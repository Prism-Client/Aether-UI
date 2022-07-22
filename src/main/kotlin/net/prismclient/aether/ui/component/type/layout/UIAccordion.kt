package net.prismclient.aether.ui.component.type.layout

import net.prismclient.aether.ui.dsl.UIComponentDSL
import net.prismclient.aether.ui.util.Block
import net.prismclient.aether.ui.util.Listener
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.interfaces.UITriFunction
import net.prismclient.aether.ui.util.ucreate
import java.util.function.BiConsumer
import java.util.function.Function

/**
 * [UIAccordion] is a sequence of dropdowns, which hold a container within them. The variables [informationContainer]
 * and [contentContainer] must be set prior to the screen creation finalization. Those variables represent the two
 * containers which are created for each item via the [item] function.
 *
 * @author sen
 * @since 1.3
 */
class UIAccordion : UIListLayout(UILayoutDirection.Vertical) {
    val items = mutableSetOf<AccordionItem>()

    /**
     * The function which provides a [UIContainer] for the [UIAccordion]. This must be set.
     *
     *      (item: AccordionItem, title: String, description: String) -> UIContainer<UIContainerSheet>
     */
    lateinit var informationContainer: UITriFunction<AccordionItem, String, String, UIContainer<UIContainerSheet>>

    /**
     * The function expect an instance of [UIContainer], a [UIContainer] and provides the item, an [AccordionItem]. This must be set.
     */
    lateinit var contentContainer: Function<AccordionItem, UIContainer<UIContainerSheet>>

    /**
     * A bi-consumer which is invoked when the content is collapsed or expanded. The second parameter is known as shouldCollapse.
     */
    lateinit var contentChange: BiConsumer<AccordionItem, Boolean>

    override fun updateLayout() {
        super.updateLayout()
        var h = 0f ///-  if (!style.useFBO) y else 0f
        for (child in components) {
            h = (child.relY + child.relHeight + child.marginBottom).coerceAtLeast(h)
        }
        height =  h - y
        updateAnchorPoint()
        updatePosition()
        updateBounds()
        updateStyle()
        super.updateLayout()
    }

    override fun update() {
        super.update()
        items.forEach { adjustDropdown(it, it.open) }
    }

    /**
     * Creates an item with the given [name] and [description] which applies to the
     * [informationContainer]. The [block]  is invoked onto the [contentContainer], so
     * any components intended to be within the dropdown should be defined within it.
     */
    inline fun item(name: String, description: String, open: Boolean, block: Block<UIContainer<UIContainerSheet>>): AccordionItem {
        val item = AccordionItem(name, description, open)
        item.dropdown = informationContainer.invoke(item, name, description)
        item.content = contentContainer.apply(item)
        UIComponentDSL.component(item.content, null, block)
        items += item
        return item
    }

    fun changeDropdown(item: AccordionItem) = adjustDropdown(item, !item.open)

    fun adjustDropdown(item: AccordionItem, shouldCollapse: Boolean) {
        item.open = shouldCollapse
        contentChange.accept(item, shouldCollapse)
        println(item.content.relHeight)
        updateLayout()
    }

    /**
     * [AccordionItem] represents an item within an [UIAccordion]. It contains the two
     * containers as well as the name and description of the accordion item.
     *
     * @author sen
     * @since 1.3
     */
    inner class AccordionItem(val name: String, val description: String, var open: Boolean) {
        lateinit var dropdown: UIContainer<UIContainerSheet>
        lateinit var content: UIContainer<UIContainerSheet>
    }
}