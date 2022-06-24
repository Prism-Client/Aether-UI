package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.UIOperation

class UIOperationUnit(var unit1: UIUnit, var unit2: UIUnit, var operation: UIOperation) : UIUnit(0f, -1) {
    override fun copy(): UIUnit = UIOperationUnit(unit1.copy(), unit2.copy(), operation)

    /**
     * Sorts the given units into proper PEMDAS format
     */
    fun sort(): UIOperationUnit {
        TODO("Operation sorting not yet implemented")
    }

    override fun toString(): String {
        return "UIOperationUnit(unit1=$unit1, unit2=$unit2, operation=$operation)"
    }
}