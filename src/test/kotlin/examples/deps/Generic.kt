package examples.deps

import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.dsl.UIComponentDSL
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.extensions.colorOf
import net.prismclient.aether.ui.util.extensions.px
import net.prismclient.aether.ui.util.interfaces.UIDependable
import net.prismclient.aether.ui.util.left
import net.prismclient.aether.ui.util.style
import net.prismclient.aether.ui.util.top

/**
 * This is an example of a (depend/include)-able file. In essence, [UIDependable] is a single function
 * interface which is invoked immediately after [UIComponentDSL.include]. The entire purpose is to be able
 * to reuse styles (and other properties) across screens. An alternative is simply making the style immutable,
 * so it will not be cleared from memory after the screen is closed.
 *
 * The styles here are reused between different example screens.
 *
 * @author sen
 * @since 7/3/2022
 * @see UIStyleSheet.immutableStyle Set to true to make the style immutable
 */
class Generic : UIDependable {
    override fun load() {
        // Create the Montserrat font family
        UIFontFamily(
            "Montserrat",
            "/prism/fonts/montserrat/",
            "Montserrat-regular",
            "Montserrat-medium",
            "Montserrat-black",
            "Montserrat-bold",
            "Montserrat-light",
            "Montserrat-thin"
        )

        // Load some assets into memory. Aether is intended to support mainly JPEG, PNG, and SVG.
        // Either explicitly state the type
        // Reference the image with the name "ui"
        //UIAssetDSL.svg("ui", "/prism/icons/navbar/ui.svg")
        // loadImage()
        // or let Aether figure it out
        // assumeLoadImage()

        // A 24x24 icon
        UIImageSheet().style("icon24x") {
            size(24, 24)
        }

        UIStyleSheet().style("generic-font") {
            // FontFamily to Montserrat
            // FontSize -> 16f
            // FontColor -> -1 = asRGBA(255, 255, 255) (aka white)
            // TextAlignment -> How the text aligns relative to the screen using bit shifting
            font("Montserrat", px(16f), colorOf(-1), left or top)

            // The above was a shorthand for
            font {
                fontFamily = "Montserrat"
                // etc...
            }
        }
    }
}