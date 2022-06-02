package com.pokemon.android.version.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.move.MovesEntity
import com.pokemon.android.version.utils.JsonFileToString

class MovesRepository {
    fun loadData(activity: MainActivity): MovesEntity? {
        val movesJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.MOVES_DATA_PATH
        ) ?: return null
        val gson = Gson()
        val moves = object : TypeToken<MovesEntity>() {}.type
        return gson.fromJson(movesJsonString, moves)
    }
}