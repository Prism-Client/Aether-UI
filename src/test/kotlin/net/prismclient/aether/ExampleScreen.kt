package net.prismclient.aether

import net.prismclient.aether.dependencies.AnimationStyles
import net.prismclient.aether.dependencies.ComponentStyles
import net.prismclient.aether.dependencies.IconStyles
import net.prismclient.aether.dependencies.TextStyles
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily

class ExampleScreen : UIScreen() {
    override fun initialize() {
        UIFontFamily("Poppins", "/fonts/", "regular", "black", "bold", "light", "thin")

        build {
            dependsOn(::TextStyles)
            dependsOn(::IconStyles)
            dependsOn(::ComponentStyles)
            dependsOn(::AnimationStyles)

            component(
                UIListLayout(
                    UIListLayout.ListDirection.Horizontal,
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
    }
}