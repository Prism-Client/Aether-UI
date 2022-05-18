package net.prismclient.aether.ui.style.impl

import net.prismclient.aether.ui.renderer.image.UIImageType
import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIImageSheet] represents a sheet with an image
 *
 * @author sen
 * @since 12/5/2022
 */
class UIImageSheet : UIStyleSheet() {
    lateinit var image: UIImageType
}