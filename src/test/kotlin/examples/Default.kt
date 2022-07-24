package examples

import examples.deps.Generic
import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.animation.ease.impl.UIQuart
import net.prismclient.aether.ui.component.type.image.UIImageSheet
import net.prismclient.aether.ui.component.type.layout.UIAccordion
import net.prismclient.aether.ui.component.type.layout.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.UIContainer
import net.prismclient.aether.ui.component.type.layout.UIContainerSheet
import net.prismclient.aether.ui.component.util.enums.UIAlignment
import net.prismclient.aether.ui.dsl.UIAssetDSL
import net.prismclient.aether.ui.dsl.UIComponentDSL
import net.prismclient.aether.ui.renderer.UIProvider
import net.prismclient.aether.ui.screen.UIScreen
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.extensions.*
import net.prismclient.aether.ui.util.interfaces.UITriFunction
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

            val accordionOpen = UIContainerSheet().style("accordion-content") {
                size(rel(1), px(200))
                background(asRGBA(1f, 1f, 0f, 0.3f))
                clipContent = true
            }

            val accordionClose = UIContainerSheet().style("accordion-closed") {
                size(rel(1), px(0))
            }

            // TODO: add style name as a parameter for keyframes

            animationOf("accordion-open", UIContainerSheet()) {
                keyframe(UIQuart(1000L), accordionOpen)
            }

            animationOf("accordion-close", UIContainerSheet()) {
                keyframe(UIQuart(1000L), accordionClose)
                onCompletion {

                }
            }

            val accordion = component(UIAccordion()) {
                informationContainer = UITriFunction { item, title, description ->
                    val container = container {
                        applyStyle("accordion-information")
                        image("accordion-icon", "accordion-icon")
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
                    if (!shouldCollapse) {
                        item.content.style.height = px(0)
                        item.content.update()
//                        UIProvider.dispatchAnimation("accordion-close", item.content)
                    } else {
                        item.content.style.height = px(200)
//                        UIProvider.dispatchAnimation("accordion-open", item.content)
                        item.content.update()
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
                item("Some name", "some description", false) {
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
            }.style {
                background(-1, 9)
                control(UIAlignment.CENTER)
                width = px(600)
            }
        }
    }
}