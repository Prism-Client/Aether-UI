package net.prismclient.aether.ui.animation.ease

import net.prismclient.aether.ui.util.interfaces.UICopy
import net.prismclient.aether.ui.animation.ease.UIEaseDirection.*
import net.prismclient.aether.ui.animation.ease.UIEaseType.*
import net.prismclient.aether.ui.animation.ease.impl.*

abstract class UIEase(var duration: Long = 1000L, var animationDirection: UIEaseDirection = INOUT) : UICopy<UIEase> {
    var animating: Boolean = false
    var finished: Boolean = false
    var startTime: Long = 0L
    var endTime: Long = 0L
    var delay: Long = 0L

    fun start() {
        startTime = System.currentTimeMillis() + delay
        endTime = startTime + duration
        animating = true
        finished = false
    }

    private fun finish() {
        animating = false
        finished = true
    }

    @JvmOverloads
    fun reset(duration: Long = this.duration) {
        this.duration = duration
        startTime = 0L
        endTime = 0L
        animating = false
        finished = false
    }

    protected fun get(): Float {
        if (!animating && !finished) return 0f
        if (finished) return 1f

        return ((System.currentTimeMillis() - startTime).toFloat() / duration.toFloat())
            .coerceAtMost(1f)
            .coerceAtLeast(0f)
            .also {
                if (it >= 1f) {
                    finish()
                }
            }
    }

    abstract fun getValue(): Double

    companion object {
        fun typeFromString(easeType: String): UIEaseType {
            return when (easeType.lowercase()) {
                "linear" -> LINEAR
                "sine" -> SINE
                "quad" -> QUAD
                "cubic" -> CUBIC
                "quart" -> QUART
                "quint" -> QUINT
                "expo", "exponential" -> EXPO
                "circ", "circle" -> CIRC
                else -> LINEAR
            }
        }

        fun directionFromString(easeDirection: String): UIEaseDirection {
            return when (easeDirection.lowercase()) {
                "in" -> IN
                "out" -> OUT
                "inout" -> INOUT
                else -> INOUT
            }
        }

        fun getEase(duration: Long = 1000L, easeType: UIEaseType, easeDirection: UIEaseDirection = INOUT): UIEase {
            return when (easeType) {
                LINEAR -> UILinear(duration, easeDirection)
                SINE -> UISine(duration, easeDirection)
                QUAD -> UIQuad(duration, easeDirection)
                CUBIC -> UICubic(duration, easeDirection)
                QUART -> UIQuart(duration, easeDirection)
                QUINT -> UIQuint(duration, easeDirection)
                EXPO -> UIExponential(duration, easeDirection)
                CIRC -> UICircle(duration, easeDirection)
            }
        }
    }
}