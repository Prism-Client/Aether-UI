package net.prismclient.aether.ui.animation

/**
 * [UIAnimationPriority] describes the priority of a dispatched
 * animation. For more information on what each priority does, refer
 * to the individual enum documentation.
 *
 * @author sen
 * @since 5/26/2022
 */
enum class UIAnimationPriority {
    /**
     * Overrides any active animation, regardless of the active animation job priority
     */
    HIGHEST,

    /**
     * Overrides if the active animation is below this priority
     */
    HIGH,

    /**
     * Awaits for the active animation to finish before animating
     */
    NORMAL,

    /**
     * Dropped if there is an active animation. Useful for hover animations
     */
    LOWEST,
}