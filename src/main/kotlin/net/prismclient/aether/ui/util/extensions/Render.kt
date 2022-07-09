package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.dsl.UIRendererDSL

inline fun renderer(block: UIRendererDSL.() -> Unit) = UIRendererDSL.block()//.also { UIRendererDSL.resetCalls() }