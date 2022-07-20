package net.prismclient.aether.ui.component.type.other

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.UIColor
import net.prismclient.aether.ui.util.extensions.renderer
import net.prismclient.aether.ui.util.name

/**
 * [UIProgress] is a component which displays the distance between the x and width
 * based on the [progress]. It is used to describe how long it has been since something
 * has been started. [progress] is represented as a float with a value between 0.0 and 1.0.
 *
 * Every property except the color reflects the properties of the background of this component.
 * To configure the color see [UIProgressSheet.progressColor].
 *
 * @author sen
 * @since 1.0
 */
class UIProgress @JvmOverloads constructor(var progress: Float = 0f) : UIComponent<UIProgressSheet>() {
    override fun renderComponent() {
        renderer {
            color(style.progressColor)
            rect(relX, relY, relWidth * progress, relHeight, style.background?.radius)
        }
    }

    override fun createsStyle(): UIProgressSheet = UIProgressSheet()
}

class UIProgressSheet : UIStyleSheet() {
    /**
     * The color of the actual progress bar.
     */
    var progressColor: UIColor? = null

    override fun copy() = UIProgressSheet().name(name).also {
        it.apply(this)
        it.progressColor = progressColor
    }
}