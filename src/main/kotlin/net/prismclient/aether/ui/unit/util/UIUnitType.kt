package net.prismclient.aether.ui.unit.util

/**
 *  A value represented in pixels
 */
const val PIXELS: Byte = 0

/**
 *  Decimal value representing the parent's width/height
 */
const val RELATIVE: Byte = 1

/**
 * The height of the active component
 */
const val EM: Byte = 2
const val AUTO: Byte = 3

/**
 *  Used only for grids. Like relatives, except for the remaining space in a grid
 */
const val FRACTION: Byte = 4

/**
 *  Used for grids. Keeps the initial size of the component
 */
const val INITIAL: Byte = 5
const val COPY: Byte = 6

/**
 * The ascender of the current stylesheet's font
 */
const val ASCENDER: Byte = 7

/**
 * The descender portion of the current stylesheet's font
 */
const val DESCENDER: Byte = 8

/**
 * A relative pixel position for a unit in an animation
 */
const val PXANIMRELATIVE: Byte = 9

/**
 * A relative percentage position for a unit in an animation
 */
const val RELANIMRELATIVE: Byte = 10