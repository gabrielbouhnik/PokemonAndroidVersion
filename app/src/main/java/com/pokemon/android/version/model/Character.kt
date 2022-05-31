package com.pokemon.android.version.model

import com.pokemon.android.version.model.item.Item
import lombok.Getter
import lombok.Setter

@Getter
class Character {
    var name : String = ""
    var pokemons : List<Pokemon> = ArrayList()
    var gender : Gender? = null
    var team : List<Pokemon> = ArrayList()
    var items : List<Item> = ArrayList()
    @Setter
    val progression : Int = 0
    @Setter
    val coins : Int = 0

    constructor(name: String, gender: Gender)
    
    fun save(){

    }
}