package net.prismclient.aether.ui.component.type.layout.styles

import net.prismclient.aether.ui.style.UIStyleSheet

/**
 * [UIFrameSheet] is the corresponding style sheet for frames. See field
 * documentation for more information.
 *
 * @author sen
 * @since 1.0
 */
open class UIFrameSheet(name: String) : UIStyleSheet(name) {
    /**
     * If true, the frame will use an FBO to render content.
     */
    var useFBO: Boolean = false

    /**
     * If true certain optimizations will be applied when
     * rendering. This only works with [useFBO] as true.
     */
    var optimizeRenderer: Boolean = true

    override fun apply(sheet: UIStyleSheet): UIFrameSheet {
        // Override the default apply function because
        // this is an inheritable class.
        this.immutableStyle = sheet.immutableStyle
        this.name = sheet.name

        this.background = sheet.background?.copy()
        this.font = sheet.font?.copy()

        this.x = sheet.x?.copy()
        this.y = sheet.y?.copy()
        this.width = sheet.width?.copy()
        this.height = sheet.height?.copy()

        this.padding = sheet.padding?.copy()
        this.margin = sheet.margin?.copy()
        this.anchor = sheet.anchor?.copy()
        this.clipContent = sheet.clipContent

        // Frame properties
        if (sheet is UIFrameSheet) {
            this.useFBO = sheet.useFBO
            this.optimizeRenderer = sheet.optimizeRenderer
        }

        return this
    }

    override fun copy(): UIFrameSheet = UIFrameSheet(name).apply(this)
}