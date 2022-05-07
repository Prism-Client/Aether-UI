package net.prismclient.aether.ui.renderer.image

import net.prismclient.aether.ui.renderer.builder.UIRendererDSL

/**
 * A class which holds an image, along with it's data
 */
// TODO: Store width and height or something lel or dont and just use nanovg calls or not idk
class UIImage(val imageName: String, val imageLocation: String, val flags: Int = 0) {
    constructor(imageName: String, flags: Int = 0) : this(imageName, "/images/$imageName.png")

    var loaded: Boolean = false
        private set

    init {
        UIRendererDSL.instance.loadImage(imageName, imageLocation, flags)
    }
}