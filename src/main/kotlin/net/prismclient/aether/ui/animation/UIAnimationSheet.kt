//package net.prismclient.aether.ui.animation
//
//import net.prismclient.aether.ui.animation.ease.UIEaseDirection
//import net.prismclient.aether.ui.animation.ease.UIEaseType
//import net.prismclient.aether.ui.util.UIAnimationPriority
//
//class UIAnimationSheet(val name: String, val jobPriority: UIAnimationPriority = UIAnimationPriority.NORMAL) {
//    var fromKeyframe = UIAnimationData().also { it.relative(true) }
//    var toKeyframe = UIAnimationData().also { it.relative(true) }
//
//    var retain: Boolean = false
//    var duration: Long = 1000L
//
//    var easeType: UIEaseType = UIEaseType.LINEAR
//    var easeDirection: UIEaseDirection = UIEaseDirection.INOUT
//
//    inline fun from(relative: Boolean = false, block: UIAnimationData.() -> Unit) {
//        fromKeyframe.relative(relative)
//        fromKeyframe.block()
//    }
//
//    inline fun to(relative: Boolean = false, block: UIAnimationData.() -> Unit) {
//        toKeyframe.relative(relative)
//        toKeyframe.block()
//    }
//
//    fun copy(): UIAnimationSheet {
//        val animation = UIAnimationSheet(name, jobPriority)
//
//        animation.fromKeyframe = fromKeyframe.copy()
//        animation.toKeyframe = toKeyframe.copy()
//
//        animation.retain = retain
//        animation.duration = duration
//
//        animation.easeType = easeType
//        animation.easeDirection = easeDirection
//
//        return animation
//    }
//}