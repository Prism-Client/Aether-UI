# Aether UI

<img src="/assets/client-logo-rounded.png" align="right" width="230" height="230">

**Aether is a UI component library engine for Minecraft** and LWJGL projects (or anything really). You can create your
own renderer implementation or use the default implementation
with *[NanoVG](https://github.com/memononen/nanovg "An anti-aliased vector graphics library")*, which is an **
Anti-Aliased vector graphics library**. **Please note the project is in the early stages of development.** Bugs may
arise, and there might be incomplete/missing features. The library is licensed under GPL-v2.0 license.

Ready to get started? [Check out the docs!](https://aether.prismclient.net/)

# Including the project

<details>

<summary>Gradle</summary>

```groovy
repositories {
  maven { url "https://jitpack.io" }
}

dependencies {
  implementation "com.github.Prism-Client:Aether-UI:Release"
}
```

</details>

<details>

<summary>Maven</summary>

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
 </repositories>
<dependencies>
    <dependency>
        <groupId>com.github.Prism-Client</groupId>
        <artifactId>Aether-UI</artifactId>
        <version>Release</version>
    </dependency>
</dependencies>
```

</details>

# Motivations

Personally, the greatest feat in making a Minecraft client is making appealing, flexible, and highly customizable UI.
Very few are able to reach even 2/3 of my goals. I found that popular clients lacked quality (anti-aliasing) in their
client, and in other cases, it simply wasn't that appealing (though that might be more related to the designer). Around
the time of the intial creation of this library, I picked up Kotlin. Kotlin introduces a ton of useful features
especially the DSL feature. It makes creating UIs just feel a whole lot easier. Because of it, I decided to design it
with Kotlin as the main programming language. Java is still supported, however I <ins>highly</ins> suggest for you to
use Kotlin.

# Design

I initially created the engine to act a bit like HTML and CSS, where you would create "style sheets" and define them as
components. This design, dare I say pattern, is essentially is how it is today. However, that is about the only thing
related to web stuff.

# Development

The project board can be found [here](https://trello.com/b/g4Nvdykx/aether)
