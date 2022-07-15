package examples

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*

class Default : UIScreen {
    override fun build() {
        create {
            styleOf(UIStyleSheet("btn")) {
                size(200, 200)
                background(colorOf(1f, 0f, 0f, 0.3f), radiusOf(25f))
            }

            val list = list(UIListLayout.ListDirection.Vertical) {
                button("Some text", "btn")
                button("Some text", "btn")
                button("Some text", "btn")
                button("Some text", "btn")
            }.style(UIContainerSheet("")) {
                control(UIAlignment.CENTER)
                size(500, 500)
                background(colorOf(-1), radiusOf(8f))
                useFBO = true
                verticalScrollbar {
                    background {
                        backgroundColor = colorOf(0f, 0f, 0f ,0.3f)
                    }
                    y = rel(0.1)
                    x = rel(1) - px(10)
                    height = rel(0.8)
                    width = px(5)
                    color = colorOf(48, 48, 48)
                    radius = radiusOf(2.5f)
                }
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
//            UIProvider.dispatchAnimation("someAnimation", list)
        }
    }
}