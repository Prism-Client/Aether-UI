package net.prismclient.aether.ui.util.interfaces

/**
 * [UICopy] is a functional interface which expects a new instance of [Self]
 *
 * @author sen
 * @since 4/26/2022
 */
interface UICopy<Self> {
    fun copy(): Self
}