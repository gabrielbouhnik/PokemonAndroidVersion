package com.pokemon.android.version.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.level.OpponentTrainerEntity
import com.pokemon.android.version.utils.JsonFileToString

class BattleFrontierTrainerRepository : Repository<OpponentTrainerEntity> {
    override fun loadData(activity: MainActivity): List<OpponentTrainerEntity> {
        val trainerJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.TRAINER_BATTLE_FRONTIER_PATH
        ) ?: return ArrayList()
        val gson = Gson()
        val result: List<OpponentTrainerEntity>
        val trainers = object : TypeToken<List<OpponentTrainerEntity>>() {}.type
        result = gson.fromJson(trainerJsonString, trainers)
        return result
    }
}