package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.BEVEL
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.BUTT
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.MITER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ROUND
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.SQUARE
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.renderer

class ExampleScreen : UIScreen() {
    override fun initialize() {
        UIFontFamily("Poppins", "/fonts/", "regular", "black", "bold", "light", "thin")

        build {
            dependsOn(::TextStyles)
            dependsOn(::IconStyles)
            dependsOn(::ComponentStyles)
            dependsOn(::AnimationStyles)

            button("Some button", "btn")

            component(
                UIListLayout(
                    UIListLayout.ListDirection.Vertical,
                    UIListLayout.ListOrientation.Forward,
                    "container"
                )
            ) {
                applyStyle("btn") {
                    for (i in 0 .. 25) {
                        button("button: $i")
                            .hover("fadeIn", "fadeOut")
                    }
                }
            }
        }
    }

    override fun render() {
        super.render()
        renderer {
            color(-1)
            line(50f, 50f, SQUARE, ROUND, 4f) {
                line(100f, 150f)
                line(200f, 200f)

                bezier(10f, 10f, 50f, 50f, 150f, 50f)
            }
        }
    }
}