package net.prismclient.aether.ui.theme

import com.google.gson.annotations.SerializedName

class UITheme {
    @SerializedName("Name")
    var name = "UITheme"

    @SerializedName("Description")
    var description = "Description"

    @SerializedName("Primary Color")
    var primaryColor = 0
    @SerializedName("Secondary Color")
    var secondaryColor = 0

    /* Dependent Colors */
    @SerializedName("Hover Color")
    var hoverColor = 0
    @SerializedName("Hover Text Color")
    var hoverTextColor = 0

    @SerializedName("Disabled Color")
    var disabledColor = 0
    @SerializedName("Disabled Text Color")
    var disabledTextColor = 0


    /* Background */
    @SerializedName("Background Color")
    var backgroundColor = 0
    @SerializedName("Secondary Background Color")
    var secondaryBackgroundColor = 0
    @SerializedName("Button Background Color")
    var buttonBackgroundColor = 0

    /* Text */
    @SerializedName("Text Color")
    var textColor = 0

    @SerializedName("Text Secondary Color")
    var textSecondaryColor = 0

    @SerializedName("Text Spacing")
    var textSpacing = 0f // TODO: Text spacing serialization

    /* Font */
    @SerializedName("Small Font Size")
    var smallFontSize = 0f
    @SerializedName("Normal Font Size")
    var normalFontSize = 0f
    @SerializedName("Medium Font Siz")
    var mediumFontSize = 0f
    @SerializedName("Large Font Size")
    var largeFontSize = 0f
    @SerializedName("Extra Large Font Size")
    var extraLargeFontSize = 0f
    @SerializedName("Normal Font Face")
    var normalFontFace = ""
    @SerializedName("Bold Font Face")
    var boldFontFace = ""
    @SerializedName("Italic Font Face")
    var italicFontFace = ""

    /* Generic net.prismclient.aether.ui.Component Controls */
    @SerializedName("Button Radius")
    var buttonRadius = 0f

    @SerializedName("Icon Button Radius")
    var iconButtonRadius = 0f /* Other */ // deleted, modified, active, inactive etc...

    /* Other */
    @SerializedName("Ease Type")
    var easeType = "quint"
    @SerializedName("Ease Direction")
    var easeDirection = "inout"
}