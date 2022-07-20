package net.prismclient.aether.ui

class Timings {

    /**
     * Time when the current frame render started in milliseconds
     */
    var frameRenderStartTime = 0L
        private set

    /**
     * Time when the last frame render started in milliseconds
     */
    var lastFrameRenderStartTime = 0L
        private set

    /**
     * Time when the current frame render ended in milliseconds
     */
    var frameRenderEndTime = 0L
        private set

    /**
     * Time when the last frame render ended in milliseconds
     */
    var lastFrameRenderEndTime = 0L
        private set

    /**
     * The delta time (frame render start - frame render end) of the current frame in milliseconds
     */
    val frameRenderDeltaTime
        get() = frameRenderEndTime - frameRenderStartTime

    /**
     * The delta time (frame render start - frame render end) of the current frame in seconds
     */
    val deltaFrameRenderTimeSeconds
        get() = frameRenderDeltaTime / 1000.0

    /**
     * The delta time (frame render start - frame render end) of the last frame in milliseconds
     */
    val lastFrameRenderDeltaTime
        get() = lastFrameRenderEndTime - lastFrameRenderStartTime

    /**
     * The delta time (frame render start - frame render end) of the last frame in seconds
     */
    val lastFrameRenderDeltaTimeSeconds
        get() = lastFrameRenderDeltaTime / 1000.0

    /**
     * The approximate amount of renders the last frame would have made in a second
     */
    val lastFrameRate
        get() = 1000.0 / lastFrameRenderDeltaTime

    /**
     * The approximate amount of renders the current frame would have made in a second
     */
    val frameRate
        get() = (1000 / (frameRenderDeltaTime + 1)).toInt()

    /**
     * Set current & last render start times
     */
    fun onFrameRenderStart() {
        lastFrameRenderStartTime = frameRenderStartTime

        frameRenderStartTime = System.currentTimeMillis()
    }

    /**
     * Set current & last render end times
     */
    fun onFrameRenderEnd() {
        lastFrameRenderEndTime = frameRenderEndTime

        frameRenderEndTime = System.currentTimeMillis()
    }

}