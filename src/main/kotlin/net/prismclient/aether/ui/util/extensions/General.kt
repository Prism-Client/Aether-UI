package net.prismclient.aether.ui.util.extensions

/**
 * Returns the value based on a [start] and [end] relative to the [progress]
 */
fun fromProgress(start: Float, end: Float, progress: Float) = start + ((end - start) * progress)