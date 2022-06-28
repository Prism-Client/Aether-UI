package net.prismclient.aether.ui.style.util

import net.prismclient.aether.ui.renderer.UIProvider

/**
 * [UIFontFamily] holds different weighted fonts of the same
 * family. Fonts are automatically loaded if the names are provided (as a string)
 * they will automatically be loaded. If the fonts are loaded with a [UIFontType],
 * the fonts must be explicitly loaded with [UIFontFamily].load()
 *
 * @author sen
 * @since 4/25/2022
 */
class UIFontFamily(
    val familyName: String,
    var regular: UIFontType?,
    var medium: UIFontType?,
    var black: UIFontType?,
    var bold: UIFontType?,
    var light: UIFontType?,
    var thin: UIFontType?
) {
    /**
     * Loads a font with the given [familyName]. The file location specifies
     * the local path to load the resources from. The type is the font name.
     * If left blank, it will not be loaded.
     */
    constructor(
        familyName: String,
        fontLocation: String,
        regular: String,
        medium: String,
        black: String,
        bold: String,
        light: String,
        thin: String
    ) : this(
        familyName,
        if (regular.isNotEmpty()) UIFontType("$familyName-regular", "$familyName-regular-italic") else null,
        if (medium.isNotEmpty()) UIFontType("$familyName-medium", "$familyName-medium-italic") else null,
        if (black.isNotEmpty()) UIFontType("$familyName-black", "$familyName-black-italic") else null,
        if (bold.isNotEmpty()) UIFontType("$familyName-bold", "$familyName-bold-italic") else null,
        if (light.isNotEmpty()) UIFontType("$familyName-light", "$familyName-light-italic") else null,
        if (thin.isNotEmpty()) UIFontType("$familyName-thin", "$familyName-thin-italic") else null,
    ) {
        this.regular?.load(fontLocation + regular)
        this.medium?.load(fontLocation + regular)
        this.black?.load(fontLocation + black)
        this.bold?.load(fontLocation + bold)
        this.light?.load(fontLocation + light)
        this.thin?.load(fontLocation + thin)
    }

    init {
        UIProvider.registerFont(this)
    }

    /**
     * Loads the active fonts of this [UIFontFamily] into memory
     */
    fun load() {
        regular?.attemptLoad()
        medium?.attemptLoad()
        black?.attemptLoad()
        bold?.attemptLoad()
        light?.attemptLoad()
        thin?.attemptLoad()
    }
}