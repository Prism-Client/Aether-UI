package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.renderer

class ExampleScreen : UIScreen() {
    override fun initialize() {
        UIFontFamily("Poppins", "/demo/fonts/", "regular", "black", "bold", "light", "thin")

        build {
            dependsOn(::TextStyles)
            dependsOn(::IconStyles)
            dependsOn(::ComponentStyles)
            dependsOn(::AnimationStyles)

            renderer{
                loadSvg("checkbox", "/demo/thumbs-up.svg")
            }

            style(UIImageSheet(), "image") {
                control(UIAlignment.CENTER)
                size(24f, 24f)
            }

            button("Text", "btn") {
                style {
                    control(UIAlignment.CENTER)
                    size(48f, 48f)
                    padding {

                    }
                }
            }.onMousePressed {
                if (it.isMouseInsideBoundingBox()) {
                    UIProvider.dispatchAnimation("testAnimation", it)
                }
            }
        }
    }

    override fun render() {
        super.render()
    }
}