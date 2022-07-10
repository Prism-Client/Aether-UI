package net.prismclient.aether.ui.renderer.impl.border

/**
 * Instructs the renderer how to render the stroke. When center, the stroke is
 * centered to the outline of the given shape. Outside, is the outside of the
 * shape, and inside is the inside of the shape.
 *
 * @author sen
 * @since 1.2
 */
enum class UIStrokeDirection {
    CENTER, OUTSIDE, INSIDE
}