package net.prismclient.aether.ui.animation.ease.impl

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.UIEaseDirection
import kotlin.math.pow
import kotlin.math.sqrt

class UICircle(duration: Long = 1000L, animationDirection: UIEaseDirection = UIEaseDirection.INOUT) : UIEase(duration, animationDirection) {
    override fun getValue(): Double {
        val x = this.get().toDouble()

        return when (animationDirection) {
            UIEaseDirection.IN -> 1.0 - sqrt(1.0 - x.pow(2))
            UIEaseDirection.OUT -> sqrt(1.0 - (x - 1.0).pow(2))
            UIEaseDirection.INOUT -> if (x < 0.5) (1.0 - sqrt(1.0 - (2.0 * x).pow(2))) / 2.0 else (sqrt(1.0 - (-2.0 * x + 2.0).pow(2)) + 1.0) / 2.0
        }
    }

    override fun copy(): UIEase = UICircle(duration, animationDirection)
}