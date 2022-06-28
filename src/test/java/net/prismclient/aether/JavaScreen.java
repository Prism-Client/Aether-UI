package net.prismclient.aether;

import net.prismclient.aether.ui.screen.UIScreen;

/**
 * This is an example of creating a screen with Java instead of Kotlin.
 * This is just an example, the code will not compile because of the missing style sheets
 */
public class JavaScreen implements UIScreen {
    @Override
    public void build() {
        // Create the build block (pretty much required)
        // build {}
//        UIComponentDSL.build((dsl) -> {
//            // The best way is to define it as follows and avoid using KFunction, and instead
//            // saving it as a local variable to set the properties (see below).
//            UIButton<UIStyleSheet> button = dsl.button("Text", "btn");
//
//            button.style.size(100, 100);
//
//            // Using KFunction (not suggested)
//            // button(..., ...) {}
//            dsl.button("Other Button", "btn", (otherButton) -> {
//                // style {}
//                ComponentKt.style(otherButton, (style) -> {
//                    style.size(100, 100);
//                    return null;
//                });
//                return null;
//            });
//
//            return null;
//        });

    }
}
