//package net.prismclient.aether.ui.importer
//
//import net.prismclient.aether.ui.component.enums.UIAlignment
//import net.prismclient.aether.ui.component.enums.UIAlignment.*
//import net.prismclient.aether.ui.style__.UIProvider__
//import net.prismclient.aether.ui.style__.UIStyleSheet__
//import net.prismclient.aether.ui.unit.util.UIOperation
//import net.prismclient.aether.ui.unit_.UIOperatorUnit
//import net.prismclient.aether.ui.unit_.impl.UIPixel
//import net.prismclient.aether.ui.util.extensions.asRGBA
//import net.prismclient.aether.ui.util.extensions.percent
//import net.prismclient.aether.ui.util.extensions.px
//import net.prismclient.aether.util.extensions.*
//import java.io.BufferedReader
//import java.io.InputStream
//import java.io.InputStreamReader
//import java.nio.charset.StandardCharsets
//
///**
// * UILoader is used to import Style Sheets from a specified file instead of loading style(s) directly
// * from a screen creation.
// *
// * @author sen
// * @since 4/5/2022
// */
//object UILoader {
//    fun loadBuild(stream: InputStream) {
//
//    }
//
//    fun loadStyle(stream: InputStream) {
////        var nanoTime = System.nanoTime()
//
//        val lines = BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8)).readLines()
//
//        // println("Took: ${System.nanoTime() - nanoTime} to load from file")
//
////        nanoTime = System.nanoTime()
//
//        lateinit var activeSheet: UIStyleSheet__
//
//        var fieldName = ""
//        var fieldExtensionName = ""
//        var inFieldExtension = false
//        var inField = false
//
//        for (line in lines) {
//            // Find the nearest field
//            if (!inField) {
//                var name = ""
//                for (c in line) {
//                    if (c == ':') { // If a ';' is introduced, assume we are now declaring styles to inherit
//                        // println("In field")
//                        inFieldExtension = true
//                        continue
//                    }
//
////                    if (inFieldExtension) {
//                        /* If we're already in the inheritance clearance area,
//                        check if there is '{' to see if we've finished */
//                        if (c == '{' && name.isNotEmpty()) {
//                            name = name.omitOuterSpaces()
//                            fieldExtensionName = fieldExtensionName.omitOuterSpaces()
//                            inField = true
//                            inFieldExtension = false
//                            // println("Parent(s): '$fieldExtensionName'")
//                            activeSheet = if (fieldExtensionName.isNotEmpty()) {
//                                val parents = fieldExtensionName.omitOuterSpaces().split()
//                                if (parents.size == 1) {
//                                    UIProvider__.getStyle(fieldExtensionName, false).copy(name, false)
//                                } else {
//                                    var sheet = UIProvider__.getStyle(parents[0])
//
//                                    for (i in 1 until parents.size) {
//                                        sheet = UIProvider__.merge(sheet, UIProvider__.getStyle(parents[i]))
//                                        // println("   --> Merged Parent: ${sheet.name}")
//                                    }
//
//                                    sheet.copy(name, true)
//                                }
//                            } else {
//                                UIStyleSheet__(name, false)
//                            }
//                            fieldExtensionName = ""
//                            UIProvider__.addStyle(activeSheet)
//                            //println("\nCreating sheet with name: '$name'")
//                            break
//                        }
////                    }
//
//                    if (c.isLetterOrDigit() || c == ' ' || c == ',') {
//                        if (inFieldExtension) {
//                            fieldExtensionName += c
//                        } else {
//                            name += c
//                        }
//                    } else {
//                        // println("The character $c is not supported")
//                    }
//                }
//            } else {
//                // Break out of the active field
//                if (line.contains('}')) {
//                    inField = false
//                    fieldName = ""
//                    continue
//                }
//
//                // If already in the field, collect values
//                var valueName = ""
//                var inValue = false
//                var value = ""
//
//                for (c in line) {
//                    if (!inValue) { // Find the value name
//                        if (c == ':') {
//                            inValue = true
//                        } else {
//                            valueName += c
//                        }
//                    } else { // If looking for the value
//                        if (c != ';') {
//                            value += c
//                        } else {
//                            break // End this line because of the semicolon
//                        }
//                    }
//                }
//
//                if (value.isNotEmpty()) {
//                    toProperty(activeSheet, valueName.omitSpaces().lowercase(), value.omitSpaces())
//                }
//            }
//        }
//        // println("Took: ${System.nanoTime() - nanoTime} to parse file")
//    }
//
//    private fun toProperty(styleSheet: UIStyleSheet__, name: String, value: String) {
//        // println("--> Attempting to set property: $name to $value")
//
//        val v = value.lowercase()
//        when (name) {
//            "align" -> styleSheet.align(v.parseAlignment())
//            "alignment" -> styleSheet.alignment = v.parseAlignment()
//
//            "x" -> styleSheet.x = v.toOperatorUnit()
//            "y" -> styleSheet.y = v.toOperatorUnit()
//            "width" -> styleSheet.width = v.toOperatorUnit()
//            "height" -> styleSheet.height = v.toOperatorUnit()
//            "position" -> {
//                val v = v.toOperatorUnitList()
//                styleSheet.x = v.getOrNull(0) ?: styleSheet.x
//                styleSheet.y = v.getOrNull(1) ?: styleSheet.y
//            }
//            "size" -> {
//                val v = v.toOperatorUnitList()
//                styleSheet.width = v.getOrNull(0) ?: styleSheet.width
//                styleSheet.height = v.getOrNull(1) ?: styleSheet.height
//            }
//
//            "margin" -> {
//                val v = v.toUnitList()
//                styleSheet.margin(v.getOrNull(0), v.getOrNull(1), v.getOrNull(2), v.getOrNull(3))
//            }
//            "margin-top" -> styleSheet.marginTop = v.toUnit()
//            "margin-right" -> styleSheet.marginRight = v.toUnit()
//            "margin-bottom" -> styleSheet.marginBottom = v.toUnit()
//            "margin-left" -> styleSheet.marginLeft = v.toUnit()
//
//            "padding" -> {
//                val v = v.toUnitList()
//                styleSheet.padding(v.getOrNull(0), v.getOrNull(1), v.getOrNull(2), v.getOrNull(3))
//            }
//            "padding-top" -> styleSheet.paddingTop = v.toUnit()
//            "padding-right" -> styleSheet.paddingRight = v.toUnit()
//            "padding-bottom" -> styleSheet.paddingBottom = v.toUnit()
//            "padding-left" -> styleSheet.paddingLeft = v.toUnit()
//
//            "primary-color" -> styleSheet.primaryColor = v.toColor()
//            "secondary-color" -> styleSheet.secondaryColor = v.toColor()
//
//            "text-alignment", "text-align" -> styleSheet.textAlignment = v.parseAlignment()
//            "text-size" -> styleSheet.textSize = v.toFloat()
//            "text-color" -> styleSheet.textColor = v.toColor()
//            "text-font-face" -> styleSheet.textFontFace = value
//            "text-font-blur" -> styleSheet.textFontBlur = v.toFloat()
//            "caret-color" -> styleSheet.caretColor = v.toColor()
//            "inactive-text-color" -> styleSheet.inactiveTextColor = v.toColor()
//            "line-height" -> styleSheet.lineHeight = v.toUnit()
//
//            "background-color" -> styleSheet.backgroundColor = v.toColor()
//            "background-selected" -> styleSheet.backgroundSelected = v.toColor()
//            "background-radius" -> styleSheet.backgroundRadius = v.toFloat()
//
//            "border-width" -> styleSheet.borderWidth = v.toFloat()
//            "border-color" -> styleSheet.borderColor = v.toColor()
//            "border-radius" -> styleSheet.backgroundRadius = v.toFloat()
//            else -> throw UnsupportedOperationException("$name is not a valid style sheet property")
//        }
//    }
//
//    private fun String.omitSpaces(): String = this.replace(" ", "")
//
//    private fun String.omitOuterSpaces(): String {
//        if (this.isEmpty()) return this
//        var startSpace = 0
//        var endSpace = this.length
//        for (i in 0 until this.length) {
//            if (this[i] != ' ') {
//                startSpace = i; break
//            }
//        }
//        for (i in this.length - 1 downTo  0) {
//            if (this[i] == ' ') {
//                endSpace = i
//            } else { break }
//        }
//        return this.substring(startSpace).substring(0, endSpace - startSpace)
//    }
//
//    private fun String.split(): List<String> {
//        val split = ArrayList<String>()
//        split.addAll(this.split(','))
//
//        for (i in split.indices) {
//            if (split[i].length > 1 && split[i][0] == ' ') {
//                split[i] = split[i].substring(1)
//            }
//        }
//
//       return split
//    }
//
//    private fun String.toOperatorUnit(): UIOperatorUnit {
//        val split = this.split('+', '-')
//
//        val unit = split[0].toUnit()
//
//        return if (split.size == 1) {
//            UIOperatorUnit(unit.value, unit.type)
//        } else {
//            val operator = UIOperatorUnit(unit.value, unit.type, split[1].toUnit())
//
//            operator.operation = if (this.contains('+')) {
//                UIOperation.ADD
//            } else if (this.contains('-')) {
//                UIOperation.SUBTRACT
//            } else if (this.contains('*')) {
//                UIOperation.MULTIPLY
//            } else if (this.contains('/')) {
//                UIOperation.DIVIDE
//            } else {
//                null
//            }
//
//            operator
//        }
//    }
//
//    private fun String.toUnit(): UIPixel { // TODO: Repeating, String Similarity, Auto, and Initial
//        val split = this.otherThanDigit()
//        val type = if (split == this.length) "px" else this.substring(split)
//        val value = (if (split == this.length) this else (if (split == 0) "0" else this.substring(0, split))).toFloat()
//
//        if (type.isFunction()) {
//            throw UnsupportedOperationException("Functions are not supported for this value")
//        }
//
//        // println("   --> Unit: $type, Value: $value")
//        return unitFromType(type, value)
//    }
//
//    private fun String.toOperatorUnitList(): List<UIOperatorUnit> {
//        val split = this.split(',')
//
//        return List(split.size) { split[it].toOperatorUnit() }
//    }
//
//    private fun String.toUnitList(): List<UIPixel> {
//        if (this.isFunction()) {
//            val name = this.funcName()
//            val params = this.grabParameters()
//
//            if (name == "repeat") {
//                val unit = params[1].toUnit()
//                return List(params[0].toInt()) { unit.copy() }
//            }
//        }
//        val split = this.split(',')
//
//        return List(split.size) { split[it].toUnit() }
//    }
//
//    private fun unitFromType(type: String, value: Float) = when (type.lowercase()) {
//        "px" -> px(value)
//        "percent", "%" -> percent(value / 100)
//        "em" -> em(value)
//        "fr", "frac", "fraction" -> fraction(value)
//        "auto" -> auto()
//        "init", "initial" -> initial()
//        else -> throw UnsupportedOperationException("Unit: $type is not supported")
//    }
//
//    private fun String.otherThanDigit(): Int {
//        for ((i, c) in this.withIndex()) {
//            if (!c.isDigit() && c != '.' && c != '-') {
//                return i
//            }
//        }
//        return this.length
//    }
//
//    private fun String.isFunction(): Boolean {
//        for ((i, c) in this.withIndex()) {
//            if (i > 0 && c == '(') {
//                if (this.last() == ')') {
//                    return true
//                }
//            }
//            if (!c.isLetterOrDigit()) {
//                return false
//            }
//        }
//        return false
//    }
//
//    private fun String.funcName(): String {
//        var new = ""
//        for (c in this) {
//            if (c.isLetterOrDigit()) {
//                new += c
//            } else {
//                break
//            }
//        }
//        return new
//    }
//
//    private fun String.grabParameters(): List<String> {
//        var pos = 0
//
//        for ((i, c) in this.withIndex()) {
//            if (c == '(') {
//                pos = i + 1
//                break
//            }
//        }
//
//        return this.substring(pos, this.length - 1).split(',')
//    }
//
//    private fun String.toColor(): Int {
//        if (this.isFunction()) {
//            val name = this.funcName()
//            val params = this.grabParameters()
//
//            // println("   --> Parsing Function: $name, Params: $params")
//            if (name == "rgb" || name == "rgba") {
//                // println("   --> Found a color function")
//
//                val r = if (params[0].contains('.')) (params[0].toFloat() * 255f - 0.5f).toInt() else params[0].toInt()
//                val g = if (params[1].contains('.')) (params[1].toFloat() * 255f - 0.5f).toInt() else params[1].toInt()
//                val b = if (params[2].contains('.')) (params[2].toFloat() * 255f - 0.5f).toInt() else params[2].toInt()
//                val a = if (params.getOrNull(3) == null) {
//                    255
//                } else {
//                    if (params[3].contains('.')) (params[3].toFloat() * 255f - 0.5f).toInt() else params[3].toInt()
//                }
//
//                // println("   --> Parsed color as r: $r, g: $g, b: $b, a: $a")
//
//                return asRGBA(r, g, b, a)
//            }
//        }
//        return this.toInt()
//    }
//
//    private fun String.parseAlignment(): UIAlignment = when (this) {
//        "custom" -> CUSTOM
//        "topleft" -> TOPLEFT
//        "topcenter" -> TOPCENTER
//        "topright" -> TOPRIGHT
//        "middleleft" -> MIDDLELEFT
//        "center", "middle" -> CENTER
//        "middleright" -> MIDDLERIGHT
//        "bottomleft" -> BOTTOMLEFT
//        "bottomcenter" -> BOTTOMCENTER
//        "bottomright" -> BOTTOMRIGHT
//        else -> throw UnsupportedOperationException("$this is not a valid alignment option")
//    }
//}