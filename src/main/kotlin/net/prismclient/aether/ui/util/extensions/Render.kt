package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.dsl.UIRendererDSL
import net.prismclient.aether.ui.util.Block

inline fun renderer(block: Block<UIRendererDSL>) = UIRendererDSL.block()