package net.prismclient.aether.ui.shape

/**
 * Exactly like [UIShape], except with a color property
 *
 * @author sen
 * @since 6/3/2022
 */
abstract class UIColoredShape : UIShape() {
    var color: Int = 0

    override fun apply(shape: UIShape): UIColoredShape {
        x = shape.x?.copy()
        y = shape.y?.copy()
        width = shape.width?.copy()
        height = shape.height?.copy()
        color = (shape as? UIColoredShape)?.color ?: color
        return this
    }
}