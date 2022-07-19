package com.pokemon.android.version.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.achievement.AchievementEntity
import com.pokemon.android.version.entity.banner.BannerEntity
import com.pokemon.android.version.utils.JsonFileToString

class AchievementRepository : Repository<AchievementEntity> {
    override fun loadData(activity: MainActivity): List<AchievementEntity> {
        val bannersJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.ACHIEVEMENTS_DATA_PATH
        ) ?: return ArrayList()
        val gson = Gson()
        val result: List<AchievementEntity>
        val achievements = object : TypeToken<List<AchievementEntity>>() {}.type
        result = gson.fromJson(bannersJsonString, achievements)
        return result
    }
}