package net.prismclient.aether.ui.renderer.dsl

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.BUTT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.MIPMAP
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.MITER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATX
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATY
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL.render
import net.prismclient.aether.ui.renderer.dsl.UIRendererDSL.width
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.colorToString
import net.prismclient.aether.ui.util.extensions.toByteBuffer
import net.prismclient.aether.ui.util.extensions.toTerminatingByteBuffer
import org.apache.commons.io.FilenameUtils
import java.nio.ByteBuffer

/**
 * [UIRendererDSL] is a helper class which makes it easier to render things on screen. It supports
 * general geometric shapes, as well as image, and text rendering.
 *
 * @author sen
 * @since 1.0
 */
object UIRendererDSL {
    lateinit var renderer: UIRenderer

    private var color = 0
    private var alpha = 0

    private var fontFace = ""
    private var fontSize = 0f
    private var fontAlignment = 0
    private var fontSpacing = 0f

    private var stroke = false
    private var strokeWidth = 0f
    private var strokeDirection = StrokeDirection.OUTSIDE
    private var pos = 0f
    private var siz = 0f
    private var halfsw = 0f

    /** General **/
    fun beginFrame(width: Float, height: Float, pxRatio: Float) = renderer.beginFrame(width, height, pxRatio)

    fun endFrame() = renderer.endFrame()

    /** Color **/

    /**
     * Sets the color to the given RGBA int
     */
    fun color(rgba: Int) {
        color = rgba
        renderer.color(color)
    }

    /**
     * Sets the color to the given RGBA values with all ints except the alpha as a float
     */
    fun color(r: Int, g: Int, b: Int, a: Float = 1f) = color(asRGBA(r, g, b, a))

    /**
     * Sets the color to the given RGBA with all ints
     */
    @JvmOverloads
    fun color(r: Int, g: Int, b: Int, a: Int = 255) = color(asRGBA(r, g, b, a))

    @Suppress("EXTENSION_SHADOWED_BY_MEMBER")
    operator fun Int.unaryPlus() = color(this)

    /** Font **/
    /**
     * Sets the active font context to the provided [font]
     */
    fun font(font: UIFont) {
        color(font.fontColor)
        font(font.fontName, font.fontSize, font.textAlignment, font.fontSpacing)
    }

    /**
     * Sets the active font context to the provided values. To set the color use [color]
     */
    fun font(fontFace: String, fontSize: Float, fontAlignment: Int, fontSpacing: Float) {
        UIRendererDSL.fontFace = fontFace
        UIRendererDSL.fontSize = fontSize
        UIRendererDSL.fontAlignment = fontAlignment
        UIRendererDSL.fontSpacing = fontSpacing
        renderer.font(fontFace, fontSize, fontAlignment, fontSpacing)
    }

    /**
     * Renders a string at the given [x] and [y] position
     *
     * @see font
     */
    fun String.render(x: Float, y: Float) = renderer.renderString(this, x, y)

    /**
     * Renders the string within the given alignment based on the plot.
     */
    fun String.render(alignment: UIAlignment, x: Float, y: Float, width: Float, height: Float) {
        val alignedX: Float = when (alignment) {
            UIAlignment.TOPCENTER, UIAlignment.CENTER, UIAlignment.BOTTOMCENTER -> width / 2f
            UIAlignment.TOPRIGHT, UIAlignment.MIDDLERIGHT, UIAlignment.BOTTOMRIGHT -> width
            else -> 0f
        } + x
        val alignedY: Float = when (alignment) {
            UIAlignment.MIDDLELEFT, UIAlignment.CENTER, UIAlignment.MIDDLERIGHT -> height / 2f
            UIAlignment.BOTTOMLEFT, UIAlignment.BOTTOMCENTER, UIAlignment.BOTTOMRIGHT -> height
            else -> 0f
        } + y

        this.render(alignedX, alignedY)
    }

    /**
     * Renders a string with a line break when the length of the string exceeds the width cap
     */
    fun String.render(x: Float, y: Float, width: Float, splitHeight: Float) =
        renderer.wrapString(this, x, y, width, splitHeight)

    /**
     * Renders a string until the given width, where then the string is
     * truncated to the point, and the appended string is added. For example,
     * if the text is "Hello", the appended string is ".." and "Hel.." width
     * is greater than width, then the string rendered is "Hel..". If the appended
     * string is blank, or null, the string is cut off at the given width point,
     * like normal clipped text.
     *
     * @param ignoreLastSpace If true, the last space (if applicable) is omitted.
     * @return The width of the rendered string
     */
    @JvmOverloads
    fun String.render(
        x: Float,
        y: Float,
        width: Float,
        appendedString: String? = null,
        ignoreLastSpace: Boolean = true
    ): Float {
        if (width >= this.width()) {
            this.render(x, y)
            return this.width()
        }
        var new = ""
        var w = 0f
        val aw = appendedString?.width() ?: 0f
        for (c in 0 until this.length) {
            val char = this[c]
            w += char.toString().width()
            if (w >= width - aw) {
                if (new.isEmpty()) break
                val flag = (this.length != new.length)
                if (ignoreLastSpace && new[new.length - 1] == ' ') // Omit the space if applicable
                    new = new.substring(0, new.length - 1)
                if (flag && appendedString != null) // Append the appendedString if the length is less than the inital
                    new += appendedString
                break
            }
            new += char
        }
        new.render(x, y)
        return new.width()
    }

    /**
     * Returns the x position of the most recently rendered string with relations to (0, 0)
     */
    fun x(): Float = renderer.stringX()

    /**
     * Returns the y position of the most recently rendered string with relations to (0, 0)
     */
    fun y(): Float = renderer.stringY()

    /**
     * Returns the width of the most recently rendered string
     */
    fun width(): Float = renderer.stringWidth()

    /**
     * Returns the height of the most recently rendered string
     */
    fun height(): Float = renderer.stringHeight()

    /**
     * Returns the width of the given string based on the most recently set font
     */
    fun String.width(): Float {
        val previous = color
        renderer.color(0)
        (if (this[this.length - 1] == ' ') "$this " else this).render(0f, 0f)
        renderer.color(previous)
        return renderer.stringWidth()
    }

    /**
     * Returns the height of the given string based on the most recently set font
     */
    fun String.height(): Float {
        val previous = color
        renderer.color(0)
        this.render(0f, 0f)
        renderer.color(previous)
        return renderer.stringHeight()
    }

    /**
     * Returns the x offset of the given string base on the index
     */
    fun String.indexOffset(index: Int): Float {
        var w = 0f
        if (index > this.length - 1)
            return boundsOf(this)[4]
        for (i in 0 until index)
            w += boundsOf(this[i].toString())[4]
        return w
    }

    fun ascender() = renderer.stringAscender()

    fun descender() = renderer.stringDescender()

    fun bounds() = renderer.textBounds()

    fun boundsOf(text: String) = renderer.boundsOf(text)

    fun loadFont(name: String, fileLocation: String) = loadFont(name, fileLocation.toByteBuffer())

    fun loadFont(name: String, buffer: ByteBuffer) = renderer.loadFont(name, buffer)

    operator fun UIFont.unaryPlus() = font(this)

    /** Image **/
    @JvmOverloads
    fun renderImage(imageName: String, x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        renderImage(imageName, x, y, width, height, radius, radius, radius, radius)

    fun renderImage(
        imageName: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) = renderer.renderImage(imageName, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)

    // TODO: Maybe something like loadAll(type: String, fileLocation: String)
    // loadAll("svg", "/aether/svgs/")

    fun loadImage(name: String, fileLocation: String, flags: Int = MIPMAP or REPEATX or REPEATY) =
        loadImage(name, fileLocation.toByteBuffer(), flags)

    fun loadImage(name: String, buffer: ByteBuffer, flags: Int): UIImageData {
        val image = UIImageData()
        image.buffer = buffer
        return renderer.loadImage(name, image, flags)
    }

    @JvmOverloads
    fun loadSvg(name: String, fileLocation: String, scale: Float =UICore.devicePxRatio) =
        loadSvg(name, fileLocation.toTerminatingByteBuffer(), scale)

    fun loadSvg(name: String, buffer: ByteBuffer, scale: Float = UICore.devicePxRatio): UIImageData {
        val image = UIImageData()
        image.buffer = buffer
        return renderer.loadSVG(name, image, scale)
    }

    /** Asset Loading **/
    @JvmOverloads
    fun loadAsset(
        name: String,
        fileLocation: String,
        fileExtension: String = FilenameUtils.getExtension(fileLocation)
    ) {
        when (fileExtension) {
            "ttf" -> loadFont(name, fileLocation)
            "png", "jpeg" -> loadImage(name, fileLocation)
            "svg" -> loadSvg(name, fileLocation)
            else -> throw UnsupportedOperationException("Unknown file extension: $fileExtension")
        }
    }

    /**
     * Loads an image or svg from the provided file location.
     */
    fun assumeLoadImage(name: String, fileLocation: String): UIImageData =
        when (FilenameUtils.getExtension(fileLocation)) {
            "png", "jpeg" -> loadImage(name, fileLocation)
            "svg" -> loadSvg(name, fileLocation)
            else -> throw UnsupportedOperationException(
                "Unknown file extension: ${
                    FilenameUtils.getExtension(
                        fileLocation
                    )
                }"
            )
        }

    /** Shapes **/
    @JvmOverloads
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        rect(x, y, width, height, radius, radius, radius, radius)

    fun rect(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) = rect(
        x, y, width, height, radius?.topLeft
            ?: 0f, radius?.topRight ?: 0f, radius?.bottomRight ?: 0f, radius?.bottomRight ?: 0f
    )

    fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) = renderer.rect(
        x + pos,
        y + pos,
        width + siz,
        height + siz,
        topLeft + halfsw,
        topRight + halfsw,
        bottomRight + halfsw,
        bottomLeft + halfsw
    )

    /**
     * Must be placed inside a [line] block
     */
    fun line(x: Float, y: Float) = renderer.line(x, y)

    fun bezier(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) = renderer.bezier(x, y, x1, y1, x2, y2)

    fun ellipse(x: Float, y: Float, width: Float, height: Float) = renderer.ellipse(x, y, width - pos, height - pos)

    fun circle(x: Float, y: Float, radius: Float) = renderer.circle(x, y, radius - pos)

    fun triangle(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) =
        renderer.triangle(x, y, x1, y1, x2, y2)

    /** Transformations and DSL blocks **/
    fun save() = renderer.save()

    fun restore() = renderer.restore()

    fun translate(x: Float, y: Float) = renderer.translate(x, y)

    fun scale(x: Float, y: Float) = renderer.scale(x, y)

    fun rotate(angle: Float) = renderer.rotate(angle)

    fun rotate(degree: Double) = renderer.rotate(degree)

    fun scissor(x: Float, y: Float, width: Float, height: Float) = renderer.scissor(x, y, width, height)

    fun stroke(strokeWidth: Float, strokeDirection: StrokeDirection) {
        stroke = true
        UIRendererDSL.strokeWidth = strokeWidth
        UIRendererDSL.strokeDirection = strokeDirection
        halfsw = strokeWidth / 2f
        halfsw = if (halfsw < 0.1f) 0f else halfsw
        // Calculate the stroke direction
        pos = when (UIRendererDSL.strokeDirection) {
            StrokeDirection.INSIDE -> UIRendererDSL.strokeWidth / 2f
            StrokeDirection.OUTSIDE -> 0f - (UIRendererDSL.strokeWidth / 2f)
            StrokeDirection.CENTER -> 0f
        }
        siz = when (UIRendererDSL.strokeDirection) {
            StrokeDirection.INSIDE -> -UIRendererDSL.strokeWidth
            StrokeDirection.OUTSIDE -> UIRendererDSL.strokeWidth
            StrokeDirection.CENTER -> 0f
        }
    }

    fun finishStroke() {
        stroke = false
        pos = 0f
        siz = 0f
        strokeWidth = 0f
        halfsw = 0f
    }

    inline fun translate(x: Float, y: Float, block: UIRendererDSL.() -> Unit) {
        save()
        translate(x, y)
        this.block()
        restore()
    }

    inline fun scale(x: Float, y: Float, block: UIRendererDSL.() -> Unit) {
        save()
        scale(x, y)
        this.block()
        restore()
    }

    inline fun rotate(angle: Float, block: UIRendererDSL.() -> Unit) {
        save()
        rotate(angle)
        this.block()
        restore()
    }

    inline fun rotate(angle: Double, block: UIRendererDSL.() -> Unit) {
        save()
        rotate(angle)
        this.block()
        restore()
    }

    inline fun scissor(x: Float, y: Float, width: Float, height: Float, block: UIRendererDSL.() -> Unit) {
        save()
        scissor(x, y, width, height)
        this.block()
        restore()
    }

    inline fun stroke(
        strokeColor: Int,
        strokeWidth: Float,
        strokeDirection: StrokeDirection = StrokeDirection.OUTSIDE,
        block: UIRendererDSL.() -> Unit
    ) {
        renderer.stroke(strokeWidth, strokeColor)
        stroke(strokeWidth, strokeDirection)
        this.block()
        finishStroke()
        renderer.finishStroke()
    }

    inline fun renderContent(fbo: UIContentFBO, block: UIRendererDSL.() -> Unit) {
        renderer.bindContentFBO(fbo)
        renderer.beginFrame(fbo.scaledWidth, fbo.scaledHeight, fbo.contentScale)
        this.block()
        renderer.endFrame()
        renderer.unbindContentFBO()
    }

    /**
     * Used to start a line path
     *
     * @param lineCap Accepts BUTT, ROUND, SQUARE
     * @param lineJoin Accepts MITER, ROUND, BEVEL
     */
    inline fun line(x: Float, y: Float, lineCap: Int = BUTT, lineJoin: Int = MITER, lineWidth: Float = 1f, block: UIRendererDSL.() -> Unit) {
        renderer.startLine(x, y, lineCap, lineJoin, lineWidth)
        this.block()
        renderer.finishLine()
    }

    /**
     * Instructs the renderer how to render the stroke. When center, the stroke is
     * centered to the outline of the given shape. Outside, is the outside of the shape
     * , and inside is the inside of the shape.
     */
    enum class StrokeDirection {
        CENTER, OUTSIDE, INSIDE
    }
}