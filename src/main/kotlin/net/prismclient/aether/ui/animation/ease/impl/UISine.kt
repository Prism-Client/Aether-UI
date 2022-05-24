package net.prismclient.aether.ui.animation.ease.impl

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.UIEaseDirection
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class UISine(duration: Long = 1000L, animationDirection: UIEaseDirection = UIEaseDirection.INOUT) :
    UIEase(duration, animationDirection) {
    override fun getValue(): Double {
        val x = this.get().toDouble()

        return when (animationDirection) {
            UIEaseDirection.IN -> 1.0 - cos((x * PI) / 2.0)
            UIEaseDirection.OUT -> sin((x * PI) / 2.0)
            UIEaseDirection.INOUT -> -(cos(PI * x) - 1.0) / 2.0
        }
    }

    override fun copy(): UIEase = UISine(duration, animationDirection)
}