# Aether UI


<img src="/docs/assets/client-logo-rounded.png" align="right" width="230" height="230">
 
**Aether is a UI component library engine for Minecraft** and LWJGL projects (or anything really). You can create your own renderer implementation or use the default implementation with *[NanoVG](https://github.com/memononen/nanovg "An anti-aliased vector graphics library")*, which is an **Anti-Aliased vector graphics library**. **Please note the project is in the early stages of development.** Bugs may arise, and there might be incomplete/missing features. I have not chosen a license for the project. The discord server for the parent project can be found [here](https://discord.gg/jg3aWfASPp).

In a future release, the ability to import designs directly from figma, and to load actually css will be added.

Todo add screenshots here!!!


# Getting started

Import the project into your IDE using the following buildscript: 

<details>
<summary>Gradle</summary>
 
```java
repositories {
  maven { url 'https://jitpack.io' }
}
 
dependencies {
  implementation 'com.github.Prism-Client:Aether-UI:VERSION' 
}
```
 
</details>

<details>
<summary>Maven</summary>

Image using maven

</details>

---

Once completed, take a look at the [docs](https://github.com/Prism-Client/Aether-UI/tree/master/docs) or the [example screen!](https://github.com/Prism-Client/Aether-UI/blob/master/src/test/kotlin/net/prismclient/aether/ExampleScreen.kt) Please note, as mentioned above there might be a lot of lacking information as it is in the early stage of development.

# Development

The project board can be found [here](https://trello.com/b/g4Nvdykx/aether)
