package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.unit.UIUnit

/**
 * Calculates the position from the starting [UIUnit] to the ending [UIUnit]. The axis
 * to calculate the units on can be specified with [isY]
 */
fun UIUnit?.animateTo(next: UIUnit?, progress: Float, isY: Boolean): Float {
    return 0f
}

/**
 * Returns the color based on the mixing of the initial color, [this], and the
 * ending [color] based on the [progress].
 */
fun Int?.colorTo(color: Int, progress: Float): Int {
    return 0
}