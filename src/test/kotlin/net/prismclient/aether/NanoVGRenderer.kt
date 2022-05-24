package net.prismclient.aether

import net.prismclient.aether.UICore.Companion.contentScaleX
import net.prismclient.aether.UICore.Companion.contentScaleY
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.style.UIProvider.getImage
import net.prismclient.aether.ui.style.UIProvider.registerImage
import net.prismclient.aether.ui.util.extensions.getAlpha
import net.prismclient.aether.ui.util.extensions.getBlue
import net.prismclient.aether.ui.util.extensions.getGreen
import net.prismclient.aether.ui.util.extensions.getRed
import org.lwjgl.nanovg.*
import org.lwjgl.opengl.GL11
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

/**
 * [DefaultRenderer] is an example implementation for the renderer
 * of Aether UI which uses NanoVG.
 *
 * @author sen
 * @since 4/23/2022
 */
class NanoVGRenderer : UIRenderer() {
    private val ctx = NanoVGGL3.nnvgCreate(NanoVGGL3.NVG_ANTIALIAS)
    private val fbos = HashMap<UIContentFBO, NVGLUFramebuffer>()
    private val color = NVGColor.create()
    private val none = NVGColor.create()
    private val outlineColor = NVGColor.create()
    private val paint = NVGPaint.create()
    override fun beginFrame(width: Float, height: Float, devicePixelRatio: Float) {
        NanoVG.nvgBeginFrame(ctx, width, height, devicePixelRatio)
    }

    override fun endFrame() {
        NanoVG.nvgEndFrame(ctx)
    }

    override fun color(color: Int) {
        super.color(color)
        NanoVG.nvgRGBA(r().toByte(), g().toByte(), b().toByte(), a().toByte(), this.color)
    }

    override fun createContentFBO(width: Float, height: Float): UIContentFBO {
        if (width <= 0 || height <= 0) throw RuntimeException("Failed to create the framebuffer. It must have a width and height greater than 0")
        val contentScale = Math.max(contentScaleX, contentScaleY)
        val framebuffer = NanoVGGL3.nvgluCreateFramebuffer(
                ctx, (width * contentScale).toInt(), (height * contentScale).toInt(),
                NanoVG.NVG_IMAGE_REPEATX or NanoVG.NVG_IMAGE_REPEATY
        ) ?: throw RuntimeException("Failed to create the framebuffer. w: $width, h: $height")
        val fbo = UIContentFBO(
                framebuffer.fbo(),
                width * contentScale,
                height * contentScale,
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

    override fun renderFBO(fbo: UIContentFBO, x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        val cs = fbo.contentScale
        NanoVG.nvgImagePattern(ctx, x, y, width, height, 0f, fbos[fbo]!!.image(), 1f, paint)
        NanoVG.nvgBeginPath(ctx)
        color(-1)
        paint.innerColor(color)
        paint.outerColor(color)
        NanoVG.nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        NanoVG.nvgFillPaint(ctx, paint)
        NanoVG.nvgFill(ctx)
    }

    override fun stroke(strokeWidth: Float, strokeColor: Int) {
        super.stroke(strokeWidth, strokeColor)
        NanoVG.nvgRGBA(strokeColor.getRed().toByte(), strokeColor.getGreen().toByte(), strokeColor.getBlue().toByte(), strokeColor.getAlpha().toByte(), outlineColor)
    }

    override fun save() {
        NanoVG.nvgSave(ctx)
    }

    override fun restore() {
        NanoVG.nvgRestore(ctx)
    }

    override fun translate(x: Float, y: Float) {
        NanoVG.nvgTranslate(ctx, x, y)
    }

    override fun rotate(angle: Float) {
        NanoVG.nvgRotate(ctx, angle)
    }

    override fun rotate(angle: Double) {
        NanoVG.nvgRotate(ctx, Math.toRadians(angle).toFloat())
    }

    override fun scale(x: Float, y: Float) {
        NanoVG.nvgScale(ctx, x, y)
    }

    override fun scissor(x: Float, y: Float, width: Float, height: Float) {
        NanoVG.nvgScissor(ctx, x, y, width, height)
    }

    override fun rect(x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        NanoVG.nvgBeginPath(ctx)
        if (width <= 1f || height <= 1f) {
            // Rendering 1px and below causes half transparent draws
            // because of how NanoVG renders antialiasing
            NanoVG.nvgShapeAntiAlias(ctx, false)
        }
        NanoVG.nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        check()
    }

    override fun linearGradient(x: Float, y: Float, width: Float, height: Float, radius: Float, gradientX: Float, gradientY: Float, gradientWidth: Float, gradientHeight: Float, color1: Int, color2: Int) {
        linearGradient(x, y, width, height, radius, radius, radius, radius, gradientX, gradientY, gradientWidth, gradientHeight, color1, color2)
    }

    override fun linearGradient(x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float, gradientX: Float, gradientY: Float, gradientWidth: Float, gradientHeight: Float, color1: Int, color2: Int) {
        val c1 = NVGColor.calloc()
        val c2 = NVGColor.calloc()
        val p = NVGPaint.calloc()
        NanoVG.nvgRGBA(color1.getRed().toByte(), color1.getGreen().toByte(), color1.getBlue().toByte(), color1.getAlpha().toByte(), c1)
        NanoVG.nvgRGBA(color2.getRed().toByte(), color2.getGreen().toByte(), color2.getBlue().toByte(), color2.getAlpha().toByte(), c2)
        NanoVG.nvgLinearGradient(ctx, gradientX, gradientY, gradientWidth, gradientHeight, c1, c2, p)
        NanoVG.nvgBeginPath(ctx)
        NanoVG.nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        NanoVG.nvgFillPaint(ctx, p)
        NanoVG.nvgFill(ctx)
        c1.free()
        c2.free()
        p.free()
    }

    override fun circle(x: Float, y: Float, radius: Float) {
        NanoVG.nvgBeginPath(ctx)
        NanoVG.nvgCircle(ctx, x, y, radius)
        check()
    }

    override fun ellipse(x: Float, y: Float, width: Float, height: Float) {
        NanoVG.nvgBeginPath(ctx)
        NanoVG.nvgEllipse(ctx, x, y, width, height)
        check()
    }

    override fun triangle(x: Float, y: Float, x1: Float, y1: Float, x2: Float, y2: Float) {
        NanoVG.nvgBeginPath(ctx)
        NanoVG.nvgMoveTo(ctx, x, y)
        NanoVG.nvgLineTo(ctx, x1, y1)
        NanoVG.nvgLineTo(ctx, x2, y2)
        NanoVG.nvgLineTo(ctx, x, y)
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
        image.handle = NanoVG.nvgCreateImageRGBA(ctx, width[0], height[0], imageFlags, image.buffer)
        image.width = width[0].toFloat()
        image.height = height[0].toFloat()
        image.loaded = true
        registerImage(imageName, image)
        return image
    }

    override fun loadSVG(svgName: String, image: UIImageData, scale: Float): UIImageData {
        image.imageType = UIImageData.ImageType.Svg
        val img: NSVGImage?
        val stack = MemoryStack.stackPush()

        try {
            if (image.buffer == null) {
                println("Failed to load the svg: $svgName")
                return image
            }
            img = NanoSVG.nsvgParse(image.buffer, stack.ASCII("px"), 96f)
            if (img == null) {
                throw RuntimeException("Failed to parse SVG. name: $svgName, scale:$scale")
            }
        } finally {
            stack.pop()
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
        image.handle = NanoVG.nvgCreateImageRGBA(ctx, w, h, NanoVG.NVG_IMAGE_REPEATX or NanoVG.NVG_IMAGE_REPEATY or NanoVG.NVG_IMAGE_GENERATE_MIPMAPS, rast)
        image.width = w.toFloat()
        image.height = h.toFloat()
        image.loaded = true
        registerImage(svgName, image)
        return image
    }

    override fun renderImage(imageName: String, x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        val img = getImage(imageName)
        var handle = 0
        if (img != null) handle = img.handle
        NanoVG.nvgImagePattern(ctx, x, y, width, height, 0f, handle, 1f, paint)
        paint.innerColor(color)
        paint.outerColor(color)
        NanoVG.nvgBeginPath(ctx)
        NanoVG.nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        NanoVG.nvgFillPaint(ctx, paint)
        NanoVG.nvgFill(ctx)
    }

    override fun deleteImage(imageName: String) {
        // TODO Deleting images not yet implemented
    }

    override fun loadFont(fontName: String, fontData: ByteBuffer?): Boolean {
        if (fontData == null) {
            println("Failed to load font $fontName as it was not found.")
            return false
        }
        NanoVG.nvgCreateFontMem(ctx, fontName, fontData, 0)
        return true
    }

    private val rows = NVGTextRow.create(maxRows)
    private val ascender = floatArrayOf(0f)
    private val decender = floatArrayOf(0f)
    private val lineHeight = floatArrayOf(0f)
    override fun renderString(text: String, x: Float, y: Float) {
        NanoVG.nvgFontBlur(ctx, 0f)
        NanoVG.nvgFontFace(ctx, fontName)
        NanoVG.nvgFontSize(ctx, fontSize)
        NanoVG.nvgTextAlign(ctx, fontAlignment)
        NanoVG.nvgFillColor(ctx, color)
        NanoVG.nvgTextLetterSpacing(ctx, fontSpacing)
        NanoVG.nvgText(ctx, x, y, text)
    }

    private var wrapWidth = 0f
    private var wrapHeight = 0f

    init {
        NanoVG.nvgRGBAf(0f, 0f, 0f, 0f, none)
    }

    override fun wrapString(text: String, x: Float, y: Float, width: Float, splitHeight: Float): Int {
        val rows = NVGTextRow.create(100)
        wrapWidth = 0f

        // Set the font state
        NanoVG.nvgFontBlur(ctx, 0f)
        NanoVG.nvgFontFace(ctx, fontName)
        NanoVG.nvgFontSize(ctx, fontSize)
        NanoVG.nvgTextAlign(ctx, fontAlignment)
        NanoVG.nvgFillColor(ctx, color)
        NanoVG.nvgTextLetterSpacing(ctx, fontSpacing)

        // Calculate the rows
        val nrows = NanoVG.nvgTextBreakLines(ctx, text, width, rows)

        // Get the metrics of the line
        NanoVG.nvgTextMetrics(ctx, ascender, decender, lineHeight)
        var h = y

        // Iterate through the rows
        for (i in 0 until nrows) {
            val row = rows[i]
            val w = NanoVG.nnvgText(ctx, x, h, row.start(), row.end()) - x // Render the text
            wrapWidth = Math.max(wrapWidth, w)
            h += lineHeight[0] + splitHeight // Increase by the font height plus the split height
        }
        wrapHeight = h - y
        return nrows
    }

    override fun stringWidth(text: String): Float {
        NanoVG.nvgFontBlur(ctx, 0f)
        NanoVG.nvgFontFace(ctx, fontName)
        NanoVG.nvgFontSize(ctx, fontSize)
        NanoVG.nvgTextAlign(ctx, fontAlignment)
        NanoVG.nvgFillColor(ctx, none)
        NanoVG.nvgTextLetterSpacing(ctx, fontSpacing)
        return NanoVG.nvgText(ctx, 0f, 0f, text)
    }

    override fun stringHeight(text: String): Float {
        NanoVG.nvgTextMetrics(ctx, null, null, lineHeight)
        return lineHeight[0]
    }

    override fun stringAscender(): Float {
        NanoVG.nvgTextMetrics(ctx, ascender, null, null)
        return ascender[0]
    }

    override fun stringDescender(): Float {
        NanoVG.nvgTextMetrics(ctx, null, decender, null)
        return Math.abs(decender[0])
    }

    override fun wrapWidth(): Float {
        return wrapWidth
    }

    override fun wrapHeight(): Float {
        return wrapHeight
    }

    private fun fill() {
        NanoVG.nvgFillColor(ctx, color)
        NanoVG.nvgFill(ctx)
    }

    private fun stroke() {
        NanoVG.nvgStrokeWidth(ctx, strokeWidth)
        NanoVG.nvgStrokeColor(ctx, outlineColor)
        NanoVG.nvgStroke(ctx)
    }

    private fun check() {
        if (stroke) {
            stroke()
        } else {
            fill()
        }
    }

    companion object {
        private const val maxRows = 100 /* Max rows created from [wrapString] */
    }
}