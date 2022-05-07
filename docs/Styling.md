# Styling

As mentioned in the [README](/README.md), Styles allow you to "assign properties to component's via a file, or
directly in a class." With this being said, there are quite a few concepts which you need to know to create a style. You
can find them below:

# Style Units

Units are placed after an integer, or decimal. For example `50px` or `50%`. The default unit is `px`.

`px` - Specifies a pixel amount <br>
`percent` or `%` - Specifies a percentage amount. Scales to the parent's width: `50%` of the parent's width <br>
`em` - The font height times the amount. For example, if the `font-size` is 18, and the amount is 0.5, the value is 9

### Special Units
Some components use custom units.

- (Grid Layouts)[]
  - `fr`, `frac`, `fraction` - Represents the amount of leftover space divided by the amount of fractions.
  - `auto` - Scales to the leftover space
  - `inital` - The size is equal to the component's size for that axis


# Style Properties

There are quite a few properties that come along with styles:

### Plot

`x`:`unit` Specifies the x position <br>
`y`:`unit` Specifies the y position <br>
`position`:`unit, unit` Specifies the x, and y

`width`:`unit` Specifies the width <br>
`height`:`unit` Specifies the height <br>
`size`:`unit, unit` Specifies the width, and height

### Background

`background-color`:`Color` Specifies the background color of the component <br>
`background-selected`:`Color` Specifies the background selected color of the component <br>
`background-radius`:`Number` Specifies the radius of the background

### Borders

Borders outline the background. See [Border / Outlines](/docs/Renderer.md#border--outlines) for more information.

`border-width`:`Number` Specifies the width of the border <br>
`border-color`:`Color` Specifies the border color <br>
`border-radius`:`Number` Specifies the radius of the background (same as `background-radius`)

### Font

# Style Functions
`?` = Nullable (can be omitted) <br> <br>

`rgb(Number, Number, Number, Number? = 255)` or `rgba(Number, Number, Number, Number? = 255)`
