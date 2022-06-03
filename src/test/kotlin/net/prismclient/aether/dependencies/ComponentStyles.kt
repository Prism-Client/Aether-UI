package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.input.slider.UISliderSheet
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.interfaces.UIDependable

/**
 * An example of [UIDependable] which has component styles setup
 * for a theme so the same styles does not need to be created multiple times
 */
class ComponentStyles : UIDependable() {
    override fun load() {
        /** Button **/
        style(UIStyleSheet(), "btn") {
            size(380f, 100f)
            background(asRGBA(0, 0, 0, 0.3f), radius(15f))

//            margin(10f)

            font {
                align(UIAlignment.CENTER)
                textAlignment = UIRenderer.ALIGNMIDDLE or UIRenderer.ALIGNCENTER
                fontFamily = "Poppins"
                fontSize = 16f
                fontType = UIFont.FontType.Light
            }
        }

        /** Slider **/
        style(UISliderSheet(), "slider") {
            sliderControl.color = -1

            background {
                radius(2.5f)
                backgroundColor = asRGBA(255, 255, 255, 0.3f)
                border {
                    borderWidth = 1f
                    borderColor = asRGBA(255, 255, 255, 0.75f)
                }
            }
        }

        /** Container w Scrollbar **/
        style(UIContainerSheet(), "container") {

            verticalScrollbar {
                x = rel(1f) - px(10) - px(5)
                y = percent(10)
                width = px(5)
                height = percent(80)
                radius = radius(2.5f)

                color = -1

                background(asRGBA(0, 0, 0, 0.3f)) {
                    radius(2.5f)
                }
            }

            horizontalScrollbar {
                x = percent(10)
                y = rel(1f) - px(10) - px(5)
                width = percent(80)
                width = percent(80)
                height = px(5)
                radius = radius(2.5f)

                color = -1

                background(asRGBA(0, 0, 0, 0.3f)) {
                    radius(2.5f)
                }
            }
        }

        /** Image **/
        style(UIImageSheet(), "imag") {
            width = px(200f)
            height = px(200f)
        }

        // Icon Image
        style(UIImageSheet(), "icon") {
            anchor(UIAlignment.MIDDLELEFT)
            //align(UIAlignment.MIDDLELEFT) // TODO: Debug why this is not updating to it's parent
            size(24f, 24f)
            margin(marginLeft = 16f, marginTop = 23f + 4f)
            imageColor = -1
        }
    }
}