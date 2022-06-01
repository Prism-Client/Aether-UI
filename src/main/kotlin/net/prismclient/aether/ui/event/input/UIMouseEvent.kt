package net.prismclient.aether.ui.event.input

import net.prismclient.aether.ui.event.propagation.UIPropagatingEvent

/**
 * [UIMouseEvent] is an event which is fired when a mouse button
 * is pressed or released.
 *
 * @author sen
 * @since 5/29/2022
 */
class UIMouseEvent(val mouseX: Float, val mouseY: Float, val button: Int, val clickCount: Int) : UIPropagatingEvent()