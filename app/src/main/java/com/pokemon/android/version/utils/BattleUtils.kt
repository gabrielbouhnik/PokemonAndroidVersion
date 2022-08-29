package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.move.Move

class BattleUtils {
    companion object {
        fun trainerStarts(pokemon: Pokemon, other: Pokemon, move: Move, opponentMove: Move): Boolean {
            return when {
                move.priorityLevel > opponentMove.priorityLevel -> {
                    true
                }
                move.priorityLevel < opponentMove.priorityLevel -> {
                    false
                }
                else -> {
                    val pokemonSpeed = pokemon.speed.toFloat() * pokemon.battleData!!.speedMultiplicator
                    val otherSpeed = other.speed.toFloat() * other.battleData!!.speedMultiplicator
                    val paralysisMultiplicator: Float = if (pokemon.status == Status.PARALYSIS) 0.5f else 1f
                    val opponentParalysisMultiplicator: Float = if (other.status == Status.PARALYSIS) 0.5f else 1f
                    pokemonSpeed * paralysisMultiplicator >= otherSpeed * opponentParalysisMultiplicator
                }
            }
        }
    }
}