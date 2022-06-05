package com.pokemon.android.version

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.entity.save.TrainerSave
import com.pokemon.android.version.model.Gender
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.utils.JsonFileToString

class SaveManager {
    companion object {
        private const val SAVE_FILE_PATH = "save.json"
        fun loadSave(activity : MainActivity) : Trainer?{
            var jsonString = JsonFileToString.loadJsonStringFromInternalStorage(activity,SAVE_FILE_PATH) ?: return null
            val gson = Gson()
            val data = object : TypeToken<TrainerSave>() {}.type
            Log.d("save:", jsonString)
            var trainerSave : TrainerSave = gson.fromJson(jsonString, data)
            var trainer = Trainer(trainerSave.name, Gender.valueOf(trainerSave.gender))
            trainer.coins = trainerSave.coins
            trainer.progression = trainerSave.progression
            trainerSave.items.forEach{ trainer.items[it.itemId] = it.quantity }
            trainerSave.team.forEach{
                var pokemon : Pokemon = Pokemon.of(it, activity.gameDataService,trainer)
                trainer.pokemons.add(pokemon)
                trainer.team.add(pokemon)}
            trainerSave.pokemons.forEach{trainer.pokemons.add(Pokemon.of(it, activity.gameDataService,trainer))}
            return trainer
        }

        fun save(activity : MainActivity){
            var trainerSave : TrainerSave = TrainerSave.of(activity.trainer!!)
            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            val jsonSave: String = gsonPretty.toJson(trainerSave)
            activity.openFileOutput(SAVE_FILE_PATH, Context.MODE_PRIVATE).use { output ->
                output.write(jsonSave.toByteArray())
            }
        }
    }
}