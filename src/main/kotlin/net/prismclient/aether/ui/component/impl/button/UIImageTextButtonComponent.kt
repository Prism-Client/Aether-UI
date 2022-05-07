//package net.prismclient.aether.ui.component.impl.button
//
//import net.prismclient.aether.ui.component.impl.image.UIImageComponent
//
//class UIImageTextButtonComponent(imageName: String, imageLocation: String = "/images/$imageName", text: String, style: String) : UITextButtonComponent(text, style) {
//    val image = UIImageComponent(imageName, imageLocation, style)
//
//    init {
//        image.parent = this
//    }
//
//    override fun initialize() {
//        super.initialize()
//        image.initialize()
//    }
//
//    override fun update() {
//        super.update()
//        image.update()
//    }
//
//    override fun render() {
//        super.render()
//        image.render()
//    }
//
//    inline fun image(block: UIImageComponent.() -> Unit) = image.block()
//}