package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * Like [UIButton], but there is a reference, [image] to the image.
 */
class UIImageButton(text: String, style: String) : UIButton<UIStyleSheet>(text, style) {
    lateinit var image: UIImage
}