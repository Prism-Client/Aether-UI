import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import net.prismclient.aether.ui.util.extensions.*
import org.lwjgl.nanovg.*
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.opengl.GL11
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

/**
 * Aether allows for the ability to provide your own rendering system. However, you might not
 * want to write a rendering system, or simply don't know how. The class provides an implementation
 * with NanoVG which is a fast vector graphics rendering library.
 *
 * @author sen
 * @since 1.0
 */
class Renderer : UIRenderer {
    private val framebuffers: HashMap<UIContentFBO, NVGLUFramebuffer> = hashMapOf()

    private val ctx: Long = nvgCreate(NVG_ANTIALIAS)
    private val fillColor: NVGColor = NVGColor.create()
    private val strokeColor: NVGColor = NVGColor.create()
    private var paint: NVGPaint? = null

    private var activeColor: Int = 0

    private var fontBounds: FloatArray = FloatArray(5)
    private var ascender: FloatArray = FloatArray(1)
    private var descender: FloatArray = FloatArray(1)

    override fun beginFrame(width: Float, height: Float, devicePxRatio: Float) =
        nvgBeginFrame(ctx, width, height, devicePxRatio)

    override fun endFrame() = nvgEndFrame(ctx)

    override fun cancelFrame() = nvgCancelFrame(ctx)

    override fun save() = nvgSave(ctx)

    override fun restore() = nvgRestore(ctx)

    override fun reset() = nvgReset(ctx)

    override fun color(color: Int) {
        activeColor = color
        nvgFillColor(
            ctx, nvgRGBA(
                color.getRed().toByte(),
                color.getGreen().toByte(),
                color.getBlue().toByte(),
                color.getAlpha().toByte(),
                this.fillColor
            )
        )
    }

    override fun globalAlpha(alpha: Float) = nvgGlobalAlpha(ctx, alpha)

    override fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float) =
        nvgTransform(ctx, a, b, c, d, e, f)

    override fun translate(x: Float, y: Float) = nvgTranslate(ctx, x, y)

    override fun scale(x: Float, y: Float) = nvgScale(ctx, x, y)

    override fun rotate(angle: Float) = nvgRotate(ctx, angle)

    override fun skewX(angle: Float) = nvgSkewX(ctx, angle)

    override fun skewY(angle: Float) = nvgSkewY(ctx, angle)

    override fun scissor(x: Float, y: Float, w: Float, h: Float) = nvgScissor(ctx, x, y, w, h)

    override fun resetScissor() = nvgResetScissor(ctx)

    override fun resetTransformation() = nvgResetTransform(ctx)

    override fun useAntialiasing(antialiasing: Boolean) = nvgShapeAntiAlias(ctx, antialiasing)

    override fun createFBO(width: Float, height: Float): UIContentFBO {
        if (width <= 0 || height <= 0) throw RuntimeException("Failed to create the framebuffer. It must have a width and height greater than 0")
        val contentScale = Aether.devicePxRatio
        val framebuffer = nvgluCreateFramebuffer(
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
        framebuffers[fbo] = framebuffer
        return fbo
    }

    override fun deleteFBO(fbo: UIContentFBO) {
        val framebuffer = framebuffers[fbo]
        if (framebuffer != null) {
            nvgluDeleteFramebuffer(ctx, framebuffer)
            framebuffers.remove(fbo)
            return
        }
        println("Failed to delete the framebuffer because it was not found.")
    }

    override fun bindFBO(fbo: UIContentFBO) {
        nvgluBindFramebuffer(
            ctx,
            framebuffers[fbo] ?: throw NullPointerException("Unable to find the framebuffer $fbo.")
        )
        GL11.glViewport(0, 0, fbo.width.toInt(), fbo.height.toInt())
        GL11.glClearColor(0f, 0f, 0f, 0f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)
    }

    override fun unbindFBO() = nvgluBindFramebuffer(ctx, null)

    override fun createImage(imageName: String, data: ByteBuffer, flags: Int): UIImageData {
        val imageData = UIImageData()
        val width = IntArray(1)
        val height = IntArray(1)
        imageData.buffer = STBImage.stbi_load_from_memory(data, width, height, intArrayOf(0), 4)
        imageData.width = width[0]
        imageData.height = height[0]
        imageData.handle = nvgCreateImageRGBA(
            ctx,
            imageData.width,
            imageData.height,
            flags,
            imageData.buffer ?: throw NullPointerException("Failed to load image. Is it corrupted?")
        )
        imageData.loaded = true
        UIProvider.registerImage(imageName, imageData)
        return imageData
    }

    override fun deleteImage(imageName: String) {
        nvgDeleteImage(ctx, UIProvider.getImage(imageName)?.handle ?: return)
        UIProvider.deleteImage(imageName)
    }

    override fun createSvg(svgName: String, data: ByteBuffer?, scale: Float): UIImageData {
        val image = UIImageData()
        image.buffer = data
        image.imageType = UIImageData.ImageType.Svg
        val img: NSVGImage?
        MemoryStack.stackPush().use {
            if (image.buffer == null) {
                println("Failed to load the svg: $svgName")
                return image
            }
            img = NanoSVG.nsvgParse(image.buffer!!, it.ASCII("px"), 96f)
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
        UIProvider.registerImage(svgName, image)
        return image
    }

    override fun createImageFromHandle(imageName: String, handle: Int, imageWidth: Int, imageHeight: Int): UIImageData {
        val image = UIImageData()
        image.handle = nnvglCreateImageFromHandle(ctx, handle, imageWidth, imageHeight, 0)
        image.imageType = UIImageData.ImageType.Image
        image.loaded = true
        return image
    }

    override fun createFont(fontName: String, fontData: ByteBuffer?): Boolean {
        if (fontData == null) {
            println("Failed to load font $fontName as it was not found.")
            return false
        }
        nvgCreateFontMem(ctx, fontName, fontData, 0)
        return true
    }

    override fun imagePattern(
        imageHandle: Int,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        angle: Float,
        alpha: Float
    ) {
        allocPaint()
        paint!!.innerColor(fillColor)
        paint!!.outerColor(fillColor)
        nvgImagePattern(
            ctx,
            x, y,
            width, height,
            angle,
            imageHandle,
            alpha,
            paint!!
        )
    }

    override fun fontFace(fontName: String) = nvgFontFace(ctx, fontName)

    override fun fontSize(size: Float) = nvgFontSize(ctx, size)

    override fun fontAlignment(alignment: Int) = nvgTextAlign(ctx, alignment)

    override fun fontSpacing(spacing: Float) = nvgTextLetterSpacing(ctx, spacing)

    override fun renderText(text: String, x: Float, y: Float) {
        nvgFillColor(ctx, fillColor)
        nvgText(ctx, x, y, text)
        nvgTextMetrics(ctx, ascender, descender, null)
        fontBounds[4] = nvgTextBounds(ctx, x, y, text, fontBounds)
    }

    override fun fontBounds(): FloatArray = fontBounds

    override fun fontBounds(text: String): FloatArray {
        nvgTextMetrics(ctx, ascender, descender, null)
        fontBounds[4] = nvgTextBounds(ctx, 0f, 0f, text, fontBounds)
        return fontBounds
    }

    override fun fontAscender(): Float = ascender[0]

    override fun fontDescender(): Float = -descender[0]

    override fun rect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float
    ) = nvgRoundedRectVarying(ctx, x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)

    override fun ellipse(x: Float, y: Float, xRadius: Float, yRadius: Float) = nvgEllipse(ctx, x, y, xRadius, yRadius)

    override fun circle(x: Float, y: Float, radius: Float) = nvgCircle(ctx, x, y, radius)

    override fun beginPath() = nvgBeginPath(ctx)

    override fun closePath() = nvgClosePath(ctx)

    override fun fill() {
        nvgFill(ctx)
        deallocatePaint()
    }

    override fun fillPaint() {
        nvgFillPaint(ctx, paint ?: throw NullPointerException("Attempted to fill paint without an active paint"))
        fill()
    }

    override fun fillColor(color: Int) = color(color)

    override fun stroke() {
        nvgStroke(ctx)
        deallocatePaint()
    }

    override fun strokePaint() {
        nvgStrokePaint(ctx, paint ?: throw NullPointerException("Attempted to stroke paint without an active paint"))
        stroke()
    }

    override fun strokeWidth(size: Float) = nvgStrokeWidth(ctx, size)

    override fun strokeColor(color: Int) {
        nvgRGBA(
            color.getRed().toByte(),
            color.getGreen().toByte(),
            color.getBlue().toByte(),
            color.getAlpha().toByte(),
            strokeColor
        )
        nvgStrokeColor(ctx, strokeColor)
    }

    override fun pathHole(value: Boolean) =
        nvgPathWinding(ctx, if (value) NVG_HOLE else NVG_SOLID)

    override fun moveTo(x: Float, y: Float) = nvgMoveTo(ctx, x, y)

    override fun lineTo(x: Float, y: Float) = nvgLineTo(ctx, x, y)

    override fun bezierTo(cx: Float, cy: Float, cx2: Float, cy2: Float, x: Float, y: Float) =
        nvgBezierTo(ctx, cx, cy, cx2, cy2, x, y)

    override fun quadTo(cx: Float, cy: Float, x: Float, y: Float) = nvgQuadTo(ctx, cx, cy, x, y)

    override fun arc(
        x: Float,
        y: Float,
        radius: Float,
        startAngle: Float,
        endAngle: Float,
        windingOrder: UIRenderer.WindingOrder
    ) = nvgArc(
        ctx,
        x,
        y,
        radius,
        startAngle,
        endAngle,
        if (windingOrder == UIRenderer.WindingOrder.CW) NVG_CW else NVG_CCW
    )

    override fun arcTo(x: Float, y: Float, x1: Float, y1: Float, radius: Float) =
        nvgArcTo(ctx, x, y, x1, y1, radius)

    override fun lineCap(cap: UIRenderer.LineCap) = nvgLineCap(
        ctx, when (cap) {
            UIRenderer.LineCap.Butt -> NVG_BUTT
            UIRenderer.LineCap.Round -> NVG_ROUND
            UIRenderer.LineCap.Square -> NVG_SQUARE
        }
    )

    override fun lineJoin(join: UIRenderer.LineJoin) = nvgLineJoin(
        ctx, when (join) {
            UIRenderer.LineJoin.Miter -> NVG_MITER
            UIRenderer.LineJoin.Round -> NVG_ROUND
            UIRenderer.LineJoin.Bevel -> NVG_BEVEL
        }
    )

    override fun linearGradient(x: Float, y: Float, x2: Float, y2: Float, startColor: Int, endColor: Int) {
        allocPaint()
        val color1 = createColor(startColor)
        val color2 = createColor(endColor)
        nvgLinearGradient(ctx, x, y, x2, y2, color1, color2, paint!!)
        color1.free()
        color2.free()
    }

    override fun radialGradient(
        x: Float,
        y: Float,
        innerRadius: Float,
        outerRadius: Float,
        startColor: Int,
        endColor: Int
    ) {
        allocPaint()
        val color1 = createColor(startColor)
        val color2 = createColor(endColor)
        nvgRadialGradient(ctx, x, y, innerRadius, outerRadius, color1, color2, paint!!)
        color1.free()
        color2.free()
    }

    override fun allocPaint() {
        paint = NVGPaint.calloc()
    }

    override fun deallocatePaint() {
        paint?.free()
        paint = null
    }

    override fun degToRad(deg: Float): Float = nvgDegToRad(deg)

    override fun radToDeg(rad: Float): Float = nvgRadToDeg(rad)

    private fun createColor(color: Int): NVGColor {
        val nvgColor = NVGColor.calloc()
        nvgRGBA(
            color.getRed().toByte(),
            color.getGreen().toByte(),
            color.getBlue().toByte(),
            color.getAlpha().toByte(),
            nvgColor
        )
        return nvgColor
    }
}