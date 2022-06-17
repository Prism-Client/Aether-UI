package net.prismclient.aether.ui.renderer

import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.util.extensions.getAlpha
import net.prismclient.aether.ui.util.extensions.getBlue
import net.prismclient.aether.ui.util.extensions.getGreen
import net.prismclient.aether.ui.util.extensions.getRed
import java.nio.ByteBuffer

/**
 * [UIRenderer] handles all calls for rendering graphics on screen. An
 * instance of this class must be created on initialization. You can
 * find the default NanoVG implementation in the test package.
 *
 * @author sen
 * @since 4/23/2022
 */
@Suppress("SpellCheckingInspection")
abstract class UIRenderer {
    @JvmField
    protected var activeColor: Int = 0

    @JvmField
    protected var fontName: String = ""

    @JvmField
    protected var fontSize: Float = 0f

    @JvmField
    protected var fontAlignment: Int = 0

    @JvmField
    protected var fontSpacing: Float = 0f

    @JvmField
    protected var stroke = false

    @JvmField
    protected var strokeWidth: Float = 0f

    @JvmField
    protected var strokeColor: Int = 0

    @JvmField var lineX = 0f
    @JvmField var lineY = 0f

    @JvmField
    protected var lineCap: Int = BUTT

    @JvmField
    protected var lineJoin: Int = MITER

    companion object Properties {
        /* Font */
        const val ALIGNLEFT = 1
        const val ALIGNCENTER = 2
        const val ALIGNRIGHT = 4
        const val ALIGNTOP = 8
        const val ALIGNMIDDLE = 16
        const val ALIGNBOTTOM = 32
        const val ALIGNBASELINE = 64

        /* Images */
        const val MIPMAP = 1
        const val REPEATX = 2
        const val REPEATY = 4
        const val FLIPY = 8
        const val PREMULTIPLIED = 16
        const val NEAREST = 32

        /* Line Cap/Join */
        const val BUTT = 0
        const val ROUND = 1
        const val SQUARE = 2
        const val BEVEL = 3
        const val MITER = 4
    }

    /**
     * Called when the frame is started
     */
    abstract fun beginFrame(width: Float, height: Float, devicePixelRatio: Float)

    /**
     * Called when the frame ends
     */
    abstract fun endFrame()

    /**
     * Saves the active coordinate system (matrix stack) which holds the position,
     * otation, etc... This function should be able to support multiple save functions.
     *
     * @see restore
     */
    abstract fun save()

    /**
     * Restores the previous coordinate system
     *
     * @see save
     */
    abstract fun restore()

    /**
     * Sets the color provided to the active color
     *
     * @param color A RGBA color
     */
    open fun color(color: Int) {
        this.activeColor = color
    }

    /**
     * Creates a new frame buffer with the provided [width] and [height]
     *
     * @param width The width of the framebuffer
     * @param height The height of the framebuffer
     * @return An instance of UIContentFBO, which holds the id, width and height
     * @see deleteContentFBO
     * @see bindContentFBO
     * @see unbindContentFBO
     */
    abstract fun createContentFBO(width: Float, height: Float): UIContentFBO

    /**
     * Deletes the frame buffer with the provided [fbo]
     *
     * @see createContentFBO
     * @see bindContentFBO
     * @see unbindContentFBO
     */
    abstract fun deleteContentFBO(fbo: UIContentFBO)

    /**
     * Sets the current FBO to the provided [fbo]
     *
     * @param fbo The [UIContentFBO] created from [createContentFBO]
     * @see createContentFBO
     * @see deleteContentFBO
     * @see bindContentFBO
     */
    abstract fun bindContentFBO(fbo: UIContentFBO)

    /**
     * Unbinds the current frame buffer
     *
     * @see createContentFBO
     * @see deleteContentFBO
     * @see bindContentFBO
     */
    abstract fun unbindContentFBO()

    /**
     * TODO
     */
    abstract fun renderFBO(
        fbo: UIContentFBO,
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
     * Sets the active font properties to the provided values
     *
     * @param fontName The name of the font
     * @param fontSize The size of the font
     * @param fontAlignment The alignment of the font
     * @param fontSpacing The spacing between each character in the text
     * @see Properties Font section on font alignment
     */
    open fun font(fontName: String, fontSize: Float, fontAlignment: Int, fontSpacing: Float) {
        this.fontName = fontName
        this.fontSize = fontSize
        this.fontAlignment = fontAlignment
        this.fontSpacing = fontSpacing
    }

    /**
     * Sets the active stroke properties to the provided value
     */
    open fun stroke(strokeWidth: Float, strokeColor: Int) {
        this.strokeWidth = strokeWidth
        this.strokeColor = strokeColor
        stroke = true
    }

    open fun finishStroke() {
        strokeWidth = 0f
        strokeColor = 0
        stroke = false
    }

    /**
     * Translates the current coordinate system
     *
     * @param x The amount to move on the x-axis
     * @param y The amount to move on the y-axis
     */
    abstract fun translate(x: Float, y: Float)

    /**
     * Rotates the current coordinate system
     *
     * @param angle The amount to rotate (in radians)
     */
    abstract fun rotate(angle: Float)

    /**
     * Rotates the current coordinate system
     *
     * @param angle The amount to rotate (in degrees)
     */
    abstract fun rotate(angle: Double)

    /**
     * Scales the current coordinate system
     *
     * @param x The amount to scale on the x-axis
     * @param y The amount to scale on the y-axis
     */
    abstract fun scale(x: Float, y: Float)

    /**
     * Scissors the verticiesnano
     *
     * @param x The x position of the rectangle scissor
     * @param y The y position of the rectangle scissor
     * @param width The width of the rectangle scissor
     * @param height The height of the rectangle scissor
     */
    abstract fun scissor(x: Float, y: Float, width: Float, height: Float)

    /**
     * Renders a rounded rectangle with varying corner radii
     *
     * @param x The x position of the rectangle
     * @param y The y position of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param topLeft The top left corner radius of the rectangle
     * @param topRight The top right corner radius of the rectangle
     * @param bottomRight The bottom right corner radius of the rectangle
     * @param bottomLeft The bottom left corner radius of the rectangle
     */
    abstract fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    )

    abstract fun circle(x: Float, y: Float, radius: Float)

    /**
     * Renders an ellipse
     *
     * @param x The x position of the ellipse
     * @param y The y position of the ellipse
     * @param width The width of the ellipse
     * @param height The height of the ellipse
     */
    abstract fun ellipse(x: Float, y: Float, width: Float, height: Float)

    abstract fun triangle(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float)

    /** Line **/
    abstract fun startLine(x: Float, y: Float, lineCap: Int, lineJoin: Int, lineWidth: Float)

    abstract fun line(x: Float, y: Float)

    abstract fun bezier(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float)

    abstract fun finishLine()

    /**
     * Renders a rounded rectangle linear gradient
     *
     * @param x The x position of the rectangle
     * @param y The y position of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param radius The radius of the rectangle's corners
     * @param gradientX The x position of the gradient
     * @param gradientY The y position of the gradient
     * @param gradientWidth The width of the gradient
     * @param gradientHeight The height of the gradient
     * @param color1 The first color of the gradient
     * @param color2 The second color of the gradient
     */
    abstract fun linearGradient(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        radius: Float,
        gradientX: Float,
        gradientY: Float,
        gradientWidth: Float,
        gradientHeight: Float,
        color1: Int,
        color2: Int
    )

    /**
     * Renders a rounded rectangle linear gradient with varying corner radii
     *
     * @param x The x position of the rectangle
     * @param y The y position of the rectangle
     * @param width The width of the rectangle
     * @param height The height of the rectangle
     * @param topLeft The top left corner radius of the rectangle
     * @param topRight The top right corner radius of the rectangle
     * @param bottomRight The bottom right corner radius of the rectangle
     * @param bottomLeft The bottom left corner radius of the rectangle
     * @param gradientX The x position of the gradient
     * @param gradientY The y position of the gradient
     * @param gradientWidth The width of the gradient
     * @param gradientHeight The height of the gradient
     * @param color1 The first color of the gradient
     * @param color2 The second color of the gradient
     */
    abstract fun linearGradient(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float,
        gradientX: Float,
        gradientY: Float,
        gradientWidth: Float,
        gradientHeight: Float,
        color1: Int,
        color2: Int
    )

    /**
     * Loads an image from an existing UILoadableImage
     *
     * @param imageName The unique name of the imageLocation
     * @return Returns if the image was sucessfully loaded or not
     */
    abstract fun loadImage(imageName: String, image: UIImageData, flags: Int): UIImageData

    /**
     * Accepts a svg from a [ByteBuffer] and rasterizes it as an image and places
     * the rasterized image into the image list with the given [svgName]
     *
     * @param scale The size to rasterize the svg to
     */
    abstract fun loadSVG(svgName: String, image: UIImageData, scale: Float): UIImageData

    /**
     * Deletes the image associated with [imageName] from memory
     */
    abstract fun deleteImage(imageName: String)

    /**
     * Renders an image based on the provided [imageName]
     *
     * @param imageName The name of the image, which was loaded with the param [imageName] in the loadImage function
     * @param x
     * @param y
     * @param width
     * @param height
     * @param radius
     */
    abstract fun renderImage(
        imageName: String,
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
     * Loads a font from the provided file location into the provided [fontName]
     *
     * @param fontName The unique name of the font
     * @param fileLocation The relative file location of the font
     * @return Returns if the font was sucessfully loaded or not
     */
    abstract fun loadFont(fontName: String, fontData: ByteBuffer?): Boolean

    /**
     * Renders a string
     *
     * @param text The string to render
     * @param x The x position of the string
     * @param y The y position of the string
     */
    abstract fun renderString(text: String, x: Float, y: Float)

    /**
     * Renders a multiline string, where it wraps when the width of the string passes [width]
     *
     * @param text The string to render
     * @param x The x position of the string
     * @param y The y position of the string
     * @param width The width to wrap the text at
     * @param splitHeight The spacing between the individual lines
     */
    abstract fun wrapString(text: String, x: Float, y: Float, width: Float, splitHeight: Float): Int

    /**
     * Returns the x position of the most recently rendered string with text alignment calculated
     */
    abstract fun stringX(): Float

    /**
     * Returns the y position of the most recently rendered string with text alignment calculated
     */
    abstract fun stringY(): Float

    /**
     * Returns the width of the most recently rendered string
     */
    abstract fun stringWidth(): Float

    /**
     * Returns the height of the most recently rendered string
     */
    abstract fun stringHeight(): Float

    /**
     * Returns the Ascender of the provided string
     */
    abstract fun stringAscender(): Float

    /**
     * Returns the Descender of the provided string
     */
    abstract fun stringDescender(): Float

    /**
     * Retrieves the bounds of the most recently rendered string. Returns an
     * array of 4 with the x, y, x + w, and y + h representing the text bounds.
     */
    abstract fun textBounds(): FloatArray

    abstract fun boundsOf(text: String): FloatArray

    abstract fun test()
}