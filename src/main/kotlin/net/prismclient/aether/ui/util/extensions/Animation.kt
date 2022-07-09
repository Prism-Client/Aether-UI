package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.util.UIColor

@JvmName("extLerp")
fun Int.lerp(next: Int, progress: Float): Int = this + ((next - this) * progress).toInt()

/**
 * Lerps the given value to the next based on the progress. There is no range limit for [progress].
 */
@JvmName("extLerp")
fun Float.lerp(next: Float, progress: Float): Float = this + ((next - this) * progress)

/**
 * Non-extension lerp function. (it rhymes!!!!)
 *
 * @see Int.lerp
 */
fun lerp(a: Int, b: Int, progress: Float): Int = a.lerp(b, progress)

/**
 * Non-extension lerp function. (it rhymes!!!!)
 *
 * @see Float.lerp
 */
fun lerp(a: Float, b: Float, progress: Float): Float = a.lerp(b, progress)

/**
 * Returns [next] if the progress is past 0.5.
 */
fun Boolean.lerp(next: Boolean, progress: Float): Boolean = if (progress > 0.5f) next else this

/**
 * Returns the color based on the mixing/lerping of the initial color, [this], and the ending [color]
 * based on the [progress]. If the color is null, it is considered to be 0 rgba(0, 0, 0, 0).
 */
fun Int.mix(color: Int, progress: Float): Int = transition(this, color, progress)

/**
 * Returns an RGBA color by mixing [this] and [color] based on the progress.
 */
fun UIColor?.mix(color: UIColor?, progress: Float): Int = (this?.rgba ?: 0).mix(color?.rgba ?: 0, progress)

// TODO: Mix function with HSV instead of RGB

fun UIColor?.mix(color: UIColor?, default: UIColor, progress: Float): Int =
    (this?.rgba ?: default.rgba).mix(color?.rgba ?: default.rgba, progress)

/**
 * Lerps the position from the starting [UIUnit] to the ending [UIUnit]. The axis to calculate the units
 * is specified with [isY].
 */
fun UIUnit?.lerp(next: UIUnit?, component: UIComponent<*>, progress: Float, isY: Boolean): Float =
    component.computeUnit(this, isY).lerp(component.computeUnit(next, isY), progress)

fun UIUnit?.lerp(next: UIUnit?, component: UIComponent<*>, default: UIUnit?, progress: Float, isY: Boolean): Float =
    component.computeUnit(this ?: default, isY).lerp(component.computeUnit(next ?: default, isY), progress)