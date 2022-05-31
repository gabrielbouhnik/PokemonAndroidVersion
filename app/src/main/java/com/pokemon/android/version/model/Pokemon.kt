package com.pokemon.android.version.model

import com.pokemon.android.version.model.move.Move

class Pokemon : PokemonData() {
    val trainer : Character? = null
    val level : Int = 1
    val move1 : Move? = null
    val move2 : Move? = null
    val move3 : Move? = null
    val move4 : Move? = null
    val status : Status = Status.OK
    var gender : Gender? = null
}