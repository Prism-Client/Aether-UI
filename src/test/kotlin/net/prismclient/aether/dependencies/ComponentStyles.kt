package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.component.type.input.slider.UISliderSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.UIDependable
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.extensions.radius
import net.prismclient.aether.ui.util.extensions.style

/**
 * An example of [UIDependable] which has component styles setup
 * for a theme so the same styles does not need to be created multiple times
 */
class ComponentStyles : UIDependable() {
    override fun load() {
        style(UISliderSheet(), "slider") {
            position(px(50), px(50))
            size(px(400), px(5))

            sliderControl.color = -1

            background {
                radius = radius(2.5f)
                color = asRGBA(255, 255, 255, 0.3f)
                border {
                    borderWidth = 1f
                    borderColor = asRGBA(255, 255, 255, 0.75f)
                }
            }
        }

        style(UIStyleSheet(), "btn") {
            background(asRGBA(0, 0, 0, 0.3f)) {
                radius = radius(15f)
            }
            x = px(200)
            y = px(200)
            width = px(150)
            height = px(50)

            font {
                align(UIAlignment.CENTER)
                textAlignment = UIRenderer.ALIGNMIDDLE or UIRenderer.ALIGNCENTER
                fontFamily = "Poppins"
                fontSize = 16f
                fontType = UIFont.FontType.Light
            }
        }
    }
}