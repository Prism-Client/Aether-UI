package net.prismclient.aether.ui.unit.type

import net.prismclient.aether.ui.unit.UIUnit
import net.prismclient.aether.ui.unit.util.UIOperation

class UIOperationUnit(value: Float, type: Byte, var otherUnit: UIUnit, var operation: UIOperation) : UIUnit(value, type) {
    override fun copy(): UIUnit = UIOperationUnit(value, type, otherUnit.copy(), operation)
}