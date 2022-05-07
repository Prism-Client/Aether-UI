package net.prismclient.aether.ui.renderer.impl.property

import net.prismclient.aether.ui.util.UICopy

/**
 * [UIRadius] represents a shape with 4 customizable corner radii
 *
 * @author sen
 * @since 4/26/2022
 */
class UIRadius(
    var topLeft: Float = 0f,
    var topRight: Float = 0f,
    var bottomRight: Float = 0f,
    var bottomLeft: Float = 0f
) : UICopy<UIRadius> { // TODO: Convert to UIUnit
    constructor(radius: Float) : this(radius, radius, radius, radius)

    fun set(radius: Float) {
        topLeft = radius
        topRight = radius
        bottomRight = radius
        bottomLeft = radius
    }

    override fun copy(): UIRadius = UIRadius(topLeft, topRight, bottomRight, bottomLeft)
}