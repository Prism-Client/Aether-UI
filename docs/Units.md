# Units

Units are a fundamental part of Aether UI. From positions to color units play the major role in controlling what a 
components do.

---
# Defining units

Units are easy to define. There are multiple ways to define a unit.

#### The suggested way: 
```kotlin
val pixels = px(50) // 50 Pixels !
val relative = percent(50) // 50 percent of the parent's corresponding value. (See below for more information)
val em = em(2f) // 2 times the font size
val ascender = ascender(1f) // The height of the ascender line (see below)
val descender = descender(1f) // The height of the descender line (see below)
```

#### The not-suggest-way-and-you-shouldnt-do-this way:
```kotlin
val pixels = UIUnit(50, PIXELS) // Refer to [UIUnitType] for all the Unit types
/** And it goes on, you get the idea **/
```

The function `px(value: Number)` is pretty straight forward, it just represents the given value in pixels, but what do 
the others mean?

_All functions above accept a `Number`, which is converted into a `Float`_

`pixels()`: Pixels on the screen <br>
`percent()`: Value / 100. The output of that equation times the unit that it corrisponds to. For example, if the unit is 
assigned to x, then it would be the parent's width of the component times the equation. So, if the parent's width is 
1000, and the value given is 10, it would become 0.1, which then is 1000 * 0.1, so the output of the unit is 100. This 
unit is very useful for scaling to multiple screen resolutions.<br>
`em()`: Font size (of the component) * value <br>
`ascender(), descender()` the ascender or descender * value. Learn more about font metrics [here](https://en.wikipedia.org/wiki/Ascender_(typography)#/media/File:Typography_Line_Terms.svg).

You may come across custom units designed specifically for a UIComponent, like `PXANIMRELATIVE` which is a unique type 
for Animations. You learn animations [here](/docs/Animation.md), how custom units work [here](#custom-units), and how
to create your own units.

## Operations with units

Aether comes along with a custom type of unit which allows you to apply operations to units. It's especially easy with 
Kotlin using operator functions. Defining operations is as simple as if you were going to do number operations, except 
instead of numbers, it's units

```kotlin
val addition = px(50) + px(50) // 50 + 50 = 100px
val subtraction = px(50) - px(50) // 50 - 50 = 0px
val multiplication = rel(10) * px(2) // 10 * 2 = 20px
val division = px(100) / px(2) // 100 / 2 = 50px
```

They can also be chained:
```kotlin
val chainedOperation = px(100) - px(10) + px(50) // 100 - 10 + 50 = 140px
```

Chained units follow order of operation:
```kotlin
val orderOfOperations = px(100) - px(10) / px(2) // 100 - (10 / 2) = 100 - 5 = 95
```

Unfortunately, Java doesn't provide the ability to overload operators.

```java
class Operations {
    /* 50 - 10 = 40px */
    UIUnit operation1 = operation(px(50), px(10), UIOperation.SUBTRACT);
    /* (100 - 10) / 2 = 45px */
    UIUnit operation2 = operation(operation(px(100), px(10), SUBTRACT), px(2), DIVIDE);
    /* 100 - (10 / 2) = 95px */
    UIUnit operation3 = operation(px(100), operation(px(10), px(2), DIVIDE), SUBTRACT);
}
```

Keep in mind, however you wrote it is how aether interprets it. It will look at the first unit, then the second unit 
regardless of order of operations. You can use the sort method to fix this:

```java
// Operation defined above
// Before: (100 - 10) / 2 = 45px
// After: 100 - (10 / 2) = 95px
// Now operation2 is identical to operation3!
operation2.sort();
```

---

# What happens behind the scenes?

To be done.

# Custom Units

Some styles, such as UIAnimationSheet have their own unique units. In the case of animations, the units are needed to 
tell aether that you want it to be relative to the component's position, instead of an actual position on screen. The 
UIUnit class represents the type as a Byte, so to create a unit, simply create a new unit that isn't one of the already 
used one in the UIUnitType (any number within a byte that is greater than 14).
