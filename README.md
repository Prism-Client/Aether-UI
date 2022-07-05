# Aether UI

<img src="/docs/assets/client-logo-rounded.png" align="right" width="230" height="230">
 
**Aether is a UI component library engine for Minecraft** and LWJGL projects (or anything really). You can create your own renderer implementation or use the default implementation with *[NanoVG](https://github.com/memononen/nanovg "An anti-aliased vector graphics library")*, which is an **Anti-Aliased vector graphics library**. **Please note the project is in the early stages of development.** Bugs may arise, and there might be incomplete/missing features.


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
        <version>production-SNAPSHOT</version>
    </dependency>
</dependencies>
```
</details>


# Development

The project board can be found [here](https://trello.com/b/g4Nvdykx/aether)
