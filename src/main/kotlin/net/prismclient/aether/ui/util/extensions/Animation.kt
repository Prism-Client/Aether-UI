package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.*

/**
 * Returns true if the unit is not a valid default unit type
 */
fun UIUnit?.isIrregular() = if (this != null)
    (this.type == PIXELS || this.type == RELATIVE || this.type == EM || this.type == BORDER || this.type == ASCENDER || this.type == ASCENDER)
else false