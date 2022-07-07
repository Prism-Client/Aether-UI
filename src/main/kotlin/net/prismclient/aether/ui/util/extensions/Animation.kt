package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor

/**
 * Lerps the given value to the next based on the progress. There is no range limit for [progress].
 *
 * @see mix Lerping colors
 */
fun Int.lerp(next: Int, progress: Float): Int = this + ((next - this) * progress).toInt()

/**
 * Lerps the given value to the next based on the progress. There is no range limit for [progress].
 */
fun Float.lerp(next: Float, progress: Float): Float = this + (next - this) * progress

/**
 * Returns [next] if the progress is past 0.5.
 */
fun Boolean.lerp(next: Boolean, progress: Float): Boolean = if (progress > 0.5f) next else this

/**
 * Returns the color based on the mixing/lerping of the initial color, [this], and the ending [color]
 * based on the [progress]. If the color is null, it is considered to be 0 rgba(0, 0, 0, 0).
 */
fun Int.mix(color: Int, progress: Float): Int = asRGBA(
    color.getRed().lerp(color.getRed(), progress),
    color.getGreen().lerp(color.getGreen(), progress),
    color.getBlue().lerp(color.getBlue(), progress),
    color.getAlpha().lerp(color.getAlpha(), progress)
)

/**
 * Returns an RGBA color by mixing [this] and [color] based on the progress.
 */
fun UIColor?.mix(color: UIColor?, progress: Float): Int = (this?.rgba ?: 0).mix(color?.rgba ?: 0, progress)

/**
 * Lerps the position from the starting [UIUnit] to the ending [UIUnit]. The axis to calculate the units
 * is specified with [isY].
 */
fun UIUnit?.lerp(next: UIUnit?, component: UIComponent<*>, progress: Float, isY: Boolean): Float =
    component.computeUnit(this, isY).lerp(component.computeUnit(next, isY), progress)