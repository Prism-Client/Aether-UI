package net.prismclient.aether.ui.renderer

import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.MIPMAP
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATX
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATY
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.toByteBuffer
import net.prismclient.aether.ui.util.extensions.toTerminatingByteBuffer
import org.apache.commons.io.FilenameUtils
import java.nio.ByteBuffer

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
    fun beginFrame(width: Float, height: Float, pxRatio: Float) =
        renderer.beginFrame(width, height, pxRatio)

    fun endFrame() = renderer.endFrame()

    /** Color **/
    fun color(rgba: Int) {
        color = rgba
        renderer.color(color)
    }

    fun color(r: Int, g: Int, b: Int, a: Float = 1f) = color(asRGBA(r, g, b, a))

    @JvmOverloads
    fun color(r: Int, g: Int, b: Int, a: Int = 255) = color(asRGBA(r, g, b, a))

    operator fun Int.unaryPlus() = color(this)

    /** Font **/
    fun font(font: UIFont) {
        color(font.fontColor)
        font(font.fontName, font.fontSize, font.textAlignment, font.fontSpacing)
    }

    fun font(fontFace: String, fontSize: Float, fontAlignment: Int, fontSpacing: Float) {
        this.fontFace = fontFace
        this.fontSize = fontSize
        this.fontAlignment = fontAlignment
        this.fontSpacing = fontSpacing
    }

    /**
     * Renders a string
     *
     * @see font
     */
    fun String.render(x: Float, y: Float) =
        renderer.renderString(this, x, y)

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
     * Renders a string until the given width, where appended string is
     * added to that point and the string is truncated to that point. For
     * example, if the text is "Hello", the appended string is ".." and "Hel.."
     * width is greater than width, then the string rendered is "Hel..". If
     * appended string is blank, the string is cut off at the given width point.
     */
    fun String.render(x: Float, y: Float, width: Float, appendedString: String) {
        var w = 0f
        var aw = appendedString.width()
        for (c in 0 until this.length) {
            w += this[c].toString().width()
            if (w > width - aw) {
                this.substring(0, c) + appendedString.render(x, y)
            }
        }
    }

    fun String.width(): Float = renderer.stringWidth(this)

    fun String.height(): Float = renderer.stringHeight(this)

    fun ascender() = renderer.stringAscender()

    fun descender() = renderer.stringDescender()

    fun loadFont(name: String, fileLocation: String) =
        loadFont(name, fileLocation.toByteBuffer())

    fun loadFont(name: String, buffer: ByteBuffer) =
        renderer.loadFont(name, buffer)

    operator fun UIFont.unaryPlus() = font(this)

    /** Image **/
    @JvmOverloads
    fun renderImage(imageName: String, x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        renderImage(imageName, x, y, width, height, radius, radius, radius, radius)

    fun renderImage(imageName: String, x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) =
        renderer.renderImage(imageName, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)

    fun loadImage(name: String, fileLocation: String, flags: Int = MIPMAP or REPEATX or REPEATY) =
        loadImage(name, fileLocation.toByteBuffer(), flags)

    fun loadImage(name: String, buffer: ByteBuffer, flags: Int) =
        renderer.loadImage(name, buffer, flags)

    @JvmOverloads
    fun loadSvg(name: String, fileLocation: String, scale: Float = 2f) =
        loadSvg(name, fileLocation.toTerminatingByteBuffer(), scale)

    fun loadSvg(name: String, buffer: ByteBuffer, scale: Float) =
        renderer.loadSVG(name, buffer, scale)

    /** Asset Loading **/
    @JvmOverloads
    fun loadAsset(name: String, fileLocation: String, fileExtension: String = FilenameUtils.getExtension(fileLocation)) {
        when (fileExtension) {
            "ttf" -> loadFont(name, fileLocation)
            "png", "jpeg" -> loadImage(name, fileLocation)
            "svg" -> loadSvg(name, fileLocation)
            else -> throw UnsupportedOperationException("Unknown file extension: $fileExtension")
        }
    }

    /** Shapes **/
    @JvmOverloads
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        rect(x, y, width, height, radius, radius, radius, radius)

    fun rect(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) =
        rect(x, y,width, height,
                radius?.topLeft ?: 0f, radius?.topRight ?: 0f,
                radius?.bottomRight ?: 0f, radius?.bottomRight ?: 0f
        )

    fun rect(x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) =
        renderer.rect(x + pos, y + pos, width + siz, height + siz, topLeft + halfsw, topRight + halfsw, bottomRight + halfsw, bottomLeft + halfsw)
    
    fun ellipse(x: Float, y: Float, width: Float, height: Float) =
        renderer.ellipse(x, y, width - pos, height - pos)

    fun circle(x: Float, y: Float, radius: Float) =
        renderer.circle(x, y, radius - pos)

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
        this.stroke = true
        this.strokeWidth = strokeWidth
        this.strokeDirection = strokeDirection
        this.halfsw = strokeWidth / 2f
        this.halfsw = if (this.halfsw < 0.1f) 0f else this.halfsw
        // Calculate the stroke direction
        pos = when (UIRendererDSL.strokeDirection) {
            StrokeDirection.INSIDE -> UIRendererDSL.strokeWidth / 2f
            StrokeDirection.OUTSIDE -> 0f -(UIRendererDSL.strokeWidth / 2f)
            StrokeDirection.CENTER -> 0f
        }
        siz = when (UIRendererDSL.strokeDirection) {
            StrokeDirection.INSIDE -> -UIRendererDSL.strokeWidth
            StrokeDirection.OUTSIDE -> UIRendererDSL.strokeWidth
            StrokeDirection.CENTER -> 0f
        }
    }

    fun finishStroke() {
        this.stroke = false
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

    inline fun stroke(strokeColor: Int, strokeWidth: Float, strokeDirection: StrokeDirection = StrokeDirection.OUTSIDE, block: UIRendererDSL.() -> Unit) {
        renderer.stroke(strokeWidth, strokeColor)
        stroke(strokeWidth, strokeDirection)
        this.block()
        finishStroke()
        renderer.finishStroke()
    }

    inline fun renderContent(fbo: UIContentFBO, block: UIRendererDSL.() -> Unit) {
        renderer.bindContentFBO(fbo)
        renderer.beginFrame(fbo.width, fbo.height, 1f)
        this.block()
        renderer.endFrame()
        renderer.unbindContentFBO()
    }

    /**
     * Instructs the renderer how to render the stroke. When center, the stroke is
     * centered to the outline of the given shape. Outside, is the outside of the shape
     * , and inside is the inside of the shape.
     */
    enum class StrokeDirection {
        CENTER,
        OUTSIDE,
        INSIDE
    }
}