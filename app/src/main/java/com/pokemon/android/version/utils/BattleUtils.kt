package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.move.Move

class BattleUtils {
    companion object {
        fun trainerStarts(pokemon: Pokemon, other: Pokemon, move: Move): Boolean {
            if (move.priorityLevel > other.IA(other).move.priorityLevel){
                return true
            }
            else if (move.priorityLevel < other.IA(other).move.priorityLevel){
                return false
            }
            else{
                val paralysisMultiplicator : Float = if (pokemon.status == Status.PARALYSIS) 2f else 1f
                return pokemon.speed.toFloat() * pokemon.battleData!!.speedMultiplicator * paralysisMultiplicator >= other.speed.toFloat() * other.battleData!!.speedMultiplicator
            }
        }
    }
}