# Fonts
Aether supports (basically) font families and is designed to work with ttf fonts. There are a total of 5 types of fonts
which can be stored: regular, black, bold, light, and thin.Every font created can have an italic version of it
associated with it.

# Font Format
As mentioned by [implementation notes](#implementation-notes), fonts are saved in a HashMap with the key being the font. They are saved by the name of the font family plus the prefix '-', the type, and then another prefix denoding if it is italic or not. This is how it is loaded by default. However, if you ever intend on changing it, change the names


# Declaring a font
Declaring a font is easy.

# Implementation notes

- UIRenderer contains a HashMap, containing the name of each individual fonts
  - This includes normal, and italic as separate fonts
  - Fonts are formatted by their type e.g "bold", "thin" etc... ("normal" is just the font name)
    - The format is "$fontName-$fontType"
    - Then followed by "-italic" (if applicable)