package net.prismclient.aether.ui.component.propagation

/**
 * @author sen
 * @since 5/29/2022
 */
class UIMouseEvent(val mouseX: Float, val mouseY: Float, val clickCount: Int) : UIPropagatingEvent()

// TODO: Click count