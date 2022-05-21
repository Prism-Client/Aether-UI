package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.component.type.input.slider.UISliderSheet
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.UIDependable
import net.prismclient.aether.ui.util.extensions.*

/**
 * An example of [UIDependable] which has component styles setup
 * for a theme so the same styles does not need to be created multiple times
 */
class ComponentStyles : UIDependable() {
    override fun load() {
        /** Button **/
        style(UIStyleSheet(), "btn") {
            background(asRGBA(0, 0, 0, 0.3f), radius(15f))

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
                radius = radius(2.5f)
                color = asRGBA(255, 255, 255, 0.3f)
                border {
                    borderWidth = 1f
                    borderColor = asRGBA(255, 255, 255, 0.75f)
                }
            }
        }

        /** Container w Scrollbar **/
        style(UIContainerSheet(), "container") {
            control(UIAlignment.CENTER)
            background(asRGBA(0, 0, 0, 0.3f)) {
                radius = radius(40f)
            }
            contentRadius = radius(40f)

            width = px(400)
            height = px(400)

            overflowX = UIContainerSheet.Overflow.Scroll
            overflowY = UIContainerSheet.Overflow.Scroll

            verticalScrollbar {
                x = rel(1f) - px(10) - px(5)
                y = percent(10)
                width = px(5)
                height = percent(80)
                radius = radius(2.5f)

                color = -1

                background(asRGBA(0, 0, 0, 0.3f)) {
                    radius = radius(2.5f)
                }
            }

            horizontalScrollbar {
                x = percent(10)
                y = rel(1f) - px(10) - px(5)
                width = percent(80)
                height = px(5)
                radius = radius(2.5f)

                color = -1

                background(asRGBA(0, 0, 0, 0.3f)) {
                    radius = radius(2.5f)
                }
            }
        }
    }
}