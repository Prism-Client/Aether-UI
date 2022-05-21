package net.prismclient.aether.ui.unit.util

import net.prismclient.aether.ui.component.type.image.UIImage

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
 * A relative pixel position for a unit in an animation
 */
const val PXANIMRELATIVE: Byte = 9

/**
 * A relative percentage position for a unit in an animation
 */
const val RELANIMRELATIVE: Byte = 10

/**
 * The x of the component, for animations
 */
const val XANIM: Byte = 11

/**
 * The y of the component, for animations
 */
const val YANIM: Byte = 12

/**
 * The width of the component, for animations
 */
const val WIDTHANIM: Byte = 13

/**
 * The height of the component, for animations
 */
const val HEIGHTANIM: Byte = 14

/**
 * Unique to [UIImage] components. Represents the width of the loaded image
 */
const val IMAGEWIDTH: Byte = 15
/**
 * Unique to [UIImage] components. Represents the height of the loaded image
 */
const val IMAGEHEIGHT: Byte = 16