package examples

import examples.deps.Generic
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.UIAccordion
import net.prismclient.aether.ui.component.type.layout.UIContainer
import net.prismclient.aether.ui.component.type.layout.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.dsl.UIComponentDSL
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.create
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.interfaces.UITriFunction
import net.prismclient.aether.ui.util.radiusOf
import net.prismclient.aether.ui.util.style
import net.prismclient.aether.ui.util.ucreate
import java.util.function.BiConsumer
import java.util.function.Function

class Default : UIScreen {
    override fun build() {
        create {
            UIAssetDSL.svg("accordion-icon", "/prism/icons/gradient/folder.svg")
            UIAssetDSL.svg("accordion-chevron", "/prism/icons/outline/chevron.svg")
            include(Generic())

            UIContainerSheet().style("accordion-information") {
                size(rel(1), px(67))
                background(asRGBA(1f, 0f, 0f, 0.3f))
            }

            UIStyleSheet().style("accordion-title") {
                control(UIAlignment.MIDDLELEFT)
                font("Montserrat-bold", colorOf(37, 39, 51), px(20f))
                x += px(82)
            }

            UIImageSheet().style("accordion-icon") {
                control(UIAlignment.MIDDLELEFT)
                size(20, 20)
                x += px(32)
            }

            UIImageSheet().style("accordion-chevron") {
                control(UIAlignment.MIDDLERIGHT)
                size(24, 24)
                x -= px(32)
                imageColor = colorOf(214, 214, 214)
            }

            UIContainerSheet().style("accordion-content") {
                size(rel(1), px(200))
                background(asRGBA(1f, 1f, 0f, 0.3f))
                clipContent = true
            }

            val accordion = component(UIAccordion()) {
                informationContainer = UITriFunction { item, title, description ->
                    val container = container {
                        applyStyle("accordion-information")
                        image("gradient/folder", "accordion-icon")
                        label(title, "accordion-title")
//                        label(description, "accordion-description")
                        onMousePressed { changeDropdown(item) }
                    }

                    return@UITriFunction container
                }

                contentContainer = Function {
                    return@Function UIContainer<UIContainerSheet>().apply {
                        applyStyle("accordion-content")
                    }
                }

                contentChange = BiConsumer { item, shouldCollapse ->
                    if (shouldCollapse) {
                        item.content.style.height = px(0)
                    } else {
                        item.content.style.height = px(200)
                    }
                    updateLayout()
                }

                item("Some name", "some description", true) {
                    button("This is a button of some sorts").style {
                        size(400, 40)
                        background(asRGBA(0f, 0f, 0f, 0.3f), 8)
                        font("Montserrat-regular", colorOf(-1), px(16))
                    }
                }

                item("Some name", "some description", true) {
                    button("This is a button of some sorts").style {
                        size(400, 40)
                        background(asRGBA(0f, 0f, 0f, 0.3f), 8)
                        font("Montserrat-regular", colorOf(-1), px(16))
                    }
                }


//                ucreate {
//                    container("accordion-information") {
//                        image("accordion-icon", "accordion-icon")
//                        label("Some text", "accordion-title")
//                        image("accordion-chevron", "accordion-chevron")
//                    }
//                }
//                item()
//                item("I like trains btw", "", true, { name: String, description: String ->
//                    image("accordion-icon", "accordion-icon")
//                    label(name, "accordion-title")
//                    image("accordion-chevron", "accordion-chevron")
//                }) {
//                    applyStyle("accordion-content")
//                }
//                item("I like trains btw", "", true, { name: String, description: String ->
//                    applyStyle("accordion-information")
//                    image("accordion-icon", "accordion-icon")
//                    println(name)
//                    label(name, "accordion-title")
//                    image("accordion-chevron", "accordion-chevron")
//                }) {
//                    applyStyle("accordion-content")
//                }

            }.style {
                background(-1, 9)
                control(UIAlignment.CENTER)
                size(600, 400)
                    useFBO = true
                verticalScrollbar {
                    width = px(4)
                    height = rel(1)
                    color = colorOf(255, 0, 0)
                    radius = radiusOf(4)
                }
            }
        }
    }
}