package net.prismclient.aether.dependencies

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
            keyframe {
                background {
                    color = asRGBA(255, 0, 0)
                }
            }

            keyframe {
                background {
                    color = asRGBA(0, 255, 0)
                }
            }
        }
    }
}