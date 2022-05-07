[![Not a web engine](/docs/assets/not-a-webengine.svg)]()
[![Works Sometimes](/docs/assets/works-sometimes.svg)]()

# README WIP SOME THINGS MIGHT BE INACCURATE

# Aether UI


<img src="/docs/images/client-logo.png" align="right" width="230" height="230">
 
Aether is anti-aliased UI engine designed for Minecraft, and general LWJGL. It is compatible with LWJGL 3, and legacy.
The engine follows an HTML/CSS declarative style where properties are defined within a css style file, and are
constructed with a build file. This allows extreme customization on the client side, although, if you have no interest
in allowing customization, the styles, and build data can be created within the code. Backend, calls are made
to *[NanoVG](https://github.com/memononen/nanovg "An anti-aliased vector graphics library")* which then renders the UI
onto the screen. However, LWJGL 2, unlike LWJGL 3, does not package NanoVG by default, and instead requires a bit of trickery to load.

# Getting started:

- [Basics](#basics)
  - [Components](/docs/Components.md#components)
    - [Border](/docs/Components.md#border) 
  - [Building](/docs/Building.md#building-components)
  - [Styling](/docs/Styling.md#styling)
    - [Style Units](/docs/Styling.md#style-units) 
    - [Style Properties](/docs/Styling.md#style-properties)
    - [Style Functions](/docs/Styling.md#style-functions)
  - [Rendering](/docs/Renderer.md#rendering)
    - [Outline](/docs/Renderer.md#border--outlines)
- [Importing Designs](/docs/Figma-Importer.md)
  - [Figma (W.I.P)](/docs/Figma-Importer.md#importing-from-figma)
- [Info](/docs/Info.md)
  - [What's new?](/docs/Info.md#whats-new)
  - [Changelog](/docs/Info.md#change-log)
  - [TODO](/docs/Info.md#todo)

# Basics:

Components all have their own styles. n nnnnnnnnnnn

<details>
  <summary>Styles</summary>

> Styles act similar to css. They allow you to assign properties to component's via a file, or directly in a class.
> They control properties such as the component's background color, or text size.

Take this example,

```
h1 {
    text-font-face: Bold;
    text-size: 48;
    text-color: rgb(255, 255, 255, 1.0);
}
```

A style is defined by its name followed by a block which contain the properties of the sheet by using curly brackets `{`
and `}`. Inside that block, a [property](docs/Styling.md#style-properties)
is defined by its name, followed by a colon `:` and the [property](docs/Styling.md#style-properties) intended on being
set to. There are a few different [units](docs/Styling.md#style-units) at your disposal, see them [here](docs/Styling.md#style-units). Finally, add a semicolon `;` to show that you are done writing the [property](docs/Styling.md#style-properties).

Sometimes, you might use a [function](docs/Styling.md#style-functions) to set a property as it can make it easier to
read, and write. An example is the `rgb` [function](docs/Styling.md#style-functions) shown
above. [Functions](docs/Styling.md#style-functions) can be declared by specifying the name, and then adding
parentheses `(` `)`. Inside those parentheses, parameters can be specified. To add multiple parameters, use a
comma `,` to add a new parameter.

</details>

<details>
  <summary>Parenting</summary>

> Components can be nested within other components, however the only change if the offset of the nested
> component's position. (Which is offset to it's parent's position). However, if the component is an instance of
> a `UICotainer` the child should be placed inside the UIContainer's component list, instead of the main component's list.

</details>


This project is All Rights Reserved. (We may change the licensing terms in the future.)
