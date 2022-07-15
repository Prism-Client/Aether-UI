package net.prismclient.aether.ui.util.extensions

import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.component.type.UILabel
import net.prismclient.aether.ui.component.type.image.UIImage
import net.prismclient.aether.ui.component.type.input.button.UIButton
import net.prismclient.aether.ui.component.type.input.button.UICheckbox
import net.prismclient.aether.ui.component.type.layout.auto.UIAutoLayout
import net.prismclient.aether.ui.component.type.layout.container.UIContainer
import net.prismclient.aether.ui.component.type.layout.list.UIListLayout
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.util.*
import net.prismclient.aether.ui.util.timed
import net.prismclient.aether.ui.util.warn

import kotlin.reflect.KClass

internal val defaultStylesheets = mutableMapOf<KClass<out UIComponent<out UIStyleSheet>>, KClass<out UIStyleSheet>>()

internal inline fun <reified S : UIStyleSheet, reified C : UIComponent<out S>> registerDefaultStylesheet(` `: KClass<C>) {
    if (!defaultStylesheets.containsKey(C::class)) {
        if (S::class.java.constructors[0].parameterCount > 0) {
            throw IllegalStateException("Stylesheet ${S::class.simpleName} has no overload constructor. Expected ${S::class.simpleName}(name: String = \"\")")
        }
        defaultStylesheets += C::class to S::class
        debug("${C::class.simpleName} -> ${S::class.simpleName}")
    } else {
        warn("Component ${C::class.simpleName} has already been registered to ${S::class.simpleName}")
    }
}

fun registerStylesheets() {
    if (defaultStylesheets.isEmpty()) {
        timed("Registering default stylesheets") {
            registerDefaultStylesheet(UIAutoLayout::class)
            registerDefaultStylesheet(UIButton::class)
            registerDefaultStylesheet(UICheckbox::class)
            registerDefaultStylesheet(UIContainer::class)
            registerDefaultStylesheet(UIImage::class)
            registerDefaultStylesheet(UILabel::class)
            registerDefaultStylesheet(UIListLayout::class)
        }
    } else {
        warn("registerStylesheets() should only be called once.")
    }
}

/**
 *
 */
fun <T : UIStyleSheet> UIComponent<out UIStyleSheet>.inferredStyle(): T {
    if (defaultStylesheets.isEmpty()) {
        warn("registerStylesheets() should be called before creating UIs.", LogLevel.GLOBAL)
        registerStylesheets()
    }

    try {
        return defaultStylesheets[this::class]!!.java.constructors[0].newInstance() as T
    } catch (exception: Exception) {
        throw IllegalStateException("No default stylesheet registered for component ${this::class.simpleName}.")
    }
}

//inline fun <reified T : UIStyleSheet> inferredStyle(): T {
//    return T::class.java.constructors[0].newInstance() as T
//}
//
//inline fun <reified T : UIStyleSheet> inferredStyle(name: String = ""): T {
//    return T::class.java.constructors[0].newInstance(name) as T
//}
