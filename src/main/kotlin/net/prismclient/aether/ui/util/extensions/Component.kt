//package net.prismclient.util.extensions
//
//import net.prismclient.ui.component__.UIComponent
//import net.prismclient.aether.UIAlignment
//import net.prismclient.ui.component__.style.UIStyleSheet
//import net.prismclient.aether.UITheme
//
//inline infix fun <T : UIComponent> T.style(block: UIStyleSheet.() -> Unit) = apply {
//    style.block()
//    update()
//}
//
//infix fun <T : UIComponent> T.style(alignment: UIAlignment) = apply {
//    style.alignment = alignment
//    update()
//}
//
//inline fun <T : UIComponent> T.style(alignment: UIAlignment, block: UIStyleSheet.() -> Unit) = apply {
//    style.alignment = alignment
//    style.block()
//    update()
//}
////
////inline infix fun <T : UIComponent> T.animate(block: UIAnimation.() -> Unit) = apply {
////    animation.block()
////    update()
////}
//
//infix fun <T : UIComponent> T.apply(component: UIComponent) = apply {
//    style.applyStyle(component.style)
//}
//
//inline infix fun <T : UIComponent> T.theme(block: UITheme.() -> Unit) = apply {
//    theme.block()
//}