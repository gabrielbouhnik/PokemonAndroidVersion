package com.pokemon.android.version.model.move.pokemon

import com.pokemon.android.version.model.move.Move

open class PokemonMove(
    var move: Move,
    var pp: Int,
    var disabled: Boolean
) {

    constructor(move: Move) : this(move, move.pp, false)

    companion object {
        fun of(moveId: Int, moves: List<Move>): PokemonMove {
            val move = moves.first { it.id == moveId }
            return PokemonMove(move, move.pp, false)
        }
    }
}