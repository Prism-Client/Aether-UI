//package net.prismclient.aether.ui.component.impl.container
//
//import net.prismclient.aether.UICore
//import net.prismclient.aether.ui.component.UIComponent
//import net.prismclient.aether.ui.component.enums.UIOverflow.*
//import net.prismclient.aether.ui.style__.UIProvider__
//import net.prismclient.aether.ui.util.UIKey
//import net.prismclient.aether.ui.util.extensions.renderer
//import kotlin.math.absoluteValue
//import kotlin.math.max
//import kotlin.math.min
//
///**
// * [UIContainer] are intended to hold a group of components in a specified area. Using [UIOverflow], scrollbars, and other
// * features can be used to control the layout
// */
//open class UIContainer(style: String) : UIComponent(style), UILayout {
//
//    protected val children = ArrayList<UIComponent>()
//
//    var verticalScrollbar = UIProvider__.verticalScrollbar
//    var horizontalScrollbar = UIProvider__.horizontalScrollbar
//
//    var expandedWidth = 0f
//    var expandedHeight = 0f
//
//    var horizontalSbValue: Float = 0f
//    var verticalSbValue: Float = 0f
//
//    var horizontalSb = false
//    var verticalSb = false
//
//    var horizontalSbSelected = false
//    var verticalBarSelected = false
//
//    var pressedX = 0f
//    var pressedY = 0f
//
//    private var verticalSbCache = 0f
//    private var initialVerticalSbValue = 0f
//    private var initialHorizontalSbValue = 0f
//
//    override fun update() {
//        super.update()
//        updateLayout()
//        calculateBounds()
//    }
//
//    override fun updateLayout() {
//        children.forEach(UIComponent::update)
//    }
//
//    override fun addChild(component: UIComponent) {
//        component.parent = this
//        children.add(component)
//    }
//
//    override fun removeChild(component: UIComponent) {
//        component.parent = null
//        children.remove(component)
//    }
//
//    override fun renderComponent() {
//        renderer {
//            scissor(relX, relY, relWidth, relHeight) {
//                translate(-(horizontalSbValue * expandedWidth), -(verticalSbValue * expandedHeight)) {
//                    children.forEach(UIComponent::render)
//                }
//            }
//        }
//
//        renderScrollbar(UICore.mouseX, UICore.mouseY)
//    }
//
//    fun renderScrollbar(mouseX: Float, mouseY: Float) {
//        /* Render the vertical scrollbar */
//        if (verticalSb && expandedHeight > 0) {
//            val newH = verticalScrollbar.height.calculate(height)
//            val x = this.x + verticalScrollbar.x.calculate(width) + verticalScrollbar.marginLeft.calculate(width) - verticalScrollbar.marginRight.calculate(width)
//            val width = verticalScrollbar.width.calculate(this.width)
//            val height = (newH / (expandedHeight + newH)) * newH
//            val leftover = (newH - height)
//            val y = this.y + (leftover * verticalSbValue) + verticalScrollbar.y.calculate(this.height) + verticalScrollbar.marginTop.calculate(height) - verticalScrollbar.marginBottom.calculate(height)
//
//            if (this@UIContainer.verticalBarSelected) {
//                val offset = mouseY - this.y - pressedY
//                if (offset != 0f) {
//                    verticalSbValue = min(1f, max(0f, offset / leftover + initialVerticalSbValue))
//                }
//            }
//
//            verticalSbValue += verticalSbCache
//            verticalSbValue = min(1f, max(0f, verticalSbValue))
//            verticalSbCache /= 1.5f
//
//            renderer {
//                color(verticalScrollbar.backgroundColor)
//                rect(x, this@UIContainer.y + verticalScrollbar.y.calculate(this@UIContainer.height) + verticalScrollbar.marginTop.calculate(height) - verticalScrollbar.marginBottom.calculate(height), width, newH, verticalScrollbar.backgroundRadius)
//
//                color(verticalScrollbar.scrollbarColor)
//                rect(x, y, width, height, verticalScrollbar.scrollbarRadius)
//                outline(verticalScrollbar.scrollbarOutlineWidth, verticalScrollbar.scrollbarOutlineColor) {
//                    rect(x, y, width, height, verticalScrollbar.scrollbarRadius)
//                }
//            }
//        }
//
//        /* Render the horizontal scrollbar */
//        if (horizontalSb && expandedWidth > 0) {
//            val newW = horizontalScrollbar.width.calculate(width)
//            val y = this.y + horizontalScrollbar.y.calculate(height) + horizontalScrollbar.marginTop.calculate(height) - horizontalScrollbar.marginBottom.calculate(height)
//            val height = horizontalScrollbar.height.calculate(this.height)
//            val width = (newW / (expandedWidth + newW)) * newW
//            val leftover = (newW - width)
//            val x = this.x + (leftover * horizontalSbValue) + horizontalScrollbar.x.calculate(this.width) + horizontalScrollbar.marginLeft.calculate(height) - horizontalScrollbar.marginRight.calculate(width)
//
//            if (this@UIContainer.horizontalSbSelected) {
//                val offset = mouseX - this.x - pressedX
//                if (offset != 0f) {
//                    horizontalSbValue = min(1f, max(0f, offset / leftover + initialHorizontalSbValue))
//                }
//            }
//
//            // scrolling for horizontal sb?
//            //verticalSbValue += verticalSbCache
//            //verticalSbValue = min(1f, max(0f, verticalSbValue))
//            //verticalSbCache /= 1.5f
//
//            renderer {
//                color(horizontalScrollbar.backgroundColor)
//                rect(this@UIContainer.x + horizontalScrollbar.x.calculate(this@UIContainer.height) + horizontalScrollbar.marginLeft.calculate(width) - horizontalScrollbar.marginRight.calculate(width), y, newW, height, horizontalScrollbar.backgroundRadius)
//
//                color(horizontalScrollbar.scrollbarColor)
//                rect(x, y, width, height, horizontalScrollbar.scrollbarRadius)
//                outline(horizontalScrollbar.scrollbarOutlineWidth, horizontalScrollbar.scrollbarOutlineColor) {
//                    rect(x, y, width, height, horizontalScrollbar.scrollbarRadius)
//                }
//            }
//        }
//    }
//
//    override fun mouseMoved(mouseX: Float, mouseY: Float) {
//        children.forEach { it.mouseMoved(mouseX, mouseY) }
//        super.mouseMoved(mouseX, mouseY)
//    }
//
//    override fun mouseClicked(mouseX: Float, mouseY: Float) {
//        super.mouseClicked(mouseX, mouseY)
//
//        children.forEach { it.mouseClicked(mouseX, mouseY) }
//
//        run {
//            val newH = verticalScrollbar.height.calculate(height)
//            val x = this.x + verticalScrollbar.x.calculate(width) + verticalScrollbar.marginLeft.calculate(width) - verticalScrollbar.marginRight.calculate(width)
//            val width = verticalScrollbar.width.calculate(this.width)
//            val height = (newH / (expandedHeight + newH)) * newH
//            val leftover = (newH - height)
//            val y = this.y + (leftover * verticalSbValue) + verticalScrollbar.y.calculate(this.height) + verticalScrollbar.marginTop.calculate(height) - verticalScrollbar.marginBottom.calculate(height)
//            val yTop = this.y + verticalScrollbar.y.calculate(this.height) + verticalScrollbar.marginTop.calculate(height) - verticalScrollbar.marginBottom.calculate(height)
//
//            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height) {
//                verticalBarSelected = true
//            } else if (mouseX >= x && mouseY >= yTop && mouseX <= x + width && mouseY <= yTop + newH) {
//                verticalSbValue = (mouseY - yTop - height / 2) / (newH - height)
//                verticalBarSelected = true
//            }
//        }
//
//        run {
//            val newW = horizontalScrollbar.width.calculate(width)
//            val y = this.y + horizontalScrollbar.y.calculate(height) + horizontalScrollbar.marginTop.calculate(height) - horizontalScrollbar.marginBottom.calculate(height)
//            val height = horizontalScrollbar.height.calculate(this.height)
//            val width = (newW / (expandedWidth + newW)) * newW
//            val leftover = (newW - width)
//            val x = this.x + (leftover * horizontalSbValue) + horizontalScrollbar.x.calculate(this.width) + horizontalScrollbar.marginLeft.calculate(width) - horizontalScrollbar.marginRight.calculate(width)
//            val xLeft = this.x + horizontalScrollbar.x.calculate(this.width) + horizontalScrollbar.marginLeft.calculate(width) - horizontalScrollbar.marginRight.calculate(width)
//
//            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height) {
//                horizontalSbSelected = true
//            } else if (mouseX >= xLeft && mouseY >= y && mouseX <= xLeft + newW && mouseY <= y + height) {
//                horizontalSbValue = (mouseX - xLeft - width / 2) / (newW - width)
//                horizontalSbSelected = true
//            }
//        }
//
//        pressedX = mouseX - x + horizontalScrollbar.x.calculate(this.width)
//        pressedY = mouseY - y + verticalScrollbar.y.calculate(this.height)
//
//        initialVerticalSbValue = verticalSbValue
//        initialHorizontalSbValue = horizontalSbValue
//    }
//
//    override fun mouseReleased(mouseX: Float, mouseY: Float) {
//        super.mouseReleased(mouseX, mouseY)
//
//        children.forEach { it.mouseReleased(mouseX, mouseY) }
//
//        verticalBarSelected = false
//        horizontalSbSelected = false
//    }
//
//    override fun mouseScrolled(mouseX: Float, mouseY: Float, scrollAmount: Float) {
//        children.forEach { it.mouseScrolled(mouseX, mouseY, scrollAmount) }
//
//        if (verticalSb && !verticalBarSelected && mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height) {
//            val newH = verticalScrollbar.height.calculate(height)
//            val height = (newH / (expandedHeight + newH)) * newH
//            val leftover = (newH - height)
//
//            verticalSbCache -= scrollAmount / leftover * 7000 / (expandedHeight / height) / height * max(1f, verticalSbCache.absoluteValue * 40)
//
//            //verticalSbValue -= scrollAmount / leftover * 25
//            verticalSbValue = max(0f, min(1f, verticalSbValue))
//        }
//    }
//
//    override fun keyPressed(key: UIKey, character: Char) {
//        children.forEach { it.keyPressed(key, character) }
//        super.keyPressed(key, character)
//    }
//
//    fun calculateBounds() {
//        var expandedWidth = 0f
//        var expandedHeight = 0f
//
//        // Calculate if any components leave the container
//        for (child in children) {
//            //child.style.overflow = VISIBLE
//            expandedWidth = max((child.relX + child.relWidth) - x, expandedWidth)
//            expandedHeight = max((child.relY + child.relHeight) - y, expandedHeight)
//        }
//
//        this.expandedWidth = max(0f, expandedWidth - width)
//        this.expandedHeight = max(0f, expandedHeight - height)
//
//        introduceScrollbar()
//    }
//
//    fun introduceScrollbar() {
//        // Check if overflow force enables the scrollbar
//        if (style.overflow == SCROLL) {
//            horizontalSb = true
//            verticalSb = true
//        }
//
//        // Check if a scrollbar needs to be introduced
//        if (style.overflow == AUTO) {
//            if (expandedWidth > width) {
//                horizontalSb = true
//            }
//            if (expandedHeight > 0) {
//                verticalSb = true
//            }
//        } else {
//            return
//        }
//
//        updateScrollbar()
//    }
//
//    fun updateScrollbar() {
//        if (verticalSb) {
//
//        }
//    }
//
//    fun adjustScrollbar(value: Float) {
//
//    }
//}