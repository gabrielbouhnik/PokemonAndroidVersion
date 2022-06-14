package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MovesEntity

class MoveFactory {
    companion object {
        fun createMove(movesEntity: MovesEntity?) : List<Move>{
            var res : ArrayList<Move> = ArrayList()
            if (movesEntity != null) {
                movesEntity.basicMoves.forEach { res.add(Move.of(it)) }
                movesEntity.statusMoves.forEach { res.add(StatusMove.of(it)) }
                movesEntity.critMoves.forEach { res.add(CritMove.of(it)) }
                movesEntity.variableHitMoves.forEach { res.add(VariableHitMove.of(it))}
                movesEntity.statsChangesMoves.forEach { res.add(StatChangeMove.of(it))}
            }
            res.sortBy{it.id}
            return res
        }
    }
}