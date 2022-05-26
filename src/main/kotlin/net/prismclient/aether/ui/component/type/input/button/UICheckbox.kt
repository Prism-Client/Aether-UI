package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.style.UIStyleSheet

open class UICheckbox(checked: Boolean = false, var imageStyle: String, style: String) : UISelectableButton<UIStyleSheet>(checked, "", style) {
    lateinit var selectedImage: UIImage
    lateinit var deselectedImage: UIImage

    init {
        onCheckChange { _, isSelected ->
            //checkbox.image = if (isSelected) selectedImage else deselectedImage
        }
    }

    override fun initialize() {
        //checkbox = UIImage(selectedImage, imageStyle)
        //UIComponentDSL.pushComponent(checkbox)
    }

    override fun renderComponent() {}
}