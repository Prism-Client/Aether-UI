package net.prismclient.aether.ui.event.input

import net.prismclient.aether.ui.event.propagation.UIPropagatingEvent

/**
 * [UIMouseEvent] is an event which is fired when a mouse button
 * is pressed or released.
 *
 * @author sen
 * @since 5/29/2022
 * @param button The button on the mouse which was pressed or release. 0 is left, 1 is right, 2 is middle.
 * @param isRelease Whether the button was released or pressed.
 * @param clickCount The amount of times the button has been pressed within half a second (500ms). NOT YET FUNCTIONAL
 */
class UIMouseEvent(val mouseX: Float, val mouseY: Float, val button: Int, val isRelease: Boolean, val clickCount: Int) : UIPropagatingEvent()