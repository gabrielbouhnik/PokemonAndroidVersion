package com.pokemon.android.version.utils

import com.pokemon.android.version.MainActivity
import java.io.IOException

class JsonFileToString {
    companion object {
        fun loadJsonString(activity: MainActivity, path: String): String? {
            val jsonString: String
            try {
                jsonString = activity.assets.open(path).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                return null
            }
            return jsonString
        }
    }
}