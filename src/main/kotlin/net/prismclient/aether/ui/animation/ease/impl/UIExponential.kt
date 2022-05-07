package net.prismclient.aether.ui.animation.ease.impl

import net.prismclient.aether.ui.animation.ease.UIEase
import net.prismclient.aether.ui.animation.ease.UIEaseDirection
import kotlin.math.pow

class UIExponential(duration: Long = 1000L, animationDirection: UIEaseDirection = UIEaseDirection.INOUT) : UIEase(duration, animationDirection) {
    override fun getValue(): Double {
        val x = this.get().toDouble()

        return when (animationDirection) {
            UIEaseDirection.IN -> if (x == 0.0) 0.0 else 2.0.pow(10 * x - 10)
            UIEaseDirection.OUT -> if (x == 1.0) 1.0 else 1.0 - 2.0.pow(-10 * x)
            UIEaseDirection.INOUT -> if (x == 0.0) {
                0.0
            } else {
                if (x == 1.0) {
                    1.0
                } else if (x < 0.5) {
                    2.0.pow(20.0 * x - 10.0) / 2.0
                } else {
                    (2.0 - 2.0.pow(-20.0 * x + 10.0)) / 2.0
                }
            }
        }
    }

    override fun copy(): UIEase = UIExponential(duration, animationDirection)
}