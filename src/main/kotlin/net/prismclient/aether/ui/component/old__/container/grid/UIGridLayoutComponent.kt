//package net.prismclient.aether.ui.component.impl.container.grid
//
//import net.prismclient.aether.ui.unit_.*
//import net.prismclient.aether.ui.component.UIComponent
//import net.prismclient.aether.ui.component.impl.container.UIContainer
//import net.prismclient.aether.ui.unit_.impl.UIPixel
//import net.prismclient.aether.ui.util.extensions.px
//import kotlin.math.max
//
//open class UIGridLayoutComponent(style: String) : UIContainer(style) {
//    protected var columns: Array<UIPixel> = arrayOf()
//    protected var rows: Array<UIPixel> = arrayOf()
//
//    var columnSpacing: UIPixel = px(0)
//    var rowSpacing: UIPixel = px(0)
//
//    var retainSpace = true /* If there is a missing component the grid will take the previous row's spacing to fill */
//
//    fun width(): Float = width
//
//    fun height(): Float = height
//
//    override fun updateLayout() {
//        var i = 0
//
//        val leftoverWidth = FloatArray(rows.size) { 0f }
//        var leftoverHeight = 0f
//        val heights = FloatArray(rows.size) { 0f }
//        var fractionWidthValue = 0f
//        var fractionHeightValue = 0f
//        var widthAutoCount = 0
//        var heightAutoCount = 0
//
//        var hoffset = 0f
//        for ((r, row) in rows.withIndex()) {
//            var woffset = 0f
//            for ((c, column) in columns.withIndex()) {
//                val child = children.getOrNull(i).also {
//                    it?.updateSize()
//                    it?.overrided = true
//                }
//                var w = when (column.type) {
//                    INITIAL -> width(i)
//                    PIXELS -> column.value
//                    RELATIVE -> column.value * width()
//                    FRACTION -> {
//                        if (r == 0) { fractionWidthValue += column.value }
//                        width(i)
//                    }
//                    AUTO -> {
//                        if (r == 0) { widthAutoCount++ }
//                        width(i)
//                    }
//                    else -> throw UnsupportedOperationException()
//                }
//                var h = when (row.type) {
//                    INITIAL -> height(i)
//                    PIXELS -> row.value
//                    RELATIVE -> row.value * height()
//                    FRACTION, AUTO -> height(i)
//                    else -> throw UnsupportedOperationException()
//                }
//
//                if (child != null) {
//                    if (heights[r] < h) {
//                        heights[r] = h
//                    }
//
//                    child.x = x + woffset
//                    child.y = y// + hoffset
//                    child.width = w
//                    child.height = h
//                    w = child.relWidth
//                }
//
//                woffset += w + if (c < columns.size - 1) columnSpacing.calculate(width) else 0f
//
//                i++
//            }
//            if (row.type == AUTO) {
//                heightAutoCount++
//            } else if (row.type == FRACTION) {
//                fractionHeightValue += row.value
//            }
//            leftoverWidth[r] = width() - woffset
//            hoffset += heights[r] + if (r < rows.size - 2) rowSpacing.calculate(height) else 0f
//        }
//        leftoverHeight = height() - hoffset
//
//        i = 0
//        var hp = 0f
//
//        for ((r, row) in rows.withIndex()) {
//            val widthFractionPush = leftoverWidth[r] / max(1f, fractionWidthValue)
//            val heightFractionPush = leftoverHeight / max(1f, fractionHeightValue)
//            var totalHeight = 0f
//            for (column in columns) {
//                val child = children.getOrNull(i)
//
//                if (child != null) {
//                    child.height = heights[r]
//
//                    var wpush = 0f
//                    var hpush = 0f
//
//                    if (column.type == AUTO) {
//                        if (fractionWidthValue < 1f) {
//                            wpush = (leftoverWidth[r] - (widthFractionPush * fractionWidthValue)) / widthAutoCount
//                        }
//                    } else if (column.type == FRACTION) {
//                        wpush = widthFractionPush * column.value
//                    }
//
//                    if (row.type == AUTO) {
//                        if (fractionHeightValue < 1f) {
//                            hpush = (leftoverHeight / heightAutoCount)
//                        }
//                    } else if (row.type == FRACTION) {
//                        hpush = heightFractionPush * row.value//leftoverHeight * row.value
//                    }
//
//                    if (wpush != 0f || hpush != 0f) {
//                        child.y += hp
//                        child.width += wpush
//                        child.height += hpush
//                        totalHeight = child.height
//
//
//                        for (j in i + 1 until (columns.size * (r + 1))) {
//                            (children.getOrNull(j) ?: break).x += wpush
//                        }
//                    }
//                }
//                i++
//            }
//            hp += totalHeight + if (r < rows.size - 1) rowSpacing.calculate(height) else 0f
//        }
//        children.forEach(UIComponent::update)
//    }
//
//    private fun width(index: Int) = children.getOrNull(index)?.width ?: 0f
//
//    private fun height(index: Int) = children.getOrNull(index)?.height ?: 0f
//
//    fun column(vararg units: UIPixel) {
//        columns = arrayOf(*units)
//    }
//
//    fun row(vararg units: UIPixel) {
//        rows = arrayOf(*units)
//    }
//
//    fun acolumn(units: Array<UIPixel>) {
//        columns = units
//    }
//
//    fun arow(units: Array<UIPixel>) {
//        rows = units
//    }
//}