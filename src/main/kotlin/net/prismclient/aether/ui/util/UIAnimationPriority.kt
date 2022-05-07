package net.prismclient.aether.ui.util

enum class UIAnimationPriority {
    HIGHEST, /* Overrides any active animation, regardless of the active animation job priority */
    HIGH, /* Overrides if the active animation is below this priority */
    NORMAL, /* Awaits for the active animation to finish before animating */
    LOWEST, /* Dropped if there is an active animation. Useful for hover animations */
}