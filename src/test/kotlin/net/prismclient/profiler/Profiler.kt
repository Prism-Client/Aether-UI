package net.prismclient.profiler

/**
 * [Profiler] is a simple profiler
 *
 * @author sen
 * @since 5/28/2022
 * @see Watcher
 */
class Profiler {
    val watchers = mutableMapOf<String, Watcher>()
    var activeWatcher: Watcher? = null
    var profileActive = false
    var totalTime = 0L

    /**
     * Begins a watcher of [name] if name is null, it will automatically
     * be created. To stop the watcher, either invoke [endProfile] or start
     * a new watch by invoking this again.
     *
     * @param maxCount See the Watcher class
     * @see Watcher
     * @return The new watcher
     */
    fun watch(name: String, maxCount: Int = 2000): Watcher {
        if (!profileActive) {
            profileActive = true
            totalTime = System.nanoTime()
        }
        activeWatcher?.end()
        activeWatcher = watchers.computeIfAbsent(name) {
            Watcher(maxCount)
        }
        activeWatcher!!.begin()
        return activeWatcher!!
    }

    /**
     * Ends the current watch, and removes this as the active profiler.
     */
    fun endWatch() {
        activeWatcher?.end()
        activeWatcher = null
        profileActive = false
        totalTime = 0L
        watchers.forEach { _, u ->
            u.stack.clear()
        }
        endProfile()
    }

    companion object {
        /**
         * The list of all active profilers
         */
        @JvmStatic
        val profilers = mutableMapOf<String, Profiler>()

        /**
         * The active profiler
         */
        @JvmStatic
        var activeProfiler: Profiler? = null

        /**
         * Begins the profiler of the given name
         */
        fun profile(name: String): Profiler {
            activeProfiler = profilers.computeIfAbsent(name) {
                Profiler()
            }
            return activeProfiler!!
        }

        /**
         * Ends the profiler of the given name
         */
        fun endProfile(): Profiler {
            return activeProfiler!!.also {
                activeProfiler = null
            }
        }
    }
}