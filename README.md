# Aether UI

<img src="/assets/client-logo-rounded.png" align="right" width="230" height="230">

Aether is a UI component engine for Minecraft. It's designed with Kotlin, Figma, and Minecraft in mind. Aether is
designed to allow you to customize the renderer to your own, or the default one
with *[NanoVG, an anti-aliased 2D vector graphics library](https://github.com/memononen/nanovg "An anti-aliased vector graphics library")*.
As mentioned before, Aether is designed with Figma in mind. Features such as [Auto Layouts](https://help.figma.com/hc/en-us/articles/360040451373-Explore-auto-layout-properties)
and [Figma Font](https://help.figma.com/hc/en-us/articles/360039956434-Getting-started-with-text) are implemented to
streamline the process of creating UIs from Figma. The library is licensed under GPL-v2.0 license.

Convinced? [Check out the docs!](https://aether.prismclient.net/)

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
the time of the initial creation of this library, I picked up Kotlin. Kotlin introduces a ton of useful features
especially the DSL feature. It makes creating UIs just feel a lot easier. Because of it, I decided to design it
with Kotlin as the main programming language. Java is still supported, however I <ins>highly</ins> suggest for you to
use Kotlin.

# Design

I initially created the engine to act a bit like HTML and CSS, however, I decided to swerve from that. I kept the idea
of styles, however, a lot of things are different from web. After all, it was initially designed for newspapers! (Obviously,
it's changed a lot since then). Because I had Figma in mind, quite a few different features (such as Auto Layouts and Text) are
somewhat "loosely" based on Figma. 

# Development

The project board can be found [here](https://trello.com/b/g4Nvdykx/aether)
