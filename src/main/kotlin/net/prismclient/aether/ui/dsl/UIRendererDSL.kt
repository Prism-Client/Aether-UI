package net.prismclient.aether.ui.dsl

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.dsl.UIPathDSL.hole
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.renderer.impl.property.UIRadius
import net.prismclient.aether.ui.util.UIColor

/**
 * [UIRendererDSL] wraps the [UIRenderer] class to minimize the amount of calls
 * to render something on screen. It also supplies support functions for more advanced
 * rendering features that the UIRenderer does not have.
 *
 * @author sen
 * @since 1.0
 */
object UIRendererDSL {
    val renderer
        get() = Aether.instance.renderer

    var color: Int = 0
        private set

    /**
     * Informs the [UIRendererDSL] that the active calls are a stroke. This is set with [stroke].
     *
     * @see stroke
     */
    var isStroke: Boolean = false

    /**
     * The active stroke width set with [stroke]. This does not reflect the renderer's stroke width
     * directly. This is set with [stroke].
     *
     */
    var activeStrokeWidth: Float = 0f

    /**
     * The active stroke direction set with [stroke].
     *
     * @see stroke
     */
    var activeStrokeDirection: StrokeDirection = StrokeDirection.CENTER

    /**
     * Informs the renderer to prepare to render a frame of the given size.
     *
     * @param devicePxRatio The device pixel ratio of the device.
     */
    fun beginFrame(displayWidth: Float, displayHeight: Float, devicePxRatio: Float) =
        renderer.beginFrame(displayWidth, displayHeight, devicePxRatio)

    /**
     * Informs the renderer to render all the calls, and flush the paths.
     */
    fun endFrame() = renderer.endFrame()

    /**
     * Sets the fill color to the given color.
     */
    fun color(color: Int) {
        UIRendererDSL.color = color
        renderer.color(color)
    }

    /**
     * Sets the fill color to the given color.
     */
    fun color(color: UIColor?) = color(color?.rgba ?: 0)

    // -- Image -- //


    // -- Font -- //

    /**
     * Applies the given font values to the active context.
     */
    fun font(fontFace: String, fontSize: Float, fontAlign: Int, fontSpacing: Float) {
        renderer.fontFace(fontFace)
        renderer.fontSize(fontSize)
        renderer.fontAlignment(fontAlign)
        renderer.fontSpacing(fontSpacing)
    }

    /**
     * Applies the property of the given [font] to the active context.
     */
    fun font(font: UIFont) {
        color(font.fontColor)
        font(font.fontName, font.cachedFontSize, font.textAlignment, font.cachedFontSpacing)
    }

    /**
     * Renders the given string at the given position
     */
    fun String.render(x: Float, y: Float) {
        path {
            renderer.renderText(this@render, x, y)
        }.fillPath()
    }

    /**
     * Returns the bounds of the most recent text render call
     */
    fun fontBounds(): FloatArray = renderer.fontBounds()

    /**
     * Returns the bounds of the given text. This is considered as text render call, so subsequent calls
     * to functions such as [fontBounds] and [fontAscender] will return the metrics of this.
     */
    fun String.fontBounds(): FloatArray = renderer.fontBounds(this)

    /**
     * Returns the ascender of the most recent text render call.
     */
    fun fontAscender(): Float = renderer.fontAscender()

    /**
     * Returns the descender of the most recent text render call.
     */
    fun fontDescender(): Float = renderer.fontDescender()

    // -- General Rendering -- //

    fun rect(x: Float, y: Float, width: Float, height: Float, radius: UIRadius?) = rect(
        x,
        y,
        width,
        height,
        radius?.topLeft ?: 0f,
        radius?.topRight ?: 0f,
        radius?.bottomRight ?: 0f,
        radius?.bottomLeft ?: 0f
    )

    /**
     * Renders a rectangle with a single radius value.
     */
    @JvmOverloads
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float = 0f) =
        rect(x, y, width, height, radius, radius, radius, radius)

    /**
     * Renders a rectangle with multiple radius values.
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
    ) {
        path {
            rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        }.fillPath()
        if (isStroke) {
            path {
                hole {
//                    when (activeStrokeDirection) {
//                        StrokeDirection.CENTER -> {
////                            rect()
//                        }
//                        StrokeDirection.INSIDE -> {}
//                        StrokeDirection.OUTSIDE -> {
                            renderer.rect(x - activeStrokeWidth, y - activeStrokeWidth, width + activeStrokeWidth, height + activeStrokeWidth, topLeft, topRight, bottomRight, bottomLeft)
                            renderer.rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
//                        }
//                    }
                }
            }.fillPath()
        }
    }

    /**
     * Renders an image with varying rounded corners at the given position.
     */
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
    ) {
        path {
            imagePattern(
                (UIProvider.getImage(imageName)
                    ?: throw NullPointerException("No image as found with the name $imageName")).handle,
                x, y,
                width, height,
                0f, 1f
            )
            rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
        }.fillPaint()
    }

    // -- Util -- //

    /**
     * Converts the given float from degrees to radians.
     */
    fun Float.toRad() = renderer.degToRad(this)

    /**
     * Converts the given float from radians to degrees.
     */
    fun Float.toDeg() = renderer.radToDeg(this)

    /**
     * Returns the x offset of the given string base on the index
     */
    fun String.indexOffset(index: Int): Float {
        var w = 0f
        if (index > this.length - 1) return this.fontBounds()[4] + this.fontBounds()[0]
        for (i in 0 until index) w += this[i].toString().fontBounds()[4]
        return w + this.fontBounds()[0]
    }

    /**
     * Creates a new path and invokes the code within the [block]. To automatically close the path
     * set [closePath] to true. Closing the path makes a line to the initial starting point.
     *
     * @param closePath Closes the path if true.
     */
    inline fun path(closePath: Boolean = false, block: UIPathDSL.() -> Unit): UIPathDSL {
        UIPathDSL.beginPath()
        UIPathDSL.block()
        if (closePath) UIPathDSL.closePath()
        return UIPathDSL
    }

    /**
     * Automatically saves and restores the state within this block. Any translations
     * and other states such as scissor are saved within the state.
     */
    inline fun save(block: UIRendererDSL.() -> Unit): UIRendererDSL {
        renderer.save()
        UIRendererDSL.block()
        renderer.restore()
        return this
    }

    /**
     * Saves the state (and automatically restores it) and translates the [block] by the given [x] and [y] values.
     */
    inline fun translate(x: Float, y: Float, block: UIRendererDSL.() -> Unit): UIRendererDSL = save {
        renderer.translate(x, y)
        block()
    }


    /**
     * Scissors (clips) any content that exceeds the give bounds.
     */
    inline fun scissor(
        x: Float, y: Float, width: Float, height: Float, block: UIRendererDSL.() -> Unit
    ): UIRendererDSL = save {
        renderer.scissor(x, y, width, height)
        block()
    }

    /**
     * Informs [UIRendererDSL] that anything within this [block] is should be a stroke.
     *
     * @see StrokeDirection
     */
    inline fun stroke(strokeWidth: Float, strokeDirection: StrokeDirection, block: UIRendererDSL.() -> Unit): UIRendererDSL {
        activeStrokeWidth = strokeWidth
        activeStrokeDirection = strokeDirection
        isStroke = true
        block()
        isStroke = false
        return this
    }

    /**
     * The direction to align a stroke via a [stroke] block.
     *
     * @since 1.2
     * @author sen
     */
    enum class StrokeDirection {
        CENTER,
        INSIDE,
        OUTSIDE
    }
}