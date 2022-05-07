package net.prismclient.aether.ui.renderer.builder

import net.prismclient.aether.ui.style.UIProvider
import net.prismclient.aether.ui.style.UIStyleSheet

object UIStyle {
    @JvmOverloads
    inline fun String.style(name: String = "", block: UIStyleSheet.() -> Unit = {}): UIStyleSheet {
        return (if (name.isEmpty()) UIStyleSheet()
        else UIProvider.getStyle(name).copy()).also { it.name = this }.also(block)
    }

//    @JvmOverloads
//    inline fun String.animation(
//        retain: Boolean = false,
//        priority: UIAnimationPriority = UIAnimationPriority.NORMAL,
//        name: String = "",
//        block: UIAnimationSheet.() -> Unit = {}
//    ): UIAnimationSheet {
//        return (if (name.isEmpty()) UIAnimationSheet(this, priority)
//        else UIProvider.getAnimation(this).copy()).also {
//            it.retain = retain
//            UIProvider.addAnimation(this, it)
//        }.also(block)
//    }
}