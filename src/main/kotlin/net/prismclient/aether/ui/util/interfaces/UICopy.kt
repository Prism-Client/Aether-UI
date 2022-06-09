package net.prismclient.aether.ui.util.interfaces

/**
 * [UICopy] is an interface which is used to return a deep copy of whatever class implements this.
 *
 * @author sen
 * @since 4/26/2022
 */
interface UICopy<Self> {
    /**
     * Returns a deep copy; a new instance of [Self], with every property identical
     * to the original, without the original references.
     */
    fun copy(): Self
}