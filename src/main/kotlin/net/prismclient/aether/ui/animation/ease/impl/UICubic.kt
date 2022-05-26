package net.prismclient.aether.ui.animation.ease.impl

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.UIEaseDirection
import kotlin.math.pow

class UICubic(duration: Long = 1000L, animationDirection: UIEaseDirection = UIEaseDirection.INOUT) :
    UIEase(duration, animationDirection) {
    override fun getValue(): Double {
        val x = this.get().toDouble()

        return when (animationDirection) {
            UIEaseDirection.IN -> x * x * x
            UIEaseDirection.OUT -> 1 - (1 - x).pow(3.0)
            UIEaseDirection.INOUT -> if (x < 0.5) 4 * x * x * x else 1 - (-2 * x + 2).pow(3) / 2.0
        }
    }

    override fun copy(): UIEase = UICubic(duration, animationDirection)
}