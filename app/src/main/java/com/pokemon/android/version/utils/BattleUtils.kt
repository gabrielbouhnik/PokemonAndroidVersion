package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.move.Move

class BattleUtils {
    companion object {
        fun getAttackOrder(pokemon: Pokemon, other: Pokemon, move: Move): List<Pokemon> {
            if (trainerStarts(pokemon,other,move)){
                return listOf(pokemon,other)
            }
            else {
                return listOf(other,pokemon)
            }
        }

        fun trainerStarts(pokemon: Pokemon, other: Pokemon, move: Move): Boolean {
            if (move.priorityLevel > other.IA(pokemon).move.priorityLevel){
                return true
            }
            else if (move.priorityLevel < other.IA(pokemon).move.priorityLevel){
                return false
            }
            else{//TODO check paralysis
                return pokemon.speed.toFloat() * pokemon.speedMultiplicator >= other.speed.toFloat() * other.speedMultiplicator
            }
        }
    }
}