package com.pokemon.android.version.model

import com.pokemon.android.version.model.item.Item
import lombok.Setter

@Setter
class Character {
    var name : String = ""
    var pokemons : List<Pokemon> = ArrayList()
    var gender : Gender? = null
    var team : List<Pokemon> = ArrayList()
    var items : List<Item> = ArrayList()
    val progression : Int = 0
    val coins : Int = 0

    fun save(){

    }
}