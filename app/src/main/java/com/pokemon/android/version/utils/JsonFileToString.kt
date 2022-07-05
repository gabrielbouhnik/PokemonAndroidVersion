package com.pokemon.android.version.utils

import com.pokemon.android.version.MainActivity
import java.io.IOException

class JsonFileToString {
    companion object {
        fun loadJsonStringFromAssets(activity: MainActivity, path: String): String? {
            val jsonString: String
            try {
                jsonString = activity.assets.open(path).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                return null
            }
            return jsonString
        }

        fun loadJsonStringFromInternalStorage(activity: MainActivity, path: String): String? {
            val stringBuilder = StringBuilder()
            try {
                activity.openFileInput(path).use { stream ->
                    val text = stream.bufferedReader().use {
                        it.readText()
                    }
                    stringBuilder.append(text)
                }
            } catch (ioException: IOException) {
                return null
            }
            return stringBuilder.toString()
        }
    }
}