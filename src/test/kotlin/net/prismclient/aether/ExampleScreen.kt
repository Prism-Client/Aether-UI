package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNCENTER
import net.prismclient.aether.ui.renderer.UIRenderer.Properties.ALIGNMIDDLE
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.align
import net.prismclient.aether.ui.util.extensions.renderer

class ExampleScreen : UIScreen() {
    override fun initialize() {
        UIFontFamily("Poppins", "/demo/fonts/", "regular", "black", "bold", "light", "thin")

        build {
            dependsOn(::TextStyles)
            dependsOn(::IconStyles)
            dependsOn(::ComponentStyles)
            dependsOn(::AnimationStyles)

            container("container") {
                h1("Text") {
                    style {
                        align(UIAlignment.CENTER)
                        font {
                            textAlignment = ALIGNCENTER or ALIGNMIDDLE
                        }
                    }
                }
            }
        }
    }

    override fun render() {
        super.render()
        renderer {

        }
    }
}