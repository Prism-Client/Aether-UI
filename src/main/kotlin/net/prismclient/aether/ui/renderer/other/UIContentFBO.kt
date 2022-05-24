package net.prismclient.aether.ui.renderer.other

import net.prismclient.aether.ui.component.type.layout.UIFrame

/**
 * [UIContentFBO] represents an FBO object. They are general created
 * when a [UIFrame] is created
 *
 * @author sen
 * @since 5/1/2022
 */
class UIContentFBO(
    val id: Int,
    val width: Float,
    val height: Float,
    val scaledWidth: Float,
    val scaledHeight: Float,
    val contentScale: Float
)