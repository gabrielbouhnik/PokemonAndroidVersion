package com.pokemon.android.version

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.entity.save.TrainerSave
import com.pokemon.android.version.model.*
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.JsonFileToString
import java.text.SimpleDateFormat

class SaveManager {
    companion object {
        private const val SAVE_FILE_PATH = "save.json"
        fun loadSave(activity: MainActivity): Trainer? {
            val jsonString = JsonFileToString.loadJsonStringFromInternalStorage(activity, SAVE_FILE_PATH) ?: return null
            val gson = Gson()
            val data = object : TypeToken<TrainerSave>() {}.type
            Log.d("save:", jsonString)
            val trainerSave: TrainerSave = gson.fromJson(jsonString, data)
            val trainer = Trainer(trainerSave.name, Gender.valueOf(trainerSave.gender))
            trainer.coins = trainerSave.coins
            trainer.progression = trainerSave.progression
            trainer.eliteProgression = trainerSave.eliteProgression
            trainerSave.items.forEach { trainer.items[it.itemId] = it.quantity }
            trainerSave.team.forEach {
                val pokemon: Pokemon = Pokemon.of(it, activity.gameDataService, trainer)
                trainer.pokemons.add(pokemon)
                trainer.team.add(pokemon)
            }
            trainerSave.pokemons.forEach { trainer.pokemons.add(Pokemon.of(it, activity.gameDataService, trainer)) }
            if (trainerSave.lastTimeDailyHealUsed != null)
                trainer.lastTimeDailyHealUsed =
                    SimpleDateFormat("yyyy-MM-dd").parse(trainerSave.lastTimeDailyHealUsed!!)
            if (trainerSave.eliteMode != null)
                activity.eliteMode = trainerSave.eliteMode!!
            if (trainerSave.battleFactorySave != null)
                trainer.battleFactoryProgression =
                    BattleFrontierProgression.of(trainerSave.battleFactorySave!!, activity.gameDataService, trainer)
            if (trainerSave.battleTowerSave != null)
                trainer.battleTowerProgression =
                    BattleFrontierProgression.of(trainerSave.battleTowerSave!!, activity.gameDataService, trainer)
            if (trainer.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID) {
                trainer.achievements = if (trainerSave.achievements == null) Achievements() else trainerSave.achievements
                trainer.successfulAchievements =
                    if (trainerSave.successfulAchievements == null) arrayListOf() else trainerSave.successfulAchievements!!
            }
            return trainer
        }

        fun save(activity: MainActivity) {
            val trainerSave: TrainerSave = TrainerSave.of(activity.trainer!!, activity.eliteMode)
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val jsonSave: String = gsonPretty.toJson(trainerSave)
            activity.openFileOutput(SAVE_FILE_PATH, Context.MODE_PRIVATE).use { output ->
                output.write(jsonSave.toByteArray())
            }
        }
    }
}