package net.prismclient.aether.ui.theme

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

object ThemeManager {
    @JvmStatic
    fun loadTheme(filename: String): UITheme {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonObject = gson.fromJson(loadJSON(filename), JsonObject::class.java)
        val theme = gson.fromJson(jsonObject.get("properties").toString(), UITheme::class.java)
        theme.name = jsonObject.get("Name").asString
        theme.description = jsonObject.get("Description").asString
        return theme
    }

    @JvmStatic
    fun loadJSON(filename: String): String {
        val inputStream = ThemeManager::class.java.getResourceAsStream("/theme/$filename.json") ?: return ""
        return BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining(System.lineSeparator()))
    }
}