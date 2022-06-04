package com.pokemon.android.version.model

class Trainer {
    var name : String = ""
    var pokemons : ArrayList<Pokemon> = ArrayList()
    var gender : Gender? = null
    var team : ArrayList<Pokemon> = ArrayList()
    var items : HashMap<Int, Int> = HashMap()
    var progression : Int = 0
    var coins : Int = 50

    constructor(name: String, gender: Gender){
        this.name = name
        this.gender = gender
    }

    fun getFirstTeamMember() : Pokemon?{
        for (pokemon in team){
            if (pokemon.currentHP > 0)
                return pokemon
        }
        return null
    }

    fun catchPokemon(pokemon : Pokemon){
        pokemon.trainer = this
        pokemons.add(pokemon)
        if (this.team.size < 6)
            team.add(pokemon)
    }

    fun addItem(id : Int, quantity : Int){
        if (items.contains(id))
            items[id] = items[id]!!.plus(quantity)
        else
            items[id] = quantity
    }

    fun canStillBattle() : Boolean{
        for (pokemon in team){
            if (pokemon.currentHP > 0)
                return true
        }
        return false
    }
}