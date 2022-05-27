package net.prismclient.aether.ui.util.interfaces

import net.prismclient.aether.ui.screen.UIScreen

/**
 * [UIDependable] allows for loading of styles, assets, and other
 * things which a screen can depend on when created. It is intended
 * to reduce the amount of boilerplate code and excessive amounts of
 * styles in a [UIScreen].
 *
 * @author sen
 * @since 5/20/2022
 */
abstract class UIDependable {
    abstract fun load()
}