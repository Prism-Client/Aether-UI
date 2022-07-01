package net.prismclient.aether.ui.component.type.layout.auto

import net.prismclient.aether.ui.component.type.layout.styles.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment

/**
 * [UIAutoLayoutSheet] is the corresponding style sheet to [UIAutoLayout]. See
 * the field documentation for more information.
 *
 * @author sen
 * @since 1.0
 */
class UIAutoLayoutSheet(name: String) : UIContainerSheet(name) {
    override fun copy(): UIAutoLayoutSheet = UIAutoLayoutSheet(name).also {
        it.apply(this)
    }
}