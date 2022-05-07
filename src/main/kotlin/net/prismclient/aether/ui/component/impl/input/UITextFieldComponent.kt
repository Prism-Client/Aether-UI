//package net.prismclient.aether.ui.component.impl.input
//
//import net.prismclient.aether.ui.component.UIComponent
//import net.prismclient.aether.ui.component.enums.UIAlignment
//import net.prismclient.aether.ui.component.impl.container.UIContainer
//import net.prismclient.aether.ui.util.UIKey
//import net.prismclient.aether.ui.util.extensions.renderer
//import kotlin.math.max
//import kotlin.math.min
//
///**
// * A simple class to hold text given by the user.
// *
// * @author sen
// * @since 3/24/2022
// */
//class UITextFieldComponent(var text: String = "", var placeholder: String = "", style: String, caretStyle: String) : UIComponent(style) {
//    var caretPosition = -1
//
//    var caret = UIContainer(caretStyle)
//
//    init {
//        caret.parent = this
//    }
//
//    override fun renderComponent() {
//        renderer {
//            font(style)
//
//            val textX = when (style.textAlignment) {
//                UIAlignment.TOPCENTER, UIAlignment.CENTER, UIAlignment.BOTTOMCENTER -> (width / 2 - text.width() / 2)
//                UIAlignment.TOPRIGHT, UIAlignment.MIDDLERIGHT, UIAlignment.BOTTOMRIGHT -> (width - text.width())
//                else -> 0f
//            } + x
//
//            val textY = when (style.textAlignment) {
//                UIAlignment.MIDDLELEFT, UIAlignment.CENTER, UIAlignment.MIDDLERIGHT -> (height - text.height()) / 2f
//                UIAlignment.BOTTOMLEFT, UIAlignment.BOTTOMCENTER, UIAlignment.BOTTOMRIGHT -> (height - text.height())
//                else -> 0f
//            } + y
//
//            val text = if (text.isNotEmpty() || caretPosition >= 0) {
//                if (caretPosition >= 0) {
//                    renderCaret(textX + text.substring(0, caretPosition).width(), textY)
//                }
//
//                text
//            } else {
//                placeholder
//            }
//
//            color(style.textColor)
//
//            text.render(style.textAlignment, x, y, width, height)
//        }
//    }
//
//    override fun mouseClicked(mouseX: Float, mouseY: Float) {
//        super.mouseClicked(mouseX, mouseY)
//
//        caretPosition = if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
//            0
//        } else {
//            -1
//        }
//
//        renderer {
//            font(style)
//
//            val textXOriginal = when (style.textAlignment) {
//                UIAlignment.TOPCENTER, UIAlignment.CENTER, UIAlignment.BOTTOMCENTER -> (width / 2 - text.width() / 2)
//                UIAlignment.TOPRIGHT, UIAlignment.MIDDLERIGHT, UIAlignment.BOTTOMRIGHT -> (width - text.width())
//                else -> 0f
//            } + x
//
//            var textX = textXOriginal
//
//            for (i in text.indices) {
//                val cw = text[i].toString().width() + 1 // Character's width
//                val hcw = cw / 2f // Half the character's width
//
//                if (i == 0) {
//                    if (mouseX >= textX && mouseY >= y && mouseX <= textX && mouseY <= y + height) {
//                        caretPosition = 0
//                        break
//                    }
//                }
//
//                if (mouseX >= textXOriginal && mouseY >= y && mouseX <= textX + hcw && mouseY <= y + height) {
//                    caretPosition = i
//                    break
//                }
//
//                if (i == text.length - 1) {
//                    if (mouseX >= textX && mouseY >= y && mouseX <= x + width && mouseY <= y + height) {
//                        caretPosition = text.length
//                        break
//                    }
//                }
//
//                textX += cw
//            }
//        }
//    }
//
//    override fun keyPressed(key: UIKey, character: Char) {
//        when (key) {
//            UIKey.BACKSPACE -> if (caretPosition > 0) {
//                text = text.substring(0, caretPosition - 1) + text.substring(caretPosition)
//                caretPosition -= 1
//            }
//            UIKey.ARROW_LEFT -> caretPosition = max(0, caretPosition - 1)
//            UIKey.ARROW_RIGHT -> caretPosition = min(text.length, caretPosition + 1)
//            UIKey.SPACE -> {
//                text = text.substring(0, caretPosition) + " " + text.substring(caretPosition)
//                caretPosition += 1
//            }
//            else -> {
//                if (character.code != 0) {
//                    text = text.substring(0, caretPosition) + character + text.substring(caretPosition)
//                    caretPosition += 1
//                }
//            }
//        }
//        super.keyPressed(key, character)
//    }
//
//    override fun initialize() {
//        super.initialize()
//        caret.initialize()
//    }
//
//    override fun update() {
//        super.update()
//        caret.update()
//    }
//
//    private fun renderCaret(x: Float, y: Float) {
//        renderer {
//            caret.x = x
//            caret.y = y
//            caret.render()
//        }
//    }
//}