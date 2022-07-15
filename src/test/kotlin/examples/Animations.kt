package examples

import examples.deps.Generic
import net.prismclient.aether.ui.animation.ease.impl.UILinear
import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.asRGBA
import net.prismclient.aether.ui.util.extensions.colorOf
import net.prismclient.aether.ui.util.extensions.px

/**
 * Animations give life to your UI. Any style sheet properties can be animated to do
 * something.
 *
 * @author sen
 * @since 7/5/2022
 */
class Animations : UIScreen {
    override fun build() {
        create {
            include(Generic())

            UIStyleSheet("button").style {
                align(UIAlignment.CENTER)
                size(400, 40)
                background(colorOf(asRGBA(0f, 0f, 0f, 0.3f)), radiusOf(0f)) {
                    border {
                        borderWidth = px(10)
                        borderColor = colorOf(asRGBA(255, 0, 0))
                    }
                }
                font("Montserrat", px(16f), colorOf(-1), left or top)
            }

            animationOf("someAnimation", UIStyleSheet()) {
                UIQuart(1000L) repeat {
                    kf {}
                    kf {
                        position(50, 0)
                    }
                    kf {
                        position(0, 0)
                    }
                    kf {
                        position(0, 50)
                    }
                    kf {
                        position(50, 50)
                    }
                }
                onCompletion {
                    UIProvider.dispatchAnimation("someAnimation", it.component)
                }
            }

            animationOf("test", UIStyleSheet()) {
                kf {}
                UILinear(1000L) to {
                    background {
//                        backgroundColor = colorOf(asRGBA(0, 0, 255))
                        border {
                            borderColor = colorOf(asRGBA(0f, 1f, 0f))
                            borderWidth = px(1)
                        }
                    }
                }
            }

            animationOf("move", UIStyleSheet()) {
                kf {}
                kf {
                    x = px(50)
                }
            }

            val l = label("Some text", "button").onMousePressed {
                UIProvider.dispatchAnimation("test", it)
                println("pressed")
            }.style {
                x = px(-50)
            }
            UIProvider.dispatchAnimation("move", l)
        }
    }
}