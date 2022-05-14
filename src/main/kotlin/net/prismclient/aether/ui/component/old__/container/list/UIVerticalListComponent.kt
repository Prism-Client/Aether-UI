//package net.prismclient.aether.ui.component.impl.container.list
//
//import net.prismclient.aether.ui.component.impl.container.UIContainer
//import net.prismclient.aether.ui.util.extensions.px
//
///**
// * Like a container, except with each child's y below the previous
// */
//class UIVerticalListComponent(style: String) : UIContainer(style) {
//    var spacing = px(0)
//
//    override fun updateLayout() {
//        super.updateLayout()
//        var h = 0f
//
//        for (child in children) {
//            child.overrided = true
//            child.updatePosition()
//            child.updateSize()
//            child.y = y + h
//            h += child.relHeight + child.verticalPixels(spacing)
//            child.update() // Some components might need to update any children
//        }
//    }
//}