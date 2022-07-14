package net.prismclient.aether.ui.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.util.Block

/**
 * [UIPathDSL] is a DSL for paths. [UIRendererDSL] utilizes this to apply the paths.
 *
 * @author sen
 * @since 1.2
 */
object UIPathDSL {
    @JvmStatic
    val renderer
        get() = Aether.renderer

    /**
     * Clears the active path and begins a new one.
     */
    @JvmStatic
    fun beginPath() = renderer.beginPath()

    /**
     * Closes the current sub-path with a line segment.
     */
    @JvmStatic
    fun closePath() = renderer.closePath()

    /**
     * Instructs the renderer to fill the current path as the active color of [UIRendererDSL.activeColor].
     *
     * @see fillPaint
     * @see strokePath
     * @see strokePaint
     * @see UIRendererDSL.activeColor
     */
    @JvmStatic
    fun fillPath() = fillPath(UIRendererDSL.activeColor)

    /**
     * Instructs the renderer to fill the current path with the given [color].
     *
     * @see fillPaint
     * @see strokePath
     * @see strokePaint
     */
    @JvmStatic
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
    @JvmStatic
    fun fillPaint() = renderer.fillPaint()

    /**
     * Instructs the renderer to fill the current path as a stroke with the act ive color of [UIRendererDSL.activeColor].
     *
     * @see UIRendererDSL.activeColor
     */
    @JvmStatic
    fun strokePath() = strokePath(UIRendererDSL.activeColor)

    /**
     * Instructs the renderer to fill the current path as a stroke with the given [color].
     *
     * @see fillPath
     * @see fillPaint
     * @see strokePaint
     * @see strokeWidth
     */
    @JvmStatic
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
    @JvmStatic
    fun strokePaint() = renderer.strokePaint()

    /**
     * Sets the stroke width to the given value.
     */
    @JvmStatic
    fun strokeWidth(width: Float) = renderer.strokeWidth(width)

    /**
     * Sets the line cap to the given [cap]
     */
    @JvmStatic
    fun lineCap(cap: UIRenderer.LineCap) = renderer.lineCap(cap)

    /**
     * Creates a new sub-path with this as the first point.
     */
    @JvmStatic
    fun moveTo(x: Float, y: Float) = renderer.moveTo(x, y)

    /**
     * Adds a line segment to the active path.
     */
    @JvmStatic
    fun lineTo(x: Float, y: Float) = renderer.lineTo(x, y)

    /**
     * Adds a cubic bezier line segment to the active path.
     */
    @JvmStatic
    fun bezierTo(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) =
        renderer.bezierTo(x, y, x1, y1, x2, y2)

    /**
     * Adds a quadratic bezier line segment to the active path.
     *
     * @param x The control x-coordinate.
     * @param y The control y-coordinate.
     */
    @JvmStatic
    fun quadTo(x: Float, y: Float, x1: Float, y1: Float) = renderer.quadTo(x, y, x1, y1)

    /**
     * Adds an arc segment at the corner defined by the last path point.
     */
    @JvmStatic
    fun arcTo(x: Float, y: Float, x1: Float, y1: Float, radius: Float) = renderer.arcTo(x, y, x1, y1, radius)

    /**
     * Creates an arc shaped sub-path. The center of the arc is [x], and [y]. The radius of the arc is
     * [radius], and the arc is draw from angle [startAngle] to [endAngle]. The arc is drawn in the direction
     * of [UIRenderer.WindingOrder]. The angles are represented in radians.
     */
    @JvmStatic
    fun arc(
        x: Float, y: Float, radius: Float, startAngle: Float, endAngle: Float, windingOrder: UIRenderer.WindingOrder
    ) = renderer.arc(x, y, radius, startAngle, endAngle, windingOrder)

    /**
     * Renders a rectangle sub-path with the given bounds and [radius].
     */
    @JvmStatic
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) = rect(
        x,
        y,
        width,
        height,
        radius?.topLeft ?: 0f,
        radius?.topRight ?: 0f,
        radius?.bottomRight ?: 0f,
        radius?.bottomLeft ?: 0f
    )

    /**
     * Creates a rectangle sub-path with a single radius value.
     */
    @JvmStatic
    @JvmOverloads
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        rect(x, y, width, height, radius, radius, radius, radius)

    /**
     * Creates a varying rounded rectangle shaped sub-path.
     */
    @JvmStatic
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
    @JvmStatic
    fun ellipse(x: Float, y: Float, width: Float, height: Float) = renderer.ellipse(x, y, width, height)

    @JvmStatic
    fun imagePattern(imageHandle: Int, x: Float, y: Float, width: Float, height: Float, angle: Float, alpha: Float) =
        renderer.imagePattern(imageHandle, x, y, width, height, angle, alpha)

    /**
     * Creates a linear gradient for the active path with the [x] and [y] as the
     * starting point and the [x2] and [y2] as the ending point.
     */
    @JvmStatic
    fun linearGradient(x: Float, y: Float, x2: Float, y2: Float, startColor: Int, endColor: Int) =
        renderer.linearGradient(x, y, x2, y2, startColor, endColor)

    /**
     * Creates a radial gradient for the active path.
     *
     * @param x The x-axis coordinate of the center of the circle.
     * @param y The y-axis coordinate of the center of the circle.
     */
    @JvmStatic
    fun radialGradient(x: Float, y: Float, innerRadius: Float, outerRadius: Float, startColor: Int, endColor: Int) =
        renderer.radialGradient(x, y, innerRadius, outerRadius, startColor, endColor)

    /**
     * Sets the current path to a hole path where every odd path fills as a hole. For example
     * if you have a rectangle and a circle is drawn within it right after the rectangle, the
     * circle will cut into the rectangle and there will be a hole where the circle was. The
     * next path will return to solid/stroke unless explicitly stated to be a hole path.
     *
     * The calls would look like:
     *
     *      path {
     *          rect(...)
     *          circle(...) // Somewhere within the rectangle call above
     *      }.fillPath() // Fill path or something
     *
     * @see <a href="https://github.com/memononen/nanovg#understanding-composite-paths">See NanoVG Composite paths</a>
     */
    @JvmStatic
    inline fun hole(block: Block<UIPathDSL>): UIPathDSL {
        UIPathDSL.block()
        renderer.pathHole(true)
        return this
    }
}