//package net.prismclient.aether.ui.component.impl.image
//
//import net.prismclient.aether.ui.component.UIComponent
//import net.prismclient.aether.ui.util.extensions.renderer
//
//open class UIImageComponent(
//    val imageName: String,
//    imageLocation: String = "/images/$imageName.png",
//    style: String
//) : UIComponent(style) {
//    var imageLoaded = false
//        private set
//    var imageRadius = this.style.backgroundRadius
//
//    init {
//        renderer {
//            imageLoaded = loadImage(imageName, imageLocation)
//
//            if (!imageLoaded) {
//                println("$imageName failed to load at $imageLocation.")
//            }
//        }
//    }
//
//    override fun update() {
//        super.update()
//    }
//
//    override fun renderComponent() {
//        renderer {
//            color(style.primaryColor)
//            renderImage(imageName, x, y, width, height, imageRadius)
//        }
//    }
//}