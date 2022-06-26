package net.prismclient.aether.screens

import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.*

class TestingScreen : UIScreen {
    override fun build() {
        build {
            UIFontFamily("Poppins", "/demo/fonts/", "regular", "medium","black", "bold", "light", "thin")
            renderer {
                loadImage("background", "/prism/background.png")
            }

            style("style") {
                font("Poppins", 14f, -1, UIRenderer.ALIGNTOP or UIRenderer.ALIGNLEFT)
            }
            label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris porttitor vehicula ipsum, lacinia lacinia purus maximus eget. Donec sollicitudin elit sed felis pulvinar, a rhoncus risus pellentesque. Integer non fermentum risus, id varius tortor. Donec posuere metus eget lorem egestas, vel accumsan ipsum rutrum. Mauris elementum sapien sit amet ligula pulvinar accumsan. Morbi ac nulla non dolor sagittis commodo quis ut massa. Vivamus non quam vehicula nibh ultricies lacinia ac ut velit. Nam nec viverra felis. Suspendisse potenti. Nulla sed ligula at dolor viverra euismod. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed id elementum risus. Phasellus convallis diam turpis, id mattis massa porttitor eu. Cras at interdum justo. Ut sit amet felis odio.\n" +
                    "\n" +
                    "Quisque sit amet nibh aliquet, rhoncus orci vel, vehicula nibh. Pellentesque eget ligula et risus dapibus fermentum sit amet a mi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam eget arcu placerat, sollicitudin lorem vitae, porttitor diam. Sed ac nisl ut elit aliquam tincidunt. Sed ut mauris venenatis massa faucibus hendrerit nec non odio. Duis id arcu vitae felis vehicula facilisis vel non nibh.", "style").style {
                position(150f, 150f)
                background(asRGBA(0, 0, 0, 0.1f))
                font {
                    isSelectable = true
                    fontRenderType = UIFont.FontRenderType.WRAP
                    lineBreakWidth = px(400)
                    selectionColor = asRGBA(0, 120, 200, 0.3f)
                    lineHeight = px(5)
                }
                clipContent = false
            }
        }
    }
}