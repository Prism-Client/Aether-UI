package net.prismclient.aether.ui.component.type.input.button

import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL
import net.prismclient.aether.ui.style.UIStyleSheet

open class UICheckbox(
    checked: Boolean = false,
    var selectedImageName: String = "checkbox",
    var deselectedImageName: String = "",
    var imageStyle: String,
    style: String
) : UISelectableButton<UIStyleSheet>(checked, "", style) {
    lateinit var selectedImage: UIImage
    lateinit var deselectedImage: UIImage

    init {
        onCheckChange { _, isSelected ->
            if (isSelected) {
                selectedImage.visible = true
                deselectedImage.visible = false
            } else {
                selectedImage.visible = false
                deselectedImage.visible = true
            }
        }
    }

    override fun initialize() {
        selectedImage = UIImage(selectedImageName, imageStyle)
        deselectedImage = UIImage(deselectedImageName, imageStyle)
        if (deselectedImageName.isEmpty()) // Make the deselected image invisible
            deselectedImage.style.imageColor = 0
        UIComponentDSL.pushComponent(selectedImage)
        UIComponentDSL.pushComponent(deselectedImage)
    }

    override fun renderComponent() {}
}