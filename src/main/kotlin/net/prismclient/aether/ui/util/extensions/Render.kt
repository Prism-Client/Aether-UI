package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.renderer.builder.UIRendererDSL

inline fun renderer(block: UIRendererDSL.() -> Unit) = UIRendererDSL.instance.block().also { UIRendererDSL.instance.resetCalls() }