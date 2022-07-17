package net.prismclient.aether.ui.renderer.impl.font

/**
 * The equivalent of Vertical and Horizontal text alignment in Figma. Certain properties cannot be used
 * on the certain axis. See the enum documentation for more information.
 *
 * @author sen
 * @since 1.3
 */
enum class TextAlignment {
    /**
     * Aligns to the left on the x-axis
     */
    LEFT,

    /**
     * Aligns to the center / middle on the x/y-axis
     */
    CENTER,

    /**
     * Aligns to the right of the x-axis
     */
    RIGHT,

    /**
     * Aligns the text to the top on the y-axis
     */
    TOP,

    //CENTER//

    /**
     * Aligns to the text to the bottom on the y-axis
     */
    BOTTOM,
}