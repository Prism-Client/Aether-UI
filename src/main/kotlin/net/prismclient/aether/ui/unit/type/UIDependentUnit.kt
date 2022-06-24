package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.unit.UIUnit
import java.util.function.Supplier

/**
 * [UIDependentUnit] is a unit which runs a functional interface [function] and sets
 * the value to the returned value of that function.
 *
 * @author sen
 * @since 6/20/2022
 */
class UIDependentUnit(var function: Supplier<Float>) : UIUnit(0f)