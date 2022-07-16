package net.prismclient.aether.ui.renderer

import net.prismclient.aether.ui.Aether
import net.prismclient.aether.ui.animation.UIAnimation
import net.prismclient.aether.ui.component.UIComponent
import net.prismclient.aether.ui.renderer.image.UIImageData
import net.prismclient.aether.ui.style.UIStyleSheet
import net.prismclient.aether.ui.style.util.UIFontFamily
import net.prismclient.aether.ui.util.warn

/**
 * [UIProvider] handles style sheets, images, fonts, and animations, a.k.a. resources
 * which the rest of engine rely on.
 *
 * @author sen
 * @since 1.0
 */
object UIProvider {
    /**
     * The maximum time in milliseconds that an animation can live.
     */
    private const val MAX_ANIMATION_DURATION = 25000L

    lateinit var render: UIRenderer

    val styles = HashMap<String, UIStyleSheet>()
    val images = HashMap<String, UIImageData>()
    val fonts = HashMap<String, UIFontFamily>()
    val animations = HashMap<String, UIAnimation<*>>()

    /**
     * Initializes the UIProvider
     *
     * @param renderer The rendering api used to rendering content onto the screen
     */
    fun initialize(renderer: UIRenderer) {
        render = renderer
    }

    fun registerImage(name: String, image: UIImageData) {
        images[name] = image
    }

    fun getImage(name: String) = images[name]

    fun deleteImage(name: String) = images.remove(name)

    /**
     * Returns the name of the images given the [UIImageData]
     */
    fun getImageName(imageData: UIImageData): String? {
        for ((name, image) in images) {
            if (image == imageData)
                return name
        }
        return null
    }

    /**
     * Adds a [UIFontFamily] into the fonts arraylist. If fonts are loaded by a
     * string instead of a UIFontType, it will automatically be loaded into memory.
     * If it was loaded with a UIFontType, load must be explicitly called.
     *
     * @see UIFontFamily
     */
    fun registerFont(fontFamily: UIFontFamily) {
        if (fonts[fontFamily.familyName] == null) {
            fonts[fontFamily.familyName] = fontFamily
        }
    }

    fun registerStyle(style: UIStyleSheet) {
        styles[style.name] = style
    }

    @JvmOverloads
    fun getStyle(styleName: String, original: Boolean = false): UIStyleSheet {
        val style = styles[styleName]
            ?: throw NullPointerException("Style of $styleName was not found. Have you created it yet?")
        return if (original) style else style.copy()
    }

    /**
     * Deletes all mutable styles
     */
    fun resetStyles() {
        styles.entries.removeIf { !it.value.immutableStyle }
    }

    fun registerAnimation(animationName: String, animation: UIAnimation<*>) {
        animations[animationName] = animation
    }

    fun <S : UIStyleSheet> dispatchAnimation(animationName: String, component: UIComponent<S>) {
        val animation: UIAnimation<S>? = animations[animationName]?.copy() as? UIAnimation<S>
        if (animation == null) {
            warn("Animation of name [$animationName] was not found")
            return
        }
        animation.start(component)
    }

    /**
     * Returns a list of [UIComponent] with the parent of the provided [UIComponent<T>]
     */
    fun getChildrenOf(component: UIComponent<*>): List<UIComponent<*>> =
        Aether.instance.components?.filter { it.parent == component }?.toList() ?: mutableListOf()
}