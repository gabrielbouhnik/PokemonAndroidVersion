package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.Pokemon

class PokemonSave(
    var id: Int,
    var gender: String,
    var status: String,
    var level: Int,
    var hp: Int,
    var attack: Int,
    var defense: Int,
    var spAtk: Int,
    var spDef: Int,
    var speed: Int,
    var currentHP: Int,
    var currentExp: Int,
    var moveids: List<MoveSave>,
    var isFromBanner : Boolean,
) {
    companion object {
        fun of(pokemon: Pokemon): PokemonSave {
            var moveIds : ArrayList<MoveSave> = ArrayList()
            moveIds.add(MoveSave(pokemon.move1.move.id, pokemon.move1.pp))
            if (pokemon.move2 != null)
                moveIds.add(MoveSave(pokemon.move2!!.move.id, pokemon.move2!!.pp))
            if (pokemon.move3 != null)
                moveIds.add(MoveSave(pokemon.move3!!.move.id, pokemon.move3!!.pp))
            if (pokemon.move4 != null)
                moveIds.add(MoveSave(pokemon.move4!!.move.id, pokemon.move4!!.pp))
            return PokemonSave(
                pokemon.data.id,
                pokemon.gender.toString(),
                pokemon.status.toString(),
                pokemon.level,
                pokemon.hp,
                pokemon.attack,
                pokemon.defense,
                pokemon.spAtk,
                pokemon.spDef,
                pokemon.speed,
                pokemon.currentHP,
                pokemon.currentExp,
                moveIds,
                pokemon.isFromBanner
            )
        }
    }
}