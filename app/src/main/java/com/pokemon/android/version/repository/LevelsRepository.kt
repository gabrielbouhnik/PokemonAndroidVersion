package com.pokemon.android.version.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.level.LevelsEntity
import com.pokemon.android.version.utils.JsonFileToString

class LevelsRepository {
    fun loadData(activity: MainActivity): LevelsEntity? {
        val levelsJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.LEVELS_DATA_PATH
        ) ?: return null
        val gson = Gson()
        val levels = object : TypeToken<LevelsEntity>() {}.type
        return gson.fromJson(levelsJsonString, levels)
    }
}