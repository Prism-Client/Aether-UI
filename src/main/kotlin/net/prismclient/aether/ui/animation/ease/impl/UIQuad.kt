package net.prismclient.aether.ui.animation.ease.impl

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.UIEaseDirection
import kotlin.math.pow

class UIQuad(duration: Long = 1000L, animationDirection: UIEaseDirection = UIEaseDirection.INOUT) :
    UIEase(duration, animationDirection) {
    override fun getValue(): Double {
        val x = this.get().toDouble()

        return when (animationDirection) {
            UIEaseDirection.IN -> x * x
            UIEaseDirection.OUT -> 1.0 - (1.0 - x) * (1.0 - x)
            UIEaseDirection.INOUT -> if (x < 0.5) 2.0 * x * x else 1.0 - (-2.0 * x + 2.0).pow(2.0) / 2.0
        }
    }

    override fun copy(): UIEase = UIQuad(duration, animationDirection)
}