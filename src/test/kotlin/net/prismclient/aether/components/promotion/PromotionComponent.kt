package net.prismclient.aether.components.promotion

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.renderer.UIRenderer
import net.prismclient.aether.ui.renderer.dsl.UIComponentDSL.ubuild
import net.prismclient.aether.ui.renderer.impl.font.UIFont
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.extensions.*


/**
 * This is an example of a component class, where we combine
 * a few components to make a bigger one. This technically can
 * be done without a custom component, using a container
 * which holds all the components, however, it might be more efficient,
 * easier to read, and require less code to create a component to
 * accomplish this task. In this case, we want a component which has a
 * title, description, and button like this: https://imgur.com/a/3L3bFe3
 *
 * Since we have 3 components that we're going to create, we need the styles
 * for them. Since we want to be able to customize them outside the component
 * we pass the style for each component as a parameter in the constructor.
 *
 * @author sen
 */
class PromotionComponent(
    val titleText: String,
    val text: String,
    val buttonText: String,
    style: String
) : UIComponent<PromotionStyleSheet>(style) {
    // This above is basic format for creating a component. Since we have
    // the needs for a custom property; the color of the button, we need
    // to create a custom style sheet.

    // We can store the components that we're holding here, as late init variables
    lateinit var title: UILabel
    lateinit var description: UILabel
    lateinit var button: UIButton<UIStyleSheet>

    override fun initialize() {
        // The unsafe version of build, which is used to create screens.
        // It creates a DSL block which does nothing else. We can use the
        // DSL functions now.
        ubuild {
            // We want to force certain sizes on the components.
            title = label(titleText, "PromotionTitle")
            description = label(text, "PromotionDescription")
            button = button(buttonText, "PromotionButton").style {
                background {
                    // Set the background color of the button
                    // to the given value with the alpha at 10%
                    backgroundColor = style.buttonColor.setAlpha(0.1f)

                    border {
                        // Despite not being shown, we want a border as well,
                        // so let's set it to the given value with the alpha at 80%
                        borderColor = style.buttonColor.setAlpha(0.8f)
                    }
                }

                font {
                    // Set the font color to the button color
                    fontColor = style.buttonColor
                }
            }
        }
        // Invokes all the initialization listeners
        super.initialize()
    }

    /**
     * The only required method that should be implemented. We don't need to render
     * anything because we're only holding components
     */
    override fun renderComponent() {}

    /**
     * Here, we create the styles for the components.
     *
     * This block is essentially the equivalent of static {}
     */
    companion object {
        init {
            // We need to create sheets for all the components
            style(UIStyleSheet(), "PromotionTitle") {
                // Align and anchor to the TOPCENTER of the screen
                // It's like doing
                // align(TOPCENTER) = (x = rel(0.5)), (y = rel(0.0))
                // anchor(TOPCENTER) = (x = rel(0.5)), (y = rel(0.0))
                control(UIAlignment.TOPCENTER)
                y = px(20f) // Push the component down by 20 px

                // Create the font. This is a shorthand for creating it,
                // it can be set manually inside the font block
                font(fontSize = 16f, fontColor = -1, textAlignment = UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP) {
                    // UIFont is an is a UIShape, which has a unique x, and y values relative to the component
                    align(UIAlignment.TOPCENTER)
                    fontFamily = "Poppins"
                    fontType = UIFont.FontType.Light

                    // Change the render type to wrapping, so we can
                    // have multiple lines
                    fontRenderType = UIFont.FontRenderType.WRAP
                    // Next, break the text at the parent width - 20px
                    // In this case it's the equivalent of the parent
                    // width with 10px padding left and right
                    lineBreakWidth = rel(1) - px(20)
                }
            }

            // Let's do the same for the description
            style(UIStyleSheet(), "PromotionDescription") {
                control(UIAlignment.TOPCENTER)
                y = px(57)
                font(
                        "Poppins",
                        14f,
                        asRGBA(255, 255, 255, 0.5f),
                        UIRenderer.ALIGNCENTER or UIRenderer.ALIGNTOP,
                        UIFont.FontType.Light
                ) {
                    align(UIAlignment.TOPCENTER)
                    fontRenderType = UIFont.FontRenderType.WRAP
                    lineBreakWidth = rel(1) - px(20)
                }
            }

            // For the button, we align it to the bottom center of this
            style(UIStyleSheet(), "PromotionButton") {
                control(UIAlignment.BOTTOMCENTER)
                // Set the size to 161x41px
                size(161, 41)
                // Push it up 10 px
                y -= px(10f)
                // The color is RGBA(0,0,0,0) because we set it when we
                // create the component to the value in the style sheet
                background(0, radius(8f))
                font(
                        "Poppins",
                        16f,
                        asRGBA(87, 164, 255),
                        UIRenderer.ALIGNMIDDLE or UIRenderer.ALIGNCENTER,
                        UIFont.FontType.Bold
                ) {
                    align(UIAlignment.CENTER)
                    // descender = bottom portion of text (basically)
                    y += descender(0.5f)
                }
            }
        }
    }
}