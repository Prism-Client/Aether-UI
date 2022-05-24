package net.prismclient.aether.ui.component.type.color

import net.prismclient.aether.ui.UICore
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.util.extensions.*
import java.awt.Color

class UIColorSwatch(style: String) : UIComponent<UIColorSwatchSheet>(style) {
    var focused = false
    var color = 0
        private set

    var hsb = floatArrayOf(0f, 0f, 0f)

    fun updateHSB() =
        Color.RGBtoHSB(style.swatchColor.getRed(), style.swatchColor.getGreen(), style.swatchColor.getBlue(), hsb)

    fun getActiveColor() =
        UICore.instance.coreCallback.getPixelColor(
            UICore.mouseX.coerceAtLeast(relX + getParentXOffset())
                .coerceAtMost(relX + relWidth + getParentXOffset() - 1f),
            UICore.mouseY.coerceAtLeast(relY + getParentYOffset() + 2f)
                .coerceAtMost(relY + relWidth + getParentYOffset())
        )

    fun adjustHue(hue: Float) {
        updateHSB()
        style.swatchColor = Color.HSBtoRGB(hue, hsb[1], hsb[2])
    }

    fun adjustSaturation(saturation: Float) {
        updateHSB()
        style.swatchColor = Color.HSBtoRGB(hsb[0], saturation, hsb[1])
    }

    fun adjustBrightness(brightness: Float) {
        updateHSB()
        style.swatchColor = Color.HSBtoRGB(hsb[0], hsb[1], brightness)
    }

    override fun renderComponent() {
        renderer {
            color(style.swatchColor)
            rect(relX, relY, relWidth, relHeight)
            renderer.linearGradient(x, y, relWidth, relHeight, 0f, x, y, x + relWidth, y, -1, asRGBA(0, 0, 0, 0))
            renderer.linearGradient(
                x,
                y,
                relWidth,
                relHeight,
                0f,
                x,
                y,
                x,
                y + relHeight,
                asRGBA(0, 0, 0, 0),
                asRGBA(0, 0, 0)
            )
        }
    }

    override fun mouseMoved(mouseX: Float, mouseY: Float) {
        super.mouseMoved(mouseX, mouseY)
        if (focused) {
            color = getActiveColor()
        }
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float) {
        super.mouseClicked(mouseX, mouseY)
        if (isMouseInsideBoundingBox()) {
            focused = true
            mouseMoved(mouseX, mouseY)
        }
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float) {
        super.mouseReleased(mouseX, mouseY)
        focused = false
    }
}