package net.prismclient.aether.ui.util.interfaces

/**
 * Represents a functional interface like Function or BiFunction, except expects 3
 * parameters, [A], [B], and [C] and expects [T] to be returned.
 *
 * @author sen
 * @since 1.3
 */
fun interface UITriFunction<A, B, C, T> {
    /**
     * Invokes with the given parameters [A], [B], and [C], and returns [T].
     */
    fun invoke(a: A, b: B, c: C): T
}