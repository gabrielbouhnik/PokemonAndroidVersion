package com.pokemon.android.version.model

class Trainer {
    val name : String = ""
    var pokemons : ArrayList<Pokemon> = ArrayList()
    val gender : Gender? = null
    var team : ArrayList<Pokemon> = ArrayList()
    var items : HashMap<Int, Int> = HashMap()
    var progression : Int = 0
    var coins : Int = 0

    constructor(name: String, gender: Gender)

    fun catchPokemon(pokemon : Pokemon){
        pokemon.trainer = this
        pokemons.add(pokemon)
        if (this.team.size < 6)
            team.add(pokemon)
    }

    fun addItem(id : Int){
        if (items.contains(id))
            items[id] = items[id]!!.plus(1)
        else
            items[id] = 1
    }

    fun save(){

    }


}