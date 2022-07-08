package net.prismclient.aether.ui.renderer

import net.prismclient.aether.ui.renderer.image.UIImageData
import java.nio.ByteBuffer
import javax.swing.GroupLayout.Alignment

/**
 * [UIRenderer] wraps all the render calls into this class. An implementation of this must
 * be providd to Aether within its constructor. The class is modeled after NanoVG, which is
 * "loosely" modeled on the HTML5 Canvas API.
 *
 * @author sen
 * @since 1.0
 */
interface UIRenderer {
    /** Frame **/

    /**
     * Begins the active frame with the given [width], and [height].
     */
    fun beginFrame(width: Float, height: Float, devicePxRatio: Float)

    /**
     * Ends the active frames and draws the frame to the screen.
     */
    fun endFrame()

    /**
     * Cancels the active frame and discards all rendering calls.
     */
    fun cancelFrame()

    /** State **/

    /**
     * Saves the current render state.
     *
     * @see restore
     */
    fun save()

    /**
     * Restores the most recently saved render state.
     *
     * @see save
     */
    fun restore()

    /**
     * Resets the state of the current render state.
     */
    fun reset()

    /**
     * Sets the active color to this [color].
     */
    fun color(color: Int)

    fun globalAlpha(alpha: Float)

    fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float)

    fun translate(x: Float, y: Float)

    fun scale(x: Float, y: Float)

    fun rotate(angle: Float)

    fun skewX(angle: Float)

    fun skewY(angle: Float)

    fun scissor(x: Float, y: Float, w: Float, h: Float)

    fun resetScissor()

    fun resetTransformation()

    /**
     * Enables/Disables anti-aliasing for supported calls
     */
    fun useAntialiasing(antialiasing: Boolean)

    /** Asset Loading **/
    fun createImage(imageData: UIImageData)

    fun deleteImage(imageData: UIImageData)

    fun createImageFromHandle(handle: Int)

    fun deleteImageFromHandle(handle: Int)

    /**
     * Creates a font from the given [fontData], with the name [fontName].
     *
     * @return Returns if the font was successfully created.
     */
    fun createFont(fontName: String, fontData: ByteBuffer): Boolean

    /** Image **/
    fun imagePattern(imageData: UIImageData, x: Float, y: Float, width: Float, height: Float, angle: Float, alpha: Float)


    /** Font **/

    /**
     * Sets the active font to the given [fontName].
     */
    fun fontFace(fontName: String)

    /**
     * Sets the font size to the given [size].
     */
    fun fontSize(size: Float)

    fun alignment(alignment: Int)

    /**
     * Spaces the text by the given [spacing].
     */
    fun fontSpacing(spacing: Float)

    /**
     * Returns an array of five floats representing the most recently rendered text.
     *
     * [0] = x
     * [1] = y
     * [2] = x + width
     * [3] = y + height
     * [4] = advance (the next glyph's x position)
     */
    fun fontBounds(): FloatArray

    /**
     * Returns the bounds of the given [text].
     *
     * @see fontBounds
     */
    fun fontBounds(text: String): FloatArray

    /**
     * Renders the [text] based on the [x], and [y] position and all the states
     *
     * @see fontSize
     * @see fontSpacing
     */
    fun renderText(text: String, x: Float, y: Float)


    /** General Shapes **/

    /**
     * Creates a rounded rectangle from the given [x], [y], [width], [height], with four corner radii points.
     */
    fun rect(x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float)

    /**
     * Creates an ellipse from the given [x], and [y].
     */
    fun ellipse(x: Float, y: Float, xRadius: Float, yRadius: Float)

    /**
     * Creates a circle from the given coordinates and [radius].
     */
    fun circle(x: Float, y: Float, radius: Float)

    /** Path **/

    /**
     * Begins a new path
     */
    fun beginPath()

    /**
     * Closes the active path
     */
    fun closePath()

    /**
     * Fills the active path
     */
    fun fill()

    /**
     * "Strokes" the active path
     */
    fun stroke()

    /**
     * Changes the path winding to the given [WindingOrder]
     */
    fun pathWinding(winding: WindingOrder)

    /**
     * Creates a new sub-path with the given [x], and [y].
     */
    fun moveTo(x: Float, y: Float)

    /**
     * Adds a new line segment to the active line.
     */
    fun lineTo(x: Float, y: Float)

    /**
     * Creates a cubic bezier segment from the last point to the [x], [y] point.
     *
     * @param cx The first control point x coordinate.
     * @param cy The first control point y coordinate.
     * @param cx2 The second control point x coordinate.
     * @param cy2 The second control point y coordinate.
     * @param x The point x-axis coordinate
     * @param y The point y-axis coordinate
     */
    fun bezierTo(cx: Float, cy: Float, cx2: Float, cy2: Float, x: Float, y: Float)

    /**
     * Adds a quadratic bezier segment from the last point to the [x], [y] point.
     *
     * @param cx The control point x coordinate.
     * @param cy The control point y coordinate.
     * @param x The point x-axis coordinate
     * @param y The point y-axis coordinate
     */
    fun quadTo(cx: Float, cy: Float, x: Float, y: Float)

    /**
     * Creates a new circle arc.
     *
     * @param x The first point x-axis coordinate.
     * @param y The first point y-axis coordinate.
     * @param radius The radius of the arc in radians.
     * @param startAngle The starting angle of the arc in radians.
     * @param endAngle The ending angle of the arc in radians.
     * @param windingOrder Whether the arc should be drawn in counter-clockwise direction.
     * @see degToRad
     * @see radToDeg
     */
    fun arc(x: Float, y: Float, radius: Float, angle: Float, startAngle: Float, endAngle: Float, windingOrder: WindingOrder)

    /**
     * @param x The first point x-axis coordinate.
     * @param y The first point y-axis coordinate.
     * @param x1 The second point x-axis coordinate.
     * @param y1 The second point y-axis coordinate.
     * @param radius The radius of the arc in radians.
     * @see degToRad
     * @see radToDeg
     */
    fun arcTo(x: Float, y: Float, x1: Float, y1: Float, radius: Float)

    /**
     * Sets the stroke width to the given [size].
     */
    fun strokeWidth(size: Float)

    /**
     * Sets the line cap to the given [cap].
     *
     * @see LineCap
     */
    fun lineCap(cap: LineCap)

    /**
     * Sets the line join to the given [join].
     *
     * @see LineJoin
     */
    fun lineJoin(join: LineJoin)

    /** Gradients **/

    /** Util **/

    /**
     * Converts degrees to radians.
     */
    fun degToRad(deg: Float): Float

    /**
     * Converts radians to degrees.
     */
    fun radToDeg(rad: Float): Float

    /**
     * [WindingOrder] describes the winding direction of the path.
     *
     * @author sen
     * @since 1.0
     */
    enum class WindingOrder {
        CW,
        CCW
    }

    /**
     * [LineCap] describes how the start and end of stroke shapes are drawn.
     *
     * @author sen
     * @since 1.0
     * @see [Line cap visualization](https://www.w3.org/TR/svg-strokes/images/linecap.svg)
     */
    enum class LineCap {
        Butt,
        Round,
        Square
    }

    /**
     * [LineJoin] describes are sharp turns in a stroke are drawn
     *
     * @author sen
     * @since 1.0
     * @see [Line join visualization](https://anzeljg.github.io/rin2/book2/2405/docs/tkinter/img/cap-join.png)
     */
    enum class LineJoin {
        Miter,
        Round,
        Bevel
    }
}