package com.pokemon.android.version

import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.item.Item
import com.pokemon.android.version.model.move.Move

class GameDataService {
    var items : List<Item> = ArrayList()
    var moves : List<Move> = ArrayList()
    var pokemons : List<PokemonData> = ArrayList()

    fun loadGameData(){

    }
}