package com.pokemon.android.version.entity

class PokemonEntity (val id : Int, val name : String, val type1 : Int, val type2 : Int,
                     possible_moves : List<PokemonMoveEntity>, hp : Int, attack : Int,
                     defense : Int, spAtk : Int ,spDef : Int,speed : Int, catchRate : Int){
}