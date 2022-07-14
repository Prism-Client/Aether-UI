package examples

import net.prismclient.aether.ui.component.type.layout.UIListLayout
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.*

class Default : UIScreen {
    override fun build() {
        create {
            autoLayout(UIListLayout.ListDirection.Vertical) {

            }
        }
    }
}