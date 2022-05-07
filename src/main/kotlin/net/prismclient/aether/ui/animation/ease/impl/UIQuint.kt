package net.prismclient.aether.ui.animation.ease.impl

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.UIEaseDirection
import kotlin.math.pow

class UIQuint(duration: Long = 1000L, animationDirection: UIEaseDirection = UIEaseDirection.INOUT) : UIEase(duration, animationDirection) {
    override fun getValue(): Double {
        val x = this.get().toDouble()

        return when (animationDirection) {
            UIEaseDirection.IN ->  x * x * x * x * x
            UIEaseDirection.OUT -> 1.0 - (1 - x).pow(5)
            UIEaseDirection.INOUT -> if (x < 0.5) 16.0 * x * x * x * x * x else 1.0 - (-2 * x + 2).pow(5) / 2.0
        }
    }

    override fun copy(): UIEase = UIQuint(duration, animationDirection)
}