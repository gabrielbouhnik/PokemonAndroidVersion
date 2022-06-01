package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.pokemon.PokemonMove

class PokemonData {
    val id : Int = 0
    val name: String = ""
    val type1 : Type? = null
    val type2 : Type? = null
    val possible_moves : List<PokemonMove> = ArrayList()
    val catchRate : Int = 0
    var hp : Int  = 0
    var attack : Int = 0
    var defense : Int = 0
    var spAtk : Int = 0
    var spDef : Int = 0
    var speed : Int = 0
}