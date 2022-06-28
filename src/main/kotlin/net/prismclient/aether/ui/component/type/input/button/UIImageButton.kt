package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.ubuild

/**
 * Like [UIButton], but with an image, or icon.
 *
 * @author sen
 * @since 5/9/2022
 */
class UIImageButton(private val imageName: String, private val imageStyle: String, text: String, style: String?) : UIButton<UIStyleSheet>(text, style) {
    lateinit var image: UIImage

    override fun initialize() {
        ubuild {
            image = image(imageName, style = imageStyle)
        }
        super.initialize()
    }
}