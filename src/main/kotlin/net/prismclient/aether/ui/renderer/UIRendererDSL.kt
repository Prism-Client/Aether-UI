package net.prismclient.aether.ui.renderer

import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.util.extensions.asRGBA
import org.apache.commons.io.FilenameUtils

object UIRendererDSL {
    lateinit var renderer: UIRenderer

    /** Color **/
    fun color(rgba: Int) {

    }

    fun color(r: Int, g: Int, b: Int, a: Float = 1f) = color(asRGBA(r, g, b, a))

    @JvmOverloads
    fun color(r: Int, g: Int, b: Int, a: Int = 255) = color(asRGBA(r, g, b, a))

    operator fun Int.unaryPlus() = color(this)

    /** Font **/
    fun font(font: UIFont) {
        color(font.fontColor)
        font(font.fontName, font.fontSize, font.textAlignment)
    }

    fun font(fontFace: String, fontSize: Float, fontAlignment: Int) {

    }

    operator fun UIFont.unaryPlus() = font(this)

    fun loadFont(name: String, fileLocation: String) {

    }

    /** Image **/
    fun renderImage(x: Float, y: Float, width: Float, height: Float, radius: Float) =
        renderImage(x, y, width, height, radius, radius, radius, radius)

    fun renderImage(x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {

    }

    fun loadImage(name: String, fileLocation: String) {

    }

    fun loadSvg(name: String, fileLocation: String) {

    }

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
    fun rect(x: Float, y: Float, width: Float, height: Float, radius: Float) =
        rect(x, y, width, height, radius, radius, radius, radius)

    fun rect(x: Float, y: Float, width: Float, height: Float, topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        renderer.rect(x, y, width, height, topLeft, topRight, bottomRight, bottomLeft)
    }

    fun ellipse(x: Float, y: Float, width: Float, height: Float) {
        renderer.ellipse(x, y, width, height)
    }

    fun circle(x: Float, y: Float, radius: Float) {

    }

    /** DSL Blocks **/

}