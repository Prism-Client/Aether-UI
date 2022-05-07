package net.prismclient.aether.components.mod

//import net.prismclient.aether.ui.component.UIComponent
//import net.prismclient.aether.ui.component.impl.image.UIImageComponent
//import net.prismclient.aether.ui.util.extensions.renderer

//class ModuleUIComponent(style: String) : UIComponent(style) {
//    val image = UIImageComponent("mouse", style = "img").also {
//        it.parent = this
//    }
//
//    override fun update() {
//        super.update()
//        image.update()
//    }
//
//    override fun renderComponent() {
//        renderer {
//            image.render()
//            color(style.primaryColor)
//            rect(relX, relY + relHeight - 40f, relWidth, 40f, 0f, 0f, style.backgroundRadius, style.backgroundRadius)
//        }
//    }
//}