package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.UIOperation

class UIOperationUnit(var unit1: UIUnit, var unit2: UIUnit, var operation: UIOperation) : UIUnit(0f, -1) {
    override fun copy(): UIUnit = UIOperationUnit(unit1.copy(), unit2.copy(), operation)

    override fun toString(): String {
        return "UIOperationUnit(unit1=$unit1, unit2=$unit2, operation=$operation)"
    }
}