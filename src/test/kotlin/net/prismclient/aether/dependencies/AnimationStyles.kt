package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.interfaces.UIDependable
import net.prismclient.aether.ui.util.extensions.animation
import net.prismclient.aether.ui.util.extensions.asRGBA

/**
 * Example of animations as a [UIDependable]
 */
class AnimationStyles : UIDependable() {
    override fun load() {
        animation(UIStyleSheet(), "testAnimation") {
            keyframe(UIQuart(1000L), true) {
                position(100f, 100f)
                background {
                    color = asRGBA(0f, 0f, 0f, 0.3f)
                    border {
                        borderColor = -1
                        borderWidth = 1f
                    }
                }
            }
        }
    }
}