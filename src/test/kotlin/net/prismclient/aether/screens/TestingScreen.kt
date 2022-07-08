package net.prismclient.aether.screens

import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayoutSheet
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.util.create
import net.prismclient.aether.ui.util.style

class TestingScreen : UIScreen {
    override fun build() {
        create {
            val navbarLayout: UIAutoLayout = UIAutoLayout(UIListLayout.ListDirection.Horizontal, null)
                .style(UIAutoLayoutSheet("navlayout")) {

                }.apply {

                }
        }
    }
}