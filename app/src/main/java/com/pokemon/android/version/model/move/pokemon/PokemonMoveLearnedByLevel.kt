package com.pokemon.android.version.model.move.pokemon

import com.pokemon.android.version.entity.pokemon.MoveLearnByLevelEntity
import com.pokemon.android.version.model.move.Move

class PokemonMoveLearnedByLevel(move : Move,
                                pp : Int,
                                val level : Int) : PokemonMove(move, pp){

    companion object {
        fun of(moveLearnByLevelEntity: MoveLearnByLevelEntity, moves: List<Move>): PokemonMoveLearnedByLevel {
            var move = moves.filter { it.id == moveLearnByLevelEntity.moveId }.first()
            return PokemonMoveLearnedByLevel(move, move.pp,moveLearnByLevelEntity.level)
        }
    }
}