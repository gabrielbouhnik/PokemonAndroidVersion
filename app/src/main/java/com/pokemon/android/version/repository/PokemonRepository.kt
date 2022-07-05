package com.pokemon.android.version.repository

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService.Companion.POKEMON_DATA_PATH
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.pokemon.PokemonDataEntity
import com.pokemon.android.version.utils.JsonFileToString

class PokemonRepository : Repository<PokemonDataEntity> {
    override fun loadData(activity: MainActivity): List<PokemonDataEntity> {
        val pokemonsJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            POKEMON_DATA_PATH
        ) ?: return ArrayList()
        val gson = Gson()
        val result: List<PokemonDataEntity>
        val pokemons = object : TypeToken<List<PokemonDataEntity>>() {}.type
        result = gson.fromJson(pokemonsJsonString, pokemons)
        result.forEach { pkmn -> Log.d("pokemon name:", pkmn.name) }
        return result
    }
}