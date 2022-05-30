package net.prismclient.aether.ui.component.propagation

/**
 * [UIPropagatingEvent] is a type of event that is invoked on the deepest component in the
 * hierarchy of components. From there, the event is propagated up the hierarchy.
 * At any point it can be canceled using [cancel] which it will stop propagating.
 *
 * @author sen
 * @since 5/29/2022
 */
abstract class UIPropagatingEvent {
    var canceled = false
        protected set

    open fun cancel() {
        this.canceled = true
    }
}