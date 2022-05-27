package net.prismclient.aether.ui.unit.util

import net.prismclient.aether.ui.component.type.image.UIImage

/**
 * The default unit type. Represents the value in pixels.
 */
const val PIXELS: Byte = 0

/**
 * Represents the screen width or height, depending on the unit times the value.
 */
const val RELATIVE: Byte = 1

/**
 * The font size of the active component
 */
const val EM: Byte = 2

// TODO: Add REM as a unit. Add border as a supported unit

/**
 * The border width of the active component
 */
const val BORDER: Byte = 3

const val AUTO: Byte = 4

/**
 *  Used only for grids. Like relatives, except for the remaining space in a grid
 */
const val FRACTION: Byte = 5

/**
 *  Used for grids. Keeps the initial size of the component
 */
const val INITIAL: Byte = 6

/**
 * The ascender of the current stylesheet's font
 */
const val ASCENDER: Byte = 7

/**
 * The descender portion of the current stylesheet's font
 */
const val DESCENDER: Byte = 8

/**
 * Unique to [UIImage] components. Represents the width of the loaded image
 */
const val IMAGEWIDTH: Byte = 15

/**
 * Unique to [UIImage] components. Represents the height of the loaded image
 */
const val IMAGEHEIGHT: Byte = 16