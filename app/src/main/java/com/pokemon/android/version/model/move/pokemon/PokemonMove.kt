package com.pokemon.android.version.model.move.pokemon

import com.pokemon.android.version.model.move.Move

open class PokemonMove (
    var move : Move,
    var pp : Int) {

    constructor(move : Move) : this(move, move.pp)

    companion object {
        fun of(moveId: Int, moves: List<Move>): PokemonMove {
            var move = moves.filter { it.id == moveId }.first()
            return PokemonMove(move, move.pp)
        }
    }
}