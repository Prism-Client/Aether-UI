package net.prismclient.aether.ui.renderer

import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.impl.font.UITextAlignment
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import java.nio.ByteBuffer

/**
 * [UIRenderer] wraps all the necessary render calls into this class. An implementation of this must
 * be provided to Aether within its constructor. The class is modeled after NanoVG, which is
 * "loosely" modeled on the HTML5 Canvas API. Depending on how the renderer that is used is designed,
 * certain methods might be blank calls as they might not need to inform the renderer.
 *
 * @author sen
 * @since 1.0
 */
interface UIRenderer {
    // -- Frame -- //

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

    // -- State -- //

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
     * Sets the active fill color to this [color]. Alternative to [fillColor].
     *
     * @see fillColor
     */
    fun color(color: Int)

    /**
     * Sets the alpha of the current path to [alpha].
     */
    fun globalAlpha(alpha: Float)

    /**
     * Pre multiplies the current coordinate system by the given matrix. The matrix is as:
     *
     *      [a, c, e]
     *      [b, d, f]
     *      [0, 0, 1]
     */
    fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float)

    /**
     * Translates the current coordinate system by the given [x] and [y] values.
     */
    fun translate(x: Float, y: Float)

    /**
     * Scales the current coordinate system by the given [x] and [y] scale factors.
     */
    fun scale(x: Float, y: Float)

    /**
     * Rotates the current coordinate system by the given [angle] in radians.
     */
    fun rotate(angle: Float)

    fun skewX(angle: Float)

    fun skewY(angle: Float)

    /**
     * Creates a box where anything outside the box is clipped (scissored).
     */
    fun scissor(x: Float, y: Float, w: Float, h: Float)

    /**
     * Resets scissors of the current state.
     */
    fun resetScissor()

    /**
     * Resets the transformations of the current coordinate system.
     */
    fun resetTransformation()

    /**
     * Enables/Disables anti-aliasing for supported calls
     */
    fun useAntialiasing(antialiasing: Boolean)

    // -- FBO --//

    /**
     * Creates a framebuffer from the given [width] and [height].
     */
    fun createFBO(width: Float, height: Float): UIContentFBO

    /**
     * Deletes the framebuffer from memory.
     */
    fun deleteFBO(fbo: UIContentFBO)

    /**
     * Begins a new draw frame and binds the given fbo to render content to.
     *
     * @see unbindFBO
     */
    fun bindFBO(fbo: UIContentFBO)

    /**
     * Unbinds the active framebuffer.
     *
     * @see bindFBO
     */
    fun unbindFBO()

    // -- Asset Loading --/
    /**
     * Creates an image from the given [data] registered to the [imageName].
     *
     * @return The created image.
     */
    fun createImage(imageName: String, data: ByteBuffer, flags: Int): UIImageData

    /**
     * Deallocates the given image from memory.
     */
    fun deleteImage(imageData: String)

    /**
     * Creates a svg from the given [svgName] and [data].
     *
     * @return The created image (svg).
     */
    fun createSvg(svgName: String, data: ByteBuffer?, scale: Float): UIImageData

    /**
     * Creates an image from an existing OpenGL image.
     */
    fun createImageFromHandle(imageName: String, handle: Int, imageWidth: Int, imageHeight: Int): UIImageData

    /**
     * Creates a font from the given [fontData], with the name [fontName].
     *
     * @return Returns if the font was successfully created.
     */
    fun createFont(fontName: String, fontData: ByteBuffer?): Boolean

    /** Image **/
    fun imagePattern(imageHandle: Int, x: Float, y: Float, width: Float, height: Float, angle: Float, alpha: Float)

    // -- Font -- //

    /**
     * Sets the active font to the given [fontName].
     */
    fun fontFace(fontName: String)

    /**
     * Sets the font size to the given [size].
     */
    fun fontSize(size: Float)

    /**
     * Spaces the text by the given [spacing].
     */
    fun fontSpacing(spacing: Float)

    /**
     * Aligns the text to the given [horizontalAlignment] and [verticalAlignment] based on the enum [UITextAlignment].
     */
    fun fontAlignment(horizontalAlignment: UITextAlignment, verticalAlignment: UITextAlignment)

    /**
     * Renders the [text] based on the [x], and [y] position and all the states
     *
     * @see fontSize
     * @see fontSpacing
     */
    fun renderText(text: String, x: Float, y: Float)

    /**
     * Renders the given [text] splitting each line by the given [lineHeight].
     */
    fun renderText(text: ArrayList<String>, x: Float, y: Float, lineHeight: Float)

    /**
     * Renders the given [text]; however, if the text exceeds [lineWidth], the text will be put onto a newline
     * with the spacing of [lineHeight]. If [lines] is not null, the string will be added to it.
     *
     * @return The row count
     */
    fun renderText(text: String, x: Float, y: Float, lineWidth: Float, lineHeight: Float, lines: ArrayList<String>?): Int

    /**
     * Calculates the bounds given the array of [text] splitting it by the [lineHeight].
     */
    fun calculateText(text: ArrayList<String>, x: Float, y: Float, lineHeight: Float)

    /**
     * Calculates the given [text] where if the text exceeds the [lineWidth], it will
     * be put onto a new line. If [lines] is not null, the string will be added to it.
     */
    fun calculateText(text: String, x: Float, y: Float, lineWidth: Float, lineHeight: Float, lines: ArrayList<String>?): Int

    /**
     * Returns an array of five floats representing the most recently rendered text.
     *
     *      [0] = x
     *      [1] = y
     *      [2] = x + width
     *      [3] = y + height
     *      [4] = advance (the next glyph's x position)
     */
    fun fontBounds(): FloatArray

    /**
     * Returns the bounds of the given [text] at (0, 0). Overrides the most recent draw
     * call, so [fontAscender] and [fontDescender] will return the value of this instead of the
     * most recent draw call.
     *
     * @see fontBounds
     */
    fun fontBounds(text: String): FloatArray

    /**
     * Returns the ascender of the most recently font draw call.
     */
    fun fontAscender(): Float

    /**
     * Returns the descender of the most recently font draw call.
     */
    fun fontDescender(): Float

    // -- General Shapes -- //

    /**
     * Creates a rounded rectangle from the given [x], [y], [width], [height], with four corner radii points.
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
    )

    /**
     * Creates an ellipse from the given [x], and [y].
     */
    fun ellipse(x: Float, y: Float, xRadius: Float, yRadius: Float)

    /**
     * Creates a circle from the given coordinates and [radius].
     */
    fun circle(x: Float, y: Float, radius: Float)

    // -- Path -- //

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
     * Fills the active path with the active paint.
     */
    fun fillPaint()

    /**
     * Sets the fill color to the given value
     *
     * @see color
     */
    fun fillColor(color: Int)

    /**
     * "Strokes" the active path
     */
    fun stroke()

    /**
     * "Strokes" the active path with the active paint.
     */
    fun strokePaint()

    /**
     * Sets the stroke width to the given [size].
     */
    fun strokeWidth(size: Float)

    /**
     * Sets the stroke color to the given [color]. The color does not affect the fill color.
     *
     * @see color
     * @see fillColor
     */
    fun strokeColor(color: Int)

    /**
     * When enabled the odd sub-path fills as a hole to the previous path. For example, if
     * a rectangle is drawn, and a circle is drawn within it after, the circle will make a
     * hole within the rectangle where it was.
     */
    fun pathHole(value: Boolean)

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
    fun arc(x: Float, y: Float, radius: Float, startAngle: Float, endAngle: Float, windingOrder: WindingOrder)

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

    // -- Gradient -- //

    /**
     * Creates a linear gradient for the active path with the [x] and [y] as the
     * starting point and the [x2] and [y2] as the ending point.
     */
    fun linearGradient(x: Float, y: Float, x2: Float, y2: Float, startColor: Int, endColor: Int)

    /**
     * Creates a radial gradient for the active path.
     *
     * @param x The x-axis coordinate of the center of the circle.
     * @param y The y-axis coordinate of the center of the circle.
     */
    fun radialGradient(x: Float, y: Float, innerRadius: Float, outerRadius: Float, startColor: Int, endColor: Int)

    // -- Paint -- //

    /**
     * Creates a new paint.
     */
    fun allocPaint()

    /**
     * Removes the active paint allocated from a gradient or image
     */
    fun deallocatePaint()

    // -- Util -- //

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
     * [LineCap] describes how the start and end of stroke shapes are drawn. [Line cap visualization](https://www.w3.org/TR/svg-strokes/images/linecap.svg).
     *
     * @author sen
     * @since 1.0
     */
    enum class LineCap {
        Butt,
        Round,
        Square
    }

    /**
     * [LineJoin] describes are sharp turns in a stroke are drawn. [Line join visualization](https://anzeljg.github.io/rin2/book2/2405/docs/tkinter/img/cap-join.png).
     *
     * @author sen
     * @since 1.0
     */
    enum class LineJoin {
        Miter,
        Round,
        Bevel
    }
}