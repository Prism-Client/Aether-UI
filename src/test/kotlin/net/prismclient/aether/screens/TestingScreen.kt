package net.prismclient.aether.screens

import net.prismclient.aether.ui.screen.UIScreen

class TestingScreen : UIScreen {
    override fun build() {
        println("no peeking")
    }
}