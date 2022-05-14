//package net.prismclient.aether.ui.component.impl.container.list
//
//import net.prismclient.aether.ui.component.impl.container.UIContainer
//import net.prismclient.aether.ui.util.extensions.px
//
///**
// * Like a container, except with each child's x below the previous
// */
//class UIHorizontalListComponent(style: String) : UIContainer(style) {
//    var spacing = px(0)
//
//    override fun updateLayout() {
//        super.updateLayout()
//        var w = 0f
//
//        for (child in children) {
//            child.overrided = true
//            child.updatePosition()
//            child.updateSize()
//            child.x = x + w
//            w += child.relWidth + child.horizontalPixels(spacing)
//            child.update()
//        }
//    }
//}