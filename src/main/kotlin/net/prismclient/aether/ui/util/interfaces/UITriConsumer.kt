package net.prismclient.aether.ui.util.interfaces

import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * [UITriConsumer] is like a normal [Consumer] or [BiConsumer] except it accepts three
 * arguments instead of one or two.
 *
 * @author sen
 * @since 6/5/2022
 */
fun interface UITriConsumer<A, B, C> {
    fun accept(a: A, b: B, c: C)

    @JvmDefault
    fun andThen(after: UITriConsumer<in A, in B, in C>): UITriConsumer<A, B, C>? {
        return UITriConsumer { a: A, b: B, c: C ->
            accept(a, b, c)
            after.accept(a, b, c)
        }
    }
}