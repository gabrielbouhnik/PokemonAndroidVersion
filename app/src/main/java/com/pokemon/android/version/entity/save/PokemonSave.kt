package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.utils.MoveUtils

data class PokemonSave(
    var id: Int,
    var status: String,
    var level: Int,
    var currentHP: Int,
    var currentExp: Int,
    var moveids: List<MoveSave>,
    var shiny: Boolean = false,
    var isFromBanner: Boolean,
    var movesLearnedByTM: List<Int>,
) {
    companion object {
        fun of(pokemon: Pokemon): PokemonSave {
            return PokemonSave(
                pokemon.data.id,
                pokemon.status.toString(),
                pokemon.level,
                pokemon.currentHP,
                pokemon.currentExp,
                MoveUtils.getMoveList(pokemon).map { MoveSave(it.move.id, it.move.pp) },
                pokemon.shiny,
                pokemon.isFromBanner,
                pokemon.movesLearnedByTM.map{it.id}
            )
        }
    }
}