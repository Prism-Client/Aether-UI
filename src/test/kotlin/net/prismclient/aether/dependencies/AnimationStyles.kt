package net.prismclient.aether.dependencies

import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.interfaces.UIDependable
import net.prismclient.aether.ui.util.extensions.animation

/**
 * Example of animations as a [UIDependable]
 */
class AnimationStyles : UIDependable() {
    override fun load() {
        animation(UIStyleSheet(), "testAnimation") {
            keyframe {
                font {
                    fontSize = 16f
                }
            }

            keyframe(UIQuart(1000L)) {
                font {
                    fontSize = 32f
                }
            }
        }
    }
}