package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.style.UIStyleSheet

open class UICheckbox(checked: Boolean = false, imageStyle: String, style: String) : UISelectableButton<UIStyleSheet>(checked, "", style) {
    var selectedImage: String = "checkbox"
    val checkbox: UIImage = UIImage(selectedImage, imageStyle)

    init {
        checkbox.parent = this
        UIComponentDSL.pushComponent(checkbox)
        onCheckChange { button, selected ->
            checkbox.image = if (selected) {
                selectedImage
            } else ""
        }
    }

    override fun renderComponent() {}
}