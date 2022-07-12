package net.prismclient.aether.ui.renderer.other

/**
 * [UIContentFBO] represents a framebuffer. It contains the necessary information to render the fbo
 * to an image via the [imagePattern]. The [contentScale] represents the device pixel ratio.
 *
 * @author sen
 * @since 1.0
 */
data class UIContentFBO(
    val imagePattern: Int,
    val width: Float,
    val height: Float,
    val scaledWidth: Float,
    val scaledHeight: Float,
    val contentScale: Float
)