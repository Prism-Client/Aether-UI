package net.prismclient.aether.ui.renderer.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.BUTT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.MIPMAP
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.MITER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATX
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATY
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.impl.border.UIStrokeDirection
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.toByteBuffer
import net.prismclient.aether.ui.util.extensions.toTerminatingByteBuffer
import net.prismclient.aether.ui.util.warn
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
    @JvmStatic
    lateinit var render: UIRenderer

    @JvmStatic
    private var color = 0

    @JvmStatic
    private var alpha = 0

    @JvmStatic
    private var fontFace = ""

    @JvmStatic
    private var fontSize = 0f

    @JvmStatic
    private var fontAlignment = 0

    @JvmStatic
    private var fontSpacing = 0f

    private var stroke = false

    @JvmStatic
    private var strokeWidth = 0f

    @JvmStatic
    private var strokeDirection = UIStrokeDirection.OUTSIDE

    @JvmStatic
    private var pos = 0f

    @JvmStatic
    private var siz = 0f

    @JvmStatic
    private var halfsw = 0f

    /** General **/
    @JvmStatic
    fun beginFrame(width: Float, height: Float, pxRatio: Float) = render.beginFrame(width, height, pxRatio)

    @JvmStatic
    fun endFrame() = render.endFrame()

    /** Color **/

    /**
     * Sets the color to the given RGBA int
     */
    @JvmStatic
    fun color(rgba: Int) {
        color = rgba
        render.color(color)
    }

    /**
     * Sets the color to the given RGBA values with all ints except the alpha as a float
     */
    @JvmStatic
    fun color(r: Int, g: Int, b: Int, a: Float = 1f) = color(asRGBA(r, g, b, a))

    /**
     * Sets the color to the given RGBA with all ints
     */
    @JvmOverloads
    @JvmStatic
    fun color(r: Int, g: Int, b: Int, a: Int = 255) = color(asRGBA(r, g, b, a))

    @Suppress("EXTENSION_SHADOWED_BY_MEMBER")
    operator fun Int.unaryPlus() = color(this)

    /** Font **/
    /**
     * Sets the active font context to the provided [font]
     */
    @JvmStatic
    fun font(font: UIFont) {
        color(font.fontColor)
        font(font.fontName, font.fontSize, font.textAlignment, font.fontSpacing)
    }

    /**
     * Sets the active font context to the provided values. To set the color use [color]
     */
    @JvmStatic
    fun font(fontFace: String, fontSize: Float, fontAlignment: Int, fontSpacing: Float) {
        UIRendererDSL.fontFace = fontFace
        UIRendererDSL.fontSize = fontSize
        UIRendererDSL.fontAlignment = fontAlignment
        UIRendererDSL.fontSpacing = fontSpacing
        render.font(fontFace, fontSize, fontAlignment, fontSpacing)
    }

    /**
     * Renders a string at the given [x] and [y] position
     *
     * @see font
     */
    @JvmStatic
    fun String.render(x: Float, y: Float) = render.renderString(this, x, y)

    /**
     * Renders the string within the given alignment based on the plot.
     */
    @JvmStatic
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
    @JvmStatic
    fun String.render(x: Float, y: Float, width: Float, splitHeight: Float) =
        render.wrapString(this, x, y, width, splitHeight, null)

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
    @JvmStatic
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
    @JvmStatic
    fun x(): Float = render.stringX()

    /**
     * Returns the y position of the most recently rendered string with relations to (0, 0)
     */
    @JvmStatic
    fun y(): Float = render.stringY()

    /**
     * Returns the width of the most recently rendered string
     */
    @JvmStatic
    fun width(): Float = render.stringWidth()

    /**
     * Returns the height of the most recently rendered string
     */
    @JvmStatic
    fun height(): Float = render.stringHeight()

    /**
     * Returns the width of the given string based on the most recently set font
     */
    @JvmStatic
    fun String.width(): Float {
        val previous = color
        render.color(0)
        (if (this[this.length - 1] == ' ') "$this " else this).render(0f, 0f)
        render.color(previous)
        return render.stringWidth()
    }

    /**
     * Returns the height of the given string based on the most recently set font
     */
    @JvmStatic
    fun String.height(): Float {
        val previous = color
        render.color(0)
        this.render(0f, 0f)
        render.color(previous)
        return render.stringHeight()
    }

    /**
     * Returns the x offset of the given string base on the index
     */
    @JvmStatic
    fun String.indexOffset(index: Int): Float {
        var w = 0f
        if (index > this.length - 1) return boundsOf(this)[4] + boundsOf(this)[0]
        for (i in 0 until index) w += boundsOf(this[i].toString())[4]
        return w + boundsOf(this)[0]
    }

    @JvmStatic
    fun ascender() = render.stringAscender()

    @JvmStatic
    fun descender() = render.stringDescender()

    @JvmStatic
    fun bounds() = render.textBounds()

    @JvmStatic
    fun boundsOf(text: String) = render.boundsOf(text)

    @JvmStatic
    fun loadFont(name: String, fileLocation: String) = loadFont(name, fileLocation.toByteBuffer())

    @JvmStatic
    fun loadFont(name: String, buffer: ByteBuffer) = render.loadFont(name, buffer)

    @JvmStatic
    operator fun UIFont.unaryPlus() = font(this)

    /** Image **/
    @JvmStatic
    @JvmOverloads
    fun renderImage(imageName: String, x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        renderImage(imageName, x, y, width, height, radius, radius, radius, radius)

    @JvmStatic
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
    ) = render.renderImage(imageName, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)

    // TODO: Maybe something like loadAll(type: String, fileLocation: String)
    // loadAll("svg", "/aether/svgs/")

    @JvmStatic
    fun loadImage(name: String, fileLocation: String, flags: Int = MIPMAP or REPEATX or REPEATY) =
        loadImage(name, fileLocation.toByteBuffer(), flags)

    @JvmStatic
    fun loadImage(name: String, buffer: ByteBuffer, flags: Int): UIImageData {
        val image = UIImageData()
        image.buffer = buffer
        return render.loadImage(name, image, flags)
    }

    @JvmStatic
    @JvmOverloads
    fun loadSvg(name: String, fileLocation: String, scale: Float = Aether.devicePxRatio) =
        loadSvg(name, fileLocation.toTerminatingByteBuffer(), scale).also {
            if (FilenameUtils.getExtension(fileLocation) != "svg") warn("SVG file extension is not .svg")
        }

    @JvmStatic
    fun loadSvg(name: String, buffer: ByteBuffer, scale: Float = Aether.devicePxRatio): UIImageData {
        val image = UIImageData()
        image.buffer = buffer
        return render.loadSVG(name, image, scale)
    }

    /** Asset Loading **/
    @JvmStatic
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
    @JvmStatic
    fun assumeLoadImage(name: String, fileLocation: String): UIImageData =
        when (FilenameUtils.getExtension(fileLocation)) {
            "png", "jpeg", "jpg" -> loadImage(name, fileLocation)
            "svg" -> loadSvg(name, fileLocation)
            else -> throw UnsupportedOperationException(
                "Unknown file extension: ${
                    FilenameUtils.getExtension(fileLocation)
                }"
            )
        }

    /** Shapes **/
    @JvmStatic
    @JvmOverloads
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        rect(x, y, width, height, radius, radius, radius, radius)

    @JvmStatic
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) = rect(
        x,
        y,
        width,
        height,
        radius?.topLeft ?: 0f,
        radius?.topRight ?: 0f,
        radius?.bottomRight ?: 0f,
        radius?.bottomRight ?: 0f
    )

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
    ) = render.rect(
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
    @JvmStatic
    fun line(x: Float, y: Float) = render.line(x, y)

    @JvmStatic
    fun bezier(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) = render.bezier(x, y, x1, y1, x2, y2)

    @JvmStatic
    fun ellipse(x: Float, y: Float, width: Float, height: Float) = render.ellipse(x, y, width - pos, height - pos)

    @JvmStatic
    fun circle(x: Float, y: Float, radius: Float) = render.circle(x, y, radius - pos)

    @JvmStatic
    fun triangle(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) = render.triangle(x, y, x1, y1, x2, y2)

    /** Transformations and DSL blocks **/
    @JvmStatic
    fun save() = render.save()

    @JvmStatic
    fun restore() = render.restore()

    @JvmStatic
    fun translate(x: Float, y: Float) = render.translate(x, y)

    @JvmStatic
    fun scale(x: Float, y: Float) = render.scale(x, y)

    @JvmStatic
    fun rotate(angle: Float) = render.rotate(angle)

    @JvmStatic
    fun rotate(degree: Double) = render.rotate(degree)

    @JvmStatic
    fun scissor(x: Float, y: Float, width: Float, height: Float) = render.scissor(x, y, width, height)

    @JvmStatic
    fun renderFBO(fbo: UIContentFBO, x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) {
        render.renderFBO(
            fbo,
            x,
            y,
            width,
            height,
            radius?.topLeft ?: 0f,
            radius?.topRight ?: 0f,
            radius?.bottomRight ?: 0f,
            radius?.bottomLeft ?: 0f
        )
    }

    @JvmStatic
    fun stroke(strokeWidth: Float, strokeDirection: UIStrokeDirection) {
        stroke = true
        UIRendererDSL.strokeWidth = strokeWidth
        UIRendererDSL.strokeDirection = strokeDirection
        halfsw = strokeWidth / 2f
        halfsw = if (halfsw < 0.1f) 0f else halfsw
        // Calculate the stroke direction
        pos = when (UIRendererDSL.strokeDirection) {
            UIStrokeDirection.INSIDE -> UIRendererDSL.strokeWidth / 2f
            UIStrokeDirection.OUTSIDE -> 0f - (UIRendererDSL.strokeWidth / 2f)
            UIStrokeDirection.CENTER -> 0f
        }
        siz = when (UIRendererDSL.strokeDirection) {
            UIStrokeDirection.INSIDE -> -UIRendererDSL.strokeWidth
            UIStrokeDirection.OUTSIDE -> UIRendererDSL.strokeWidth
            UIStrokeDirection.CENTER -> 0f
        }
    }

    @JvmStatic
    fun finishStroke() {
        stroke = false
        pos = 0f
        siz = 0f
        strokeWidth = 0f
        halfsw = 0f
    }

    @JvmStatic
    inline fun translate(x: Float, y: Float, block: UIRendererDSL.() -> Unit) {
        save()
        translate(x, y)
        this.block()
        restore()
    }

    @JvmStatic
    inline fun scale(x: Float, y: Float, block: UIRendererDSL.() -> Unit) {
        save()
        scale(x, y)
        this.block()
        restore()
    }

    @JvmStatic
    inline fun rotate(angle: Float, block: UIRendererDSL.() -> Unit) {
        save()
        rotate(angle)
        this.block()
        restore()
    }

    @JvmStatic
    inline fun rotate(angle: Double, block: UIRendererDSL.() -> Unit) {
        save()
        rotate(angle)
        this.block()
        restore()
    }

    @JvmStatic
    inline fun scissor(x: Float, y: Float, width: Float, height: Float, block: UIRendererDSL.() -> Unit) {
        save()
        scissor(x, y, width, height)
        this.block()
        restore()
    }

    @JvmStatic
    inline fun stroke(
        strokeColor: Int,
        strokeWidth: Float,
        strokeDirection: UIStrokeDirection = UIStrokeDirection.OUTSIDE,
        block: UIRendererDSL.() -> Unit
    ) {
        render.stroke(strokeWidth, strokeColor)
        stroke(strokeWidth, strokeDirection)
        this.block()
        finishStroke()
        render.finishStroke()
    }

    @JvmStatic
    inline fun renderContent(fbo: UIContentFBO, block: UIRendererDSL.() -> Unit) {
        render.bindContentFBO(fbo)
        render.beginFrame(fbo.scaledWidth, fbo.scaledHeight, fbo.contentScale)
        this.block()
        render.endFrame()
        render.unbindContentFBO()
    }

    /**
     * Used to start a line path
     *
     * @param lineCap Accepts BUTT, ROUND, SQUARE
     * @param lineJoin Accepts MITER, ROUND, BEVEL
     */
    @JvmStatic
    inline fun line(
        x: Float,
        y: Float,
        lineCap: Int = BUTT,
        lineJoin: Int = MITER,
        lineWidth: Float = 1f,
        block: UIRendererDSL.() -> Unit
    ) {
        render.startLine(x, y, lineCap, lineJoin, lineWidth)
        this.block()
        render.finishLine()
    }
}