package com.pokemon.android.version.model

class Trainer {
    var name : String = ""
    var pokemons : ArrayList<Pokemon> = ArrayList()
    var gender : Gender? = null
    var team : ArrayList<Pokemon> = ArrayList()
    var items : HashMap<Int, Int> = HashMap()
    var progression : Int = 0
    var coins : Int = 0

    constructor(name: String, gender: Gender){
        this.name = name
        this.gender = gender
    }

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