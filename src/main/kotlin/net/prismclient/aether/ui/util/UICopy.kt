package net.prismclient.aether.ui.util

/**
 * Returns a new instance of it's self
 *
 * @author sen
 * @since 4/26/2022
 */
interface UICopy<Self> {
    fun copy(): Self
}