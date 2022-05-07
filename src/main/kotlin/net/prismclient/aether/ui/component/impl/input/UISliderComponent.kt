//package net.prismclient.aether.ui.component.impl.input
//
//import net.prismclient.aether.ui.component.UIComponent
//import net.prismclient.aether.ui.style__.sheets.UISliderSheet__
//
//class UISliderComponent(
//    private val slider: UISliderSheet__,
//    value: Float,
//    var min: Float = 0f,
//    var max: Float = 10f,
//    var step: Float = 1f,
//    style: String
//) : UIComponent(style) {
//    private var sliderWidth = 0f
//    private var sliderHeight = 0f
//
//    private var mouseOffset = 0f
//    private var sliderPosition = 0f
//    var selected = false
//        private set
//
//    override fun update() {
//        super.update()
//        sliderWidth = slider.width.calculate(width)
//        sliderHeight = slider.height.calculate(height)
//    }
//
//    override fun renderComponent() {
//
//    }
//
//    override fun mouseClicked(mouseX: Float, mouseY: Float) {
//        super.mouseClicked(mouseX, mouseY)
//        if (isMouseInside(mouseX, mouseY)) {
//            selected = true
//            mouseOffset = mouseX - (x + width)
//        }
//    }
//
//    override fun mouseReleased(mouseX: Float, mouseY: Float) {
//        super.mouseReleased(mouseX, mouseY)
//        selected = false
//    }
//
//    private fun updateSlider() {
////        sliderPosition = (width - ())
//    }
//}