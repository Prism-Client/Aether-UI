package net.prismclient.aether

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIProvider.getImage
import net.prismclient.aether.ui.style.UIProvider.registerImage
import net.prismclient.aether.ui.util.extensions.*
import org.lwjgl.nanovg.*
import org.lwjgl.nanovg.NanoVG.nvgLinearGradient
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.opengl.GL11
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.lang.Float.max
import java.nio.ByteBuffer
import kotlin.math.abs

/**
 * [NanoVGRenderer] is the default renderer implementation for Aether
 *
 * @author sen
 * @since 4/23/2022
 */
class NanoVGRenderer : UIRenderer() {
    private val ctx = NanoVGGL3.nnvgCreate(NanoVGGL3.NVG_ANTIALIAS)
    private val fbos = HashMap<UIContentFBO, NVGLUFramebuffer>()
    private val color = NVGColor.create()
    private val none = NVGColor.create().also { nvgRGBAf(0f, 0f, 0f, 0f, it) }
    private val outlineColor = NVGColor.create()
    private val paint = NVGPaint.create()

    override fun beginFrame(width: Float, height: Float, devicePixelRatio: Float) {
        nvgBeginFrame(ctx, width, height, devicePixelRatio)
    }

    override fun endFrame() {
        nvgEndFrame(ctx)
    }

    override fun color(color: Int) {
        super.color(color)
        nvgRGBA(color.getRed().toByte(), color.getGreen().toByte(), color.getBlue().toByte(), color.getAlpha().toByte(), this.color)
    }

    override fun createContentFBO(width: Float, height: Float): UIContentFBO {
        if (width <= 0 || height <= 0) throw RuntimeException("Failed to create the framebuffer. It must have a width and height greater than 0")
        val contentScale = UICore.devicePxRatio
        val framebuffer = NanoVGGL3.nvgluCreateFramebuffer(
            ctx, (width * contentScale).toInt(), (height * contentScale).toInt(),
            NVG_IMAGE_REPEATX or NVG_IMAGE_REPEATY
        ) ?: throw RuntimeException("Failed to create the framebuffer. w: $width, h: $height")
        val fbo = UIContentFBO(
            framebuffer.fbo(),
            width * contentScale,
            height * contentScale,
            width,
            height,
            contentScale
        )
        fbos[fbo] = framebuffer
        return fbo
    }

    override fun deleteContentFBO(fbo: UIContentFBO) {
        val framebuffer = fbos[fbo]
        if (framebuffer != null) {
            NanoVGGL3.nvgluDeleteFramebuffer(ctx, framebuffer)
            fbos.remove(fbo)
            return
        }
        println("Failed to delete the framebuffer because it was not found.")
    }

    override fun bindContentFBO(fbo: UIContentFBO) {
        NanoVGGL3.nvgluBindFramebuffer(ctx, fbos[fbo])
        GL11.glViewport(0, 0, fbo.width.toInt(), fbo.height.toInt())
        GL11.glClearColor(0f, 0f, 0f, 0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)
    }

    override fun unbindContentFBO() {
        NanoVGGL3.nvgluBindFramebuffer(ctx, null)
    }

    override fun renderFBO(
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
        nvgImagePattern(ctx, x, y, width, height, 0f, fbos[fbo]!!.image(), 1f, paint)
        nvgBeginPath(ctx)
        color(-1)
        paint.innerColor(color)
        paint.outerColor(color)
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        nvgFillPaint(ctx, paint)
        nvgFill(ctx)
    }

    override fun stroke(strokeWidth: Float, strokeColor: Int) {
        super.stroke(strokeWidth, strokeColor)
        nvgRGBA(
            strokeColor.getRed().toByte(),
            strokeColor.getGreen().toByte(),
            strokeColor.getBlue().toByte(),
            strokeColor.getAlpha().toByte(),
            outlineColor
        )
    }

    override fun save() {
        nvgSave(ctx)
    }

    override fun restore() {
        nvgRestore(ctx)
    }

    override fun translate(x: Float, y: Float) {
        nvgTranslate(ctx, x, y)
    }

    override fun rotate(angle: Float) {
        nvgRotate(ctx, angle)
    }

    override fun rotate(angle: Double) {
        nvgRotate(ctx, Math.toRadians(angle).toFloat())
    }

    override fun scale(x: Float, y: Float) {
        nvgScale(ctx, x, y)
    }

    override fun scissor(x: Float, y: Float, width: Float, height: Float) {
        nvgScissor(ctx, x, y, width, height)
    }

    override fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) {
        nvgBeginPath(ctx)
        if (width <= 1f || height <= 1f) {
            // Rendering 1px and below causes half transparent draws
            // because of how NanoVG renders antialiasing
            nvgShapeAntiAlias(ctx, false)
        }
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        check()
    }

    override fun startLine(x: Float, y: Float, lineCap: Int, lineJoin: Int, lineWidth: Float) {
        nvgBeginPath(ctx)
        nvgLineCap(ctx, lineCap)
        nvgLineJoin(ctx, lineJoin)
        nvgStrokeWidth(ctx, lineWidth)
        nvgMoveTo(ctx, x, y)
    }

    override fun line(x: Float, y: Float) =
        nvgLineTo(ctx, x, y)

    override fun bezier(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) =
        nvgBezierTo(ctx, x, y, x1, y1, x2, y2)

    override fun finishLine() {
        nvgStrokeColor(ctx, color)
        nvgStroke(ctx)
    }

    override fun linearGradient(
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
    ) {
        linearGradient(
            x,
            y,
            width,
            height,
            radius,
            radius,
            radius,
            radius,
            gradientX,
            gradientY,
            gradientWidth,
            gradientHeight,
            color1,
            color2
        )
    }

    override fun linearGradient(
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
    ) {
        val c1 = NVGColor.calloc()
        val c2 = NVGColor.calloc()
        val p = NVGPaint.calloc()
        nvgRGBA(
            color1.getRed().toByte(),
            color1.getGreen().toByte(),
            color1.getBlue().toByte(),
            color1.getAlpha().toByte(),
            c1
        )
        nvgRGBA(
            color2.getRed().toByte(),
            color2.getGreen().toByte(),
            color2.getBlue().toByte(),
            color2.getAlpha().toByte(),
            c2
        )
        nvgLinearGradient(ctx, gradientX, gradientY, gradientWidth, gradientHeight, c1, c2, p)
        nvgBeginPath(ctx)
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        nvgFillPaint(ctx, p)
        nvgFill(ctx)
        c1.free()
        c2.free()
        p.free()
    }

    override fun circle(x: Float, y: Float, radius: Float) {
        nvgBeginPath(ctx)
        nvgCircle(ctx, x, y, radius)
        check()
    }

    override fun ellipse(x: Float, y: Float, width: Float, height: Float) {
        nvgBeginPath(ctx)
        nvgEllipse(ctx, x, y, width, height)
        check()
    }

    override fun triangle(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) {
        nvgBeginPath(ctx)
        nvgMoveTo(ctx, x, y)
        nvgLineTo(ctx, x1, y1)
        nvgLineTo(ctx, x2, y2)
        nvgLineTo(ctx, x, y)
        check()
    }

    override fun loadImage(imageName: String, image: UIImageData, imageFlags: Int): UIImageData {
        image.imageType = UIImageData.ImageType.Image
        if (image.buffer == null) {
            println("Failed to load image with name: $imageName as it was null")
            return image
        }
        val width = intArrayOf(0)
        val height = intArrayOf(0)
        image.buffer = STBImage.stbi_load_from_memory(image.buffer, width, height, intArrayOf(0), 4)

        if (image.buffer == null) {
            println("Failed to parse file. Is it corrupted?")
            return image
        }

//        Imagecla.premultiplyAlpha(image.buffer, width[0], height[0], width[0] * 4)
        image.handle = nvgCreateImageRGBA(ctx, width[0], height[0], imageFlags, image.buffer)
        image.width = width[0]
        image.height = height[0]



        image.loaded = true
        registerImage(imageName, image)
        return image
    }

    override fun loadSVG(svgName: String, image: UIImageData, scale: Float): UIImageData {
        image.imageType = UIImageData.ImageType.Svg
        val img: NSVGImage?
        val stack = MemoryStack.stackPush().use {
            if (image.buffer == null) {
                println("Failed to load the svg: $svgName")
                return image
            }
            img = NanoSVG.nsvgParse(image.buffer, it.ASCII("px"), 96f)
            if (img == null) {
                throw RuntimeException("Failed to parse SVG. name: $svgName, scale:$scale")
            }
        }

        val rasterizer = NanoSVG.nsvgCreateRasterizer()
        val w = (img!!.width() * scale).toInt()
        val h = (img.height() * scale).toInt()
        val rast = MemoryUtil.memAlloc(w * h * 4)
        NanoSVG.nsvgRasterize(
            rasterizer,
            img, 0f, 0f,
            scale,
            rast, w, h,
            w * 4
        )
        NanoSVG.nsvgDeleteRasterizer(rasterizer)
        image.handle = nvgCreateImageRGBA(
            ctx,
            w,
            h,
            NVG_IMAGE_REPEATX or NVG_IMAGE_REPEATY or NVG_IMAGE_GENERATE_MIPMAPS,
            rast
        )
        image.width = w
        image.height = h
        image.loaded = true
        registerImage(svgName, image)
        return image
    }

    override fun renderImage(
        imageName: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) {
        val img = getImage(imageName)
        var handle = 0
        if (img != null) handle = img.handle
        nvgImagePattern(ctx, x, y, width, height, 0f, handle, 1f, paint)
        paint.innerColor(color)
        paint.outerColor(color)
        nvgBeginPath(ctx)
        nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        nvgFillPaint(ctx, paint)
        nvgFill(ctx)
    }

    override fun deleteImage(imageName: String) {
        nvgDeleteImage(ctx, getImage(imageName)?.handle ?: 0)
        UIProvider.deleteImage(imageName)
    }

    override fun loadFont(fontName: String, fontData: ByteBuffer?): Boolean {
        if (fontData == null) {
            println("Failed to load font $fontName as it was not found.")
            return false
        }
        nvgCreateFontMem(ctx, fontName, fontData, 0)
        return true
    }

    private val ascender = floatArrayOf(0f)
    private val descender = floatArrayOf(0f)
    private val lineHeight = floatArrayOf(0f)
    private val bounds = floatArrayOf(0f, 0f, 0f, 0f, 0f)

    override fun renderString(text: String, x: Float, y: Float) {
        nvgFontBlur(ctx, 0f)
        nvgFontFace(ctx, fontName)
        nvgFontSize(ctx, fontSize)
        nvgTextAlign(ctx, fontAlignment)
        nvgFillColor(ctx, color)
        nvgTextLetterSpacing(ctx, fontSpacing)
        bounds[4] = nvgTextBounds(ctx, x, y, text, bounds)
        nvgText(ctx, x, y, text)
    }

    private var wrapWidth = 0f
    private var wrapHeight = 0f

    override fun wrapString(text: String, x: Float, y: Float, width: Float, splitHeight: Float, lines: ArrayList<String>?): Int {
        // TODO: Rewrite text wrapping
        val rows = NVGTextRow.calloc(100)//private val rows = NVGTextRow.create(maxRows)
        wrapWidth = 0f

        // Set the font state
        nvgFontBlur(ctx, 0f)
        nvgFontFace(ctx, fontName)
        nvgFontSize(ctx, fontSize)
        nvgTextAlign(ctx, fontAlignment)
        nvgFillColor(ctx, color)
        nvgTextLetterSpacing(ctx, fontSpacing)

        // Calculate the rows
        val nrows = nvgTextBreakLines(ctx, text, width, rows)

        // Get the metrics of the line
        nvgTextMetrics(ctx, ascender, descender, lineHeight)
        var h = y

        // Iterate through the rows
        for (i in 0 until nrows) {
            val row = rows[i]
            lines?.add(MemoryUtil.memUTF8(row.start(), (row.end() - row.start()).toInt()))
            val w = nnvgText(ctx, x, h, row.start(), row.end()) - x // Render the text
            wrapWidth = max(wrapWidth, w)
            h += lineHeight[0] + splitHeight // Increase by the font height plus the split height
        }
        wrapHeight = h - y

        nvgTextBoxBounds(ctx, x, y, width, text, bounds)

        bounds[4] = bounds[3] - bounds[0]
        // Increase the height by the split height times the rows
        bounds[2] = bounds[2] + (splitHeight * nrows)

        rows.free()
        return  nrows
    }

    override fun stringX(): Float = bounds[0]

    override fun stringY(): Float = bounds[1]

    override fun stringWidth(): Float = bounds[2] - bounds[0]

    override fun stringHeight(): Float = bounds[3] - bounds[1]

    override fun stringAscender(): Float {
        nvgTextMetrics(ctx, ascender, null, null)
        return ascender[0]
    }

    override fun stringDescender(): Float {
        nvgTextMetrics(ctx, null, descender, null)
        return abs(descender[0])
    }

    override fun textBounds(): FloatArray = bounds

    override fun boundsOf(text: String): FloatArray {
        nvgFontBlur(ctx, 0f)
        nvgFontFace(ctx, fontName)
        nvgFontSize(ctx, fontSize)
        nvgTextAlign(ctx, fontAlignment)
        nvgTextLetterSpacing(ctx, fontSpacing)
        bounds[4] = nvgTextBounds(ctx, 0f, 0f, text, bounds)
        return bounds
    }

    private fun fill() {
        nvgFillColor(ctx, color)
        nvgFill(ctx)
    }

    private fun stroke() {
        nvgStrokeWidth(ctx, strokeWidth)
        nvgStrokeColor(ctx, outlineColor)
        nvgStroke(ctx)
    }

    private fun check() {
        if (stroke) {
            stroke()
        } else {
            fill()
        }
    }
}