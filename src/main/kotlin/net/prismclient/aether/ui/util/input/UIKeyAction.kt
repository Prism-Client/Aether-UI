package net.prismclient.aether.ui.util.input

/**
 * [UIKeyAction] describes the action of a key press. See
 * the respected enums for documentation.
 *
 * @author sen
 * @since 5/13/2022
 */
enum class UIKeyAction {
    /**
     * The initial press of any key
     */
    PRESS,

    /**
     * The release of a key that was pressed
     */
    RELEASE,

    /**
     * Like key press, excepts that the key is held, and it is being invoked again.
     */
    REPEAT
}