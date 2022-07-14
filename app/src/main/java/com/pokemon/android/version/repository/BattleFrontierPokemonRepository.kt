package com.pokemon.android.version.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.battlefrontier.BattleFrontierPokemonEntity
import com.pokemon.android.version.utils.JsonFileToString

class BattleFrontierPokemonRepository : Repository<BattleFrontierPokemonEntity>  {
    override fun loadData(activity: MainActivity): List<BattleFrontierPokemonEntity> {
        val pokemonsJsonString: String = JsonFileToString.loadJsonStringFromAssets(
            activity,
            GameDataService.POKEMON_BATTLE_FRONTIER_PATH
        ) ?: return ArrayList()
        val gson = Gson()
        val result: List<BattleFrontierPokemonEntity>
        val pokemons = object : TypeToken<List<BattleFrontierPokemonEntity>>() {}.type
        result = gson.fromJson(pokemonsJsonString, pokemons)
        return result
    }
}