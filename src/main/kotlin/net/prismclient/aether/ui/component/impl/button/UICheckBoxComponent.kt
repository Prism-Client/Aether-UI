//package net.prismclient.aether.ui.component.impl.button
//
//import net.prismclient.aether.ui.component.impl.image.UIImageComponent
//
//class UICheckBoxComponent(
//    imageName: String,
//    imageLocation: String = "/images/$imageName.png",
//    style: String
//) : UIImageComponent(imageName, imageLocation, style) {
//    var checked: Boolean = false
//
//    override fun mouseClicked(mouseX: Float, mouseY: Float) {
//        super.mouseClicked(mouseX, mouseY)
//        if (isMouseInside(mouseX, mouseY)) {
//            checked = !checked
//        }
//    }
//
//    override fun renderComponent() {
//        if (checked) {
//            super.renderComponent()
//        }
//    }
//}