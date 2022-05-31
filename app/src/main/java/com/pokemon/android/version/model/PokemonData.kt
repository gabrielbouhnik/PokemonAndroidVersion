package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.Move
import lombok.Getter

@Getter
open class PokemonData {
    val name: String = ""
    val type1 : Type? = null
    val type2 : Type? = null
    val possible_moves : List<Move> = ArrayList()
    val catch_rate : Int = 0
}