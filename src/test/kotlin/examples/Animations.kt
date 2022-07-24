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
            button("Hello").style {

            }

            verticalList {

            }.style {

            }
        }
    }
}