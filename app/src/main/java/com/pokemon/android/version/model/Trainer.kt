package com.pokemon.android.version.model

import com.pokemon.android.version.model.item.Item

class Trainer {
    val name : String = ""
    var pokemons : ArrayList<Pokemon> = ArrayList()
    val gender : Gender? = null
    var team : ArrayList<Pokemon> = ArrayList()
    var items : ArrayList<Item> = ArrayList()
    val progression : Int = 0
    val coins : Int = 0

    constructor(name: String, gender: Gender)

    fun catchPokemon(pokemon : Pokemon){
        pokemon.trainer = this
        pokemons.add(pokemon)
        if (this.team.size < 6)
            team.add(pokemon)
    }


    fun save(){

    }


}