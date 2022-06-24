package net.prismclient.aether.components.promotion

import net.prismclient.aether.ui.style.UIStyleSheet

class PromotionStyleSheet : UIStyleSheet() {
    var buttonColor: Int = 0

    /**
     * In order to avoid a ClassCastException, or something of the sort
     * the copy method needs to be implemented.
     *
     * This function expects a deep copy; a new instance of everything
     * that does not tie back to the original, but has identical properties.
     */
    override fun copy(): PromotionStyleSheet = PromotionStyleSheet().also {
        // A function provided by UIStyleSheet. It applies/copies all the
        // default properties to the new instance, so you don't have to.
        it.apply(this)

        // However, for properties defined in this class, we need to copy them.
        // All properties should be inheriting the UICopy interface. UIUnits and
        // UIRadius already do this for you. For classes that you create, you need
        // manually implement the copy interface.
        //
        // Note that you don't have to do this for Primitives (int, float, etc.) as they are immutable
        it.buttonColor = buttonColor
    }
}