package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.move.Move

class BattleUtils {
    companion object {
        fun abilitiesCheck(pokemon: Pokemon, opponent: Pokemon) : String{
            val sb = StringBuilder()
            if (pokemon.hasAbility(Ability.INTIMIDATE)){
                sb.append("Intimidate: ${opponent.data.name}'s attack fell!\n")
                if (opponent.hasAbility(Ability.CLEAR_BODY))
                    sb.append("Clear Body: ${opponent.data.name}'s stats cannot be lowered!\n")
                else
                    opponent.battleData!!.attackMultiplicator *= 0.75f
            }
            if (pokemon.hasAbility(Ability.SUPER_LUCK))
                pokemon.battleData!!.criticalRate *= 1.5f
            return sb.toString()
        }

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