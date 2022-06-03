package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.move.CritMove
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveCategory
import kotlin.math.roundToInt
import kotlin.random.Random

class DamageCalculator {
    companion object {
        fun getCritMultiplicator(move : Move) : Float{
            var random = Random.nextInt(100)
            if (move is CritMove){
                if (random < 13)
                    return 1.5f
            }
            else if (random < 6)
                return 1.5f
            return 1f
        }

        fun computeDamage(attacker : Pokemon, move : Move, opponent : Pokemon) : Int{
            var multiplicator = 1f
            var stab = if (attacker.data.type1 == move.type || attacker.data.type2 == move.type) 1.5f else 1f
            if (move.category == MoveCategory.PHYSICAL) {
                if (attacker.status == Status.BURN)
                    multiplicator *= 0.5f
            }
            var type : Float = move.type.isEffectiveAgainst(opponent.data.type2) * move.type.isEffectiveAgainst(opponent.data.type1)
            var crit = getCritMultiplicator(move)
            var random : Float = Random.nextInt(85,100).toFloat()/100f
            var offensiveStat = if (move.category == MoveCategory.PHYSICAL) attacker.attack.toFloat() * attacker.attackMultiplicator else attacker.spAtk.toFloat() * attacker.spAtkMultiplicator
            var defensiveStat = if (move.category == MoveCategory.PHYSICAL) attacker.defense.toFloat() * attacker.defenseMultiplicator else attacker.spDef.toFloat() * attacker.spDefMultiplicator
            return ((((((attacker.level.toFloat() * 0.4f) + 2) * move.power.toFloat() * offensiveStat)/defensiveStat * 50) + 2f) * type * crit * stab * multiplicator* random).roundToInt().div(1000)
        }
    }
}