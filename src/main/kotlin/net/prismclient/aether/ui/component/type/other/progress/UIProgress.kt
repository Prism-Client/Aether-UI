package net.prismclient.aether.ui.component.type.other.progress

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.util.extensions.renderer

/**
 * [UIProgress] is a component which displays the distance between the x and width
 * based on the [progress]. It is used to describe how long it has been since something
 * has been started. [progress] is represented as a float with a value between 0.0 and 1.0.
 *
 * Every property except the color reflects the properties of the background of this component.
 * To configure the color see [UIProgressSheet.progressColor].
 *
 * @author sen
 * @since 6/23/2022
 */
class UIProgress @JvmOverloads constructor(var progress: Float = 0f, style: String?) : UIComponent<UIProgressSheet>(style) {
    override fun renderComponent() {
        renderer {
            color(style.progressColor)
            rect(relX, relY, relWidth * progress, relHeight, style.background?.radius)
        }
    }
}