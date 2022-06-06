package net.prismclient.aether.ui.event.propagation

/**
 * [UIPropagatingEvent] is a type of event that is invoked on the deepest component in the
 * hierarchy of components. From there, the event is propagated up the hierarchy.
 * At any point it can be canceled using [cancel] which it will stop propagating.
 *
 * @author sen
 * @since 5/29/2022
 * @param propagationIndex The index of the event in the propagation chain.
 */
abstract class UIPropagatingEvent(var propagationIndex: Int = 0) {
    var canceled = false
        protected set

    open fun cancel() {
        this.canceled = true
    }
}