package examples

import examples.deps.Generic
import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayoutSheet
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*

class Default : UIScreen {

    var navbarButtonLayout: UIAutoLayout? = null
    var editHudButtonLayout: UIAutoLayout? = null

    override fun build() {
        create {
            styleOf(UIStyleSheet("btn")) {
                size(200, 200)
                background(colorOf(1f, 0f, 0f ,1f), radiusOf(25f))
            }

            val list = list(UIListLayout.ListDirection.Vertical) {
                button("Some text", "btn")
                button("Some text", "btn")
                button("Some text", "btn")
                button("Some text", "btn")
            }.style(UIContainerSheet("")) {
                size(500, 500)
                background(colorOf(0f, 0f, 1f, 1f))
                useFBO = true
            }

            animationOf("someAnimation", UIContainerSheet()) {
                kf {
                    x = px(0)
                }
                UIQuart(1000L) to {
                    x = px(200)
                }
                UIQuart(1000L) to {
                    x = px(0)
                }
                onCompletion {
                    UIProvider.dispatchAnimation("someAnimation", it.component)
                }
            }
            UIProvider.dispatchAnimation("someAnimation", list)
        }
    }

    /**
     * Creates a navbar button
     */
    private fun navButton(buttonText: String, imageName: String) {
        ucreate {
            autoLayout(navbarButtonLayout!!) {
                ignore {
                    image(imageName, "icon24x")
                    text(buttonText, "generic-font")
                }
            }
        }
    }
}