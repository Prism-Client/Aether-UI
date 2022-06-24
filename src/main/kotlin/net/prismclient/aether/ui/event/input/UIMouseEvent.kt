package net.prismclient.aether.ui.event.input

import net.prismclient.aether.ui.event.propagation.UIPropagatingEvent

/**
 * [UIMouseEvent] is an event which is fired when a mouse button
 * is pressed or released.
 *
 * @author sen
 * @since 5/29/2022
 * @param button The button on the mouse which was pressed or release. 0 is left, 1 is right, 2 is middle.
 * @param clickCount The amount of times the button has been pressed within half a second (500ms). UICore only supports
 * the left mouse button (button = 0) by default. You have to modify the implementation by extending the class if you
 * want to handle other mouseButton counts.
 */
class UIMouseEvent(val mouseX: Float, val mouseY: Float, val button: Int, val clickCount: Int) : UIPropagatingEvent()