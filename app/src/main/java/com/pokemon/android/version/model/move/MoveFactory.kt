package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MovesEntity

class MoveFactory {
    companion object {
        fun createMove(movesEntity: MovesEntity?): List<Move> {
            val res: ArrayList<Move> = ArrayList()
            if (movesEntity != null) {
                movesEntity.basicMoves.forEach { res.add(Move.of(it)) }
                movesEntity.variableHitMoves.forEach { res.add(VariableHitMove.of(it)) }
                movesEntity.statsChangesMoves.forEach { res.add(StatChangeMove.of(it)) }
                movesEntity.drainMoves.forEach { res.add(DrainMove.of(it)) }
                movesEntity.recoilMoves.forEach { res.add(RecoilMove.of(it)) }
                movesEntity.healMoves.forEach { res.add(HealMove.of(it)) }
            }
            res.sortBy { it.id }
            return res
        }
    }
}