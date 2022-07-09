package net.prismclient.aether.ui.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.renderer.UIRenderer

/**
 * [UIPathDSL] is a DSL for paths. [UIRendererDSL] utilizes this to apply the paths.
 *
 * @author sen
 * @since 1.2
 */
object UIPathDSL {
    val renderer
        get() = Aether.instance.renderer

    /**
     * Clears the active path and begins a new one.
     */
    fun beginPath() = renderer.beginPath()

    /**
     * Closes the current sub-path with a line segment.
     */
    fun closePath() = renderer.closePath()

    /**
     * Instructs the renderer to fill the current path as the active color of [UIRendererDSL.color].
     *
     * @see fillPaint
     * @see strokePath
     * @see strokePaint
     * @see UIRendererDSL.color
     */
    fun fillPath() = fillPath(UIRendererDSL.color)

    /**
     * Instructs the renderer to fill the current path with the given [color].
     *
     * @see fillPaint
     * @see strokePath
     * @see strokePaint
     */
    fun fillPath(color: Int) {
        renderer.fillColor(color)
        renderer.fill()
    }

    /**
     * Instructs the renderer to use the active paint fill allocated from a gradient or image instead of a solid fill.
     *
     * @see fillPath
     * @see strokePath
     * @see strokePaint
     */
    fun fillPaint() = renderer.fillPaint()

    /**
     * Instructs the renderer to fill the current path as a stroke with the act ive color of [UIRendererDSL.color].
     *
     * @see UIRendererDSL.color
     */
    fun strokePath() = strokePath(UIRendererDSL.color)

    /**
     * Instructs the renderer to fill the current path as a stroke with the given [color].
     *
     * @see fillPath
     * @see fillPaint
     * @see strokePaint
     * @see strokeWidth
     */
    fun strokePath(color: Int) {
        renderer.strokeColor(color)
        renderer.stroke()
    }

    /**
     * Instructs the renderer to use the active paint as a stroke.
     *
     * @see fillPath
     * @see fillPaint
     * @see strokePath
     * @see strokeWidth
     */
    fun strokePaint() = renderer.strokePaint()

    /**
     * Sets the stroke width to the given value.
     */
    fun strokeWidth(width: Float) = renderer.strokeWidth(width)

    /**
     * Sets the line cap to the given [cap]
     */
    fun lineCap(cap: UIRenderer.LineCap) = renderer.lineCap(cap)

    /**
     * Creates a new sub-path with this as the first point.
     */
    fun moveTo(x: Float, y: Float) = renderer.moveTo(x, y)

    /**
     * Adds a line segment to the active path.
     */
    fun lineTo(x: Float, y: Float) = renderer.lineTo(x, y)

    /**
     * Adds a cubic bezier line segment to the active path.
     */
    fun bezierTo(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) =
        renderer.bezierTo(x, y, x1, y1, x2, y2)

    /**
     * Adds a quadratic bezier line segment to the active path.
     *
     * @param x The control x-coordinate.
     * @param y The control y-coordinate.
     */
    fun quadTo(x: Float, y: Float, x1: Float, y1: Float) = renderer.quadTo(x, y, x1, y1)

    /**
     * Adds an arc segment at the corner defined by the last path point.
     */
    fun arcTo(x: Float, y: Float, x1: Float, y1: Float, radius: Float) = renderer.arcTo(x, y, x1, y1, radius)

    /**
     * Creates an arc shaped sub-path. The center of the arc is [x], and [y]. The radius of the arc is
     * [radius], and the arc is draw from angle [startAngle] to [endAngle]. The arc is drawn in the direction
     * of [UIRenderer.WindingOrder]. The angles are represented in radians.
     */
    fun arc(
        x: Float, y: Float, radius: Float, startAngle: Float, endAngle: Float, windingOrder: UIRenderer.WindingOrder
    ) = renderer.arc(x, y, radius, startAngle, endAngle, windingOrder)

    /**
     * Creates a varying rounded rectangle shaped sub-path.
     */
    fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) = renderer.rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)

    /**
     * Creates an ellipse shaped sub-path.
     */
    fun ellipse(x: Float, y: Float, width: Float, height: Float) = renderer.ellipse(x, y, width, height)

    fun imagePattern(imageHandle: Int, x: Float, y: Float, width: Float, height: Float, angle: Float, alpha: Float) =
        renderer.imagePattern(imageHandle, x, y, width, height, angle, alpha)

    /**
     * Creates a linear gradient for the active path with the [x] and [y] as the
     * starting point and the [x2] and [y2] as the ending point.
     */
    fun linearGradient(x: Float, y: Float, x2: Float, y2: Float, startColor: Int, endColor: Int) =
        renderer.linearGradient(x, y, x2, y2, startColor, endColor)

    /**
     * Creates a radial gradient for the active path.
     *
     * @param x The x-axis coordinate of the center of the circle.
     * @param y The y-axis coordinate of the center of the circle.
     */
    fun radialGradient(x: Float, y: Float, innerRadius: Float, outerRadius: Float, startColor: Int, endColor: Int) =
        renderer.radialGradient(x, y, innerRadius, outerRadius, startColor, endColor)
}