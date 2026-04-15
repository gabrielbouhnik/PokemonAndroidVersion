package com.pokemon.android.version.model.move.pokemon

import com.pokemon.android.version.model.move.Move

open class PokemonMove(
    var move: Move,
    var pp: Int,
    var disabledCountdown: Int
) {

    constructor(move: Move) : this(move, move.pp, 0)

    companion object {
        fun of(moveId: Int, moves: List<Move>): PokemonMove {
            val move = moves.first { it.id == moveId }
            return PokemonMove(move, move.pp, 0)
        }
    }

    fun reduceDisableCountdown() {
        if (disabledCountdown > 0) {
            disabledCountdown -= 1
        }
    }

    fun isDisabled() : Boolean {
        return disabledCountdown > 0
    }
}