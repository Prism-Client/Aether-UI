package net.prismclient.aether

import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.renderer.other.UIContentFBO
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGLUFramebuffer
import org.lwjgl.nanovg.NVGPaint

import org.lwjgl.nanovg.NanoVGGL3.*
import org.lwjgl.nanovg.NanoVG.*

import java.nio.ByteBuffer

/**
 * Aether allows for the ability to provide your own rendering system. However, you might not
 * want to write a rendering system, or simply don't know how. The class provides an implementation
 * with NanoVG which is fast vector graphics rendering library.
 *
 * @author sen
 * @since 1.0
 */
class NanoVGRenderer : UIRenderer {
    private val framebuffers: HashMap<UIContentFBO, NVGLUFramebuffer> = hashMapOf()

    private val ctx: Long = nvgCreate(NVG_ANTIALIAS)
    private val color: NVGColor? = null
    private val paint: NVGPaint? = null

    private var activeColor: Int = 0

    override fun beginFrame(width: Float, height: Float, devicePxRatio: Float) = nvgBeginFrame(ctx, width, height, devicePxRatio)

    override fun endFrame() = nvgEndFrame(ctx)

    override fun cancelFrame() = nvgCancelFrame(ctx)

    override fun save() = nvgSave(ctx)

    override fun restore() = nvgRestore(ctx)

    override fun reset() = nvgReset(ctx)

    override fun color(color: Int) {
        activeColor = color
    }

    override fun globalAlpha(alpha: Float) = nvgGlobalAlpha(ctx, alpha)

    override fun transform(a: Float, b: Float, c: Float, d: Float, e: Float, f: Float) = nvgTransform(ctx, a, b, c, d, e, f)

    override fun translate(x: Float, y: Float)  = nvgTranslate(ctx, x, y)

    override fun scale(x: Float, y: Float) = nvgScale(ctx, x, y)

    override fun rotate(angle: Float) = nvgRotate(ctx, angle)

    override fun skewX(angle: Float) = nvgSkewX(ctx, angle)

    override fun skewY(angle: Float) = nvgSkewY(ctx, angle)

    override fun scissor(x: Float, y: Float, w: Float, h: Float) = nvgScissor(ctx, x, y, w, h)

    override fun resetScissor() = nvgResetScissor(ctx)

    override fun resetTransformation() = nvgResetTransform(ctx)

    override fun useAntialiasing(antialiasing: Boolean) = nvgShapeAntiAlias(ctx, antialiasing)

    override fun createImage(imageData: UIImageData) {
        TODO("Not yet implemented")
    }

    override fun deleteImage(imageData: UIImageData) {
        TODO("Not yet implemented")
    }

    override fun createImageFromHandle(handle: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteImageFromHandle(handle: Int) {
        TODO("Not yet implemented")
    }

    override fun createFont(fontName: String, fontData: ByteBuffer): Boolean {
        TODO("Not yet implemented")
    }

    override fun imagePattern(
        imageData: UIImageData,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        angle: Float,
        alpha: Float
    ) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun ellipse(x: Float, y: Float, xRadius: Float, yRadius: Float) {
        TODO("Not yet implemented")
    }

    override fun circle(x: Float, y: Float, radius: Float) {
        TODO("Not yet implemented")
    }

    override fun beginPath() {
        TODO("Not yet implemented")
    }

    override fun closePath() {
        TODO("Not yet implemented")
    }

    override fun fill() {
        TODO("Not yet implemented")
    }

    override fun stroke() {
        TODO("Not yet implemented")
    }

    override fun pathWinding(winding: Int) {
        TODO("Not yet implemented")
    }

    override fun moveTo(x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun lineTo(x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun bezierTo(cx: Float, cy: Float, cx2: Float, cy2: Float, x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun quadTo(cx: Float, cy: Float, x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun arc(
        x: Float,
        y: Float,
        radius: Float,
        angle: Float,
        startAngle: Float,
        endAngle: Float,
        windingOrder: UIRenderer.WindingOrder
    ) {
        TODO("Not yet implemented")
    }

    override fun arcTo(x: Float, y: Float, x1: Float, y1: Float, radius: Float) {
        TODO("Not yet implemented")
    }

    override fun strokeWidth(size: Float) {
        TODO("Not yet implemented")
    }

    override fun lineCap(cap: UIRenderer.LineCap) {
        TODO("Not yet implemented")
    }

    override fun lineJoin(join: UIRenderer.LineJoin) {
        TODO("Not yet implemented")
    }

    override fun degToRad(deg: Float): Float = nvgDegToRad(deg)

    override fun radToDeg(rad: Float): Float = nvgRadToDeg(rad)

}