//package net.prismclient.aether.ui.animation
//
//import net.prismclient.aether.ui.unit_.impl.UIRelativePixel
//
//// TODO: Initialized variables
//class UIAnimationData {
//
//    var x = UIRelativePixel()
//    var y = UIRelativePixel()
//    var width = UIRelativePixel()
//    var height = UIRelativePixel()
//    var backgroundColor = -1
//
//    init {
//        width.by(1f)
//        height.by(1f)
//        width.relative = true
//        height.relative = true
//    }
//
//    fun relative(isRelative: Boolean) {
//        x.relative = isRelative
//        y.relative = isRelative
//        width.relative = isRelative
//        height.relative = isRelative
//    }
//
//    fun copy(): UIAnimationData {
//        val anim = UIAnimationData()
//
//        anim.x = copy(x)
//        anim.y = copy(y)
//        anim.width = copy(width)
//        anim.height = copy(height)
//        anim.backgroundColor = backgroundColor
//
//        return anim
//    }
//
//    private fun copy(pixel: UIRelativePixel): UIRelativePixel = UIRelativePixel(pixel.value, pixel.type, pixel.relative)
//}