package net.prismclient.profiler

import org.apache.commons.collections4.queue.CircularFifoQueue
import java.util.LinkedList
import java.util.Queue

/**
 * A [Watcher] is a listener which counts the time between two points via an active [Profiler].
 *
 * @author sen
 * @since 5/28/2022
 * @see Profiler
 *
 * @param maxCount The maximum number of times this [Watcher] can be called before the stack starts removing values
 */
class Watcher(val maxCount: Int = 2000) {
    val stack = CircularFifoQueue<Long>(maxCount)
    private var startTime = 0L

    fun begin() {
        startTime = System.nanoTime()
    }

    fun end() {
        stack.add((System.nanoTime() - startTime) / 1000L)
    }

    fun getAverage(): Double {
        var total = 0L
        for (i in 0 until stack.size)
            total += stack.get(i)
        total *= 1000L
        return (total / stack.size.coerceAtLeast(1).toDouble()) / Profiler.activeProfiler!!.totalTime.toDouble()
    }
}