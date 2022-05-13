package net.prismclient.aether.ui.renderer.builder

import net.prismclient.aether.UICore
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.component.util.enums.UIAlignment.*
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNLEFT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNTOP
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.MIPMAP
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATX
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.REPEATY
import net.prismclient.aether.ui.renderer.impl.border.UIBorder
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.limit
import net.prismclient.aether.ui.util.extensions.nullableByteBuffer
import java.nio.ByteBuffer
import kotlin.math.max

/**
 * UIRendererDSL handles the entire rendering for AetherUI. It calls the
 * active instance of the [UIRenderer] class to render.
 *
 * @author sen
 */
class UIRendererDSL(val render: UIRenderer) {
    /* General Properties */
    private var color = 0
    private var globalAlpha = 1f

    /* Font */
    private var fontFace = ""
    private var fontSize = 0f
    private var textAlign = 0
    private var fontSpacing = 0f

    /* Stroke */
    var stroke = false
    var strokeWidth = 0f
    var strokeColor = 0

    init {
        instance = this
    }

    fun beginFrame(width: Float, height: Float, devicePixelRatio: Float = 1f) =
        render.beginFrame(width, height, devicePixelRatio)

    fun endFrame() {
        render.endFrame()
        resetCalls()
    }

    fun resetCalls() {
        color(-1)
        color = 0
        globalAlpha = 1f
        fontFace = "Default"
        fontSize = 14f
        textAlign = ALIGNTOP or ALIGNLEFT
        fontSpacing = 0f
        stroke = false
        strokeWidth = 0f
        strokeColor = 0
    }

    fun save() = render.save()

    fun restore() = render.restore()

    fun color(color: Int) {
        // Apply the global alpha
        this.color = color// (color.getAlpha() * globalAlpha + 0.5f).toInt()
        this.render.color(this.color)
    }

    fun color(r: Int, g: Int, b: Int, a: Int) = color(asRGBA(r, g, b, a))

    fun color(r: Float, g: Float, b: Float, a: Float) = color(asRGBA(r, g, b, a))

    fun alpha(a: Float) {
        globalAlpha = a.limit()
    }

    fun alpha(a: Int) {
        globalAlpha = (a / 255f).limit()
    }

    @JvmOverloads
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        rect(x, y, width, height, radius, radius, radius, radius)

    fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) {
        if (stroke) {
            val half = strokeWidth / 2f
            render.srect(
                x - half,
                y - half,
                width + strokeWidth,
                height + strokeWidth,
                topLeft,
                topRight,
                bottomRight,
                bottomLeft
            )
        } else render.rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
    }

    fun rect(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) =
        rect(
            x,
            y,
            width,
            height,
            radius?.topLeft ?: 0f,
            radius?.topRight ?: 0f,
            radius?.bottomRight ?: 0f,
            radius?.bottomLeft ?: 0f
        )


    fun triangle() {
        TODO("Triangle")
    }

    fun circle() {
        TODO("Circle")
    }

    fun ellipse(x: Float, y: Float, width: Float, height: Float) {
        if (stroke) {
            val half = strokeWidth / 2f
            render.sellipse(x - half, y - half, width + strokeWidth, height + strokeWidth)
        } else render.ellipse(x, y, width, height)
    }

    fun loadImage(imageName: String, fileLocation: String, imageFlags: Int = REPEATX or REPEATY or MIPMAP): Boolean =
        render.loadImage(imageName, fileLocation.ifEmpty { "/images/$imageName.png" }.nullableByteBuffer(), imageFlags)


    fun loadImage(imageName: String, imageData: ByteBuffer, imageFlags: Int = REPEATX or REPEATY or MIPMAP): Boolean =
        render.loadImage(imageName, imageData, imageFlags)


    fun renderImage(imageName: String, x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        render.renderImage(imageName, x, y, width, height, radius)

    @JvmOverloads
    fun loadFont(fontName: String, fileLocation: String = "/fonts/$fontName.ttf") =
        render.loadFont(fontName, fileLocation.ifEmpty { "/fonts/$fontFace.ttf" }.nullableByteBuffer())

    fun loadFont(fontName: String, fontBuffer: ByteBuffer) =
        render.loadFont(fontName, fontBuffer)

    fun String.width(): Float = render.stringWidth(this)

    fun String.height(): Float = render.stringHeight(this)

    fun String.render(x: Float, y: Float) {
        render.renderString(this, x, y)
    }

    fun String.renderWrap(x: Float, y: Float, width: Float, splitHeight: Float) {
        render.wrapString(this, x, y, width, splitHeight)
    }

    fun String.render(alignment: UIAlignment, x: Float, y: Float, width: Float, height: Float) {
        val alignedX: Float = when (alignment) {
            TOPCENTER, CENTER, BOTTOMCENTER -> width / 2f //(width - this.width()) / 2f
            TOPRIGHT, MIDDLERIGHT, BOTTOMRIGHT -> width //(width - this.width())
            else -> 0f
        } + x
        val alignedY: Float = when (alignment) {
            MIDDLELEFT, CENTER, MIDDLERIGHT -> height / 2f //(height - this.height()) / 2f
            BOTTOMLEFT, BOTTOMCENTER, BOTTOMRIGHT -> height //(height - this.height())
            else -> 0f
        } + y

        this.render(alignedX, alignedY)
    }

    fun ascender() = render.stringAscender()

    fun descender() = render.stringDescender()

    fun font(font: UIFont) {
        color(font.fontColor)
        font(font.fontName, font.fontSize, font.textAlignment, font.fontSpacing)
    }

    fun font(
        fontFace: String,
        fontSize: Float,
        textAlign: Int,
        fontSpacing: Float
    ) {
        this.fontFace = fontFace
        this.fontSize = fontSize
        this.textAlign = textAlign
        this.fontSpacing = fontSpacing
        this.render.font(fontFace, fontSize, textAlign, fontSpacing)
    }

    inline fun outline(outlineWidth: Float, outlineColor: Int, block: UIRendererDSL.() -> Unit) {
        if (outlineWidth <= 0f) return
        stroke = true
        instance.strokeWidth = outlineWidth
        instance.strokeColor = outlineColor
        render.stroke(strokeWidth, strokeColor)
        this.block()
        stroke = false
    }

    fun translate(x: Float, y: Float) = render.translate(x, y)

    fun scale(x: Float, y: Float) = render.scale(x, y)

    fun rotate(angle: Float) = render.rotate(angle)

    fun rotate(degree: Double) = render.rotate(degree)

    fun scissor(x: Float, y: Float, width: Float, height: Float) = render.scissor(x, y, width, height)

    inline fun translate(x: Float, y: Float, block: UIRendererDSL.() -> Unit) {
        save()
        translate(x, y)
        block.invoke(this)
        restore()
    }

    inline fun scale(x: Float, y: Float, block: UIRendererDSL.() -> Unit) {
        save()
        scale(x, y)
        block.invoke(this)
        restore()
    }

    inline fun rotate(angle: Float, block: UIRendererDSL.() -> Unit) {
        save()
        rotate(angle)
        block.invoke(this)
        restore()
    }

    inline fun rotate(angle: Double, block: UIRendererDSL.() -> Unit) {
        save()
        rotate(angle)
        block.invoke(this)
        restore()
    }

    inline fun scissor(x: Float, y: Float, width: Float, height: Float, block: UIRendererDSL.() -> Unit) {
        save()
        scissor(x, y, width, height)
        block.invoke(this)
        restore()
    }

    inline fun renderContent(fbo: UIContentFBO, block: UIRendererDSL.() -> Unit) {
        render.bindContentFBO(fbo)
        render.beginFrame(fbo.width, fbo.height, 1f) //max(UICore.contentScaleX, UICore.contentScaleY))
        block.invoke(this)
        render.endFrame()
        render.unbindContentFBO()
    }

    fun renderFbo(
        fbo: UIContentFBO,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) {
        render.renderFBO(fbo, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
    }

    companion object {
        @JvmStatic
        lateinit var instance: UIRendererDSL
    }
}