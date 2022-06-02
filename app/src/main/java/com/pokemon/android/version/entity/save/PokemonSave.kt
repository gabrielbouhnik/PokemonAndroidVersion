package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.Pokemon

class PokemonSave(
    var id: Int,
    var gender: String,
    var status: String,
    var level: Int,
    var hp: Int,
    var attack: Int = 0,
    var defense: Int = 0,
    var spAtk: Int = 0,
    var spDef: Int = 0,
    var speed: Int = 0,
    var currentHP: Int,
    var moveids: List<MoveSave>
) {
    companion object {
        fun of(pokemon: Pokemon): PokemonSave {
            var moveids : ArrayList<MoveSave> = ArrayList()
            moveids.add(MoveSave(pokemon.move1.move.id, pokemon.move1.pp))
            if (pokemon.move2 != null)
                moveids.add(MoveSave(pokemon.move2!!.move.id, pokemon.move2!!.pp))
            if (pokemon.move3 != null)
                moveids.add(MoveSave(pokemon.move3!!.move.id, pokemon.move3!!.pp))
            if (pokemon.move4 != null)
                moveids.add(MoveSave(pokemon.move4!!.move.id, pokemon.move4!!.pp))
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
                moveids
            )
        }
    }
}