package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.style.UIStyleSheet

open class UICheckbox(checked: Boolean = false, var imageStyle: String, style: String) : UISelectableButton<UIStyleSheet>(checked, "", style) {
    var selectedImage: String = "checkbox"
    lateinit var checkbox: UIImage

    init {
        onCheckChange { _, selected ->
            checkbox.image = if (selected) {
                selectedImage
            } else ""
        }
    }

    override fun initialize() {
        checkbox = UIImage(selectedImage, imageStyle)
        UIComponentDSL.pushComponent(checkbox)
    }

    override fun renderComponent() {}
}