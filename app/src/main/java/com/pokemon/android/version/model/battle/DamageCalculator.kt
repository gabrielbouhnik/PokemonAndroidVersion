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
        fun getCritMultiplicator(attacker : Pokemon, move : Move) : Float{
            val random = Random.nextInt(100)
            if (move is CritMove){
                if (random < 13)
                    return 1.5f
            }
            else if (random < 6 * attacker.battleData!!.critRate)
                return 1.5f
            return 1f
        }

        fun computeDamageIA(attacker : Pokemon, move : Move, opponent : Pokemon) : Int{
            var multiplicator = 1f
            val stab = if (attacker.data.type1 == move.type || attacker.data.type2 == move.type) 1.5f else 1f
            if (move.category == MoveCategory.PHYSICAL) {
                if (attacker.status == Status.BURN)
                    multiplicator *= 0.5f
            }
            val type : Float = move.type.isEffectiveAgainst(opponent.data.type2) * move.type.isEffectiveAgainst(opponent.data.type1)
            val random : Float = Random.nextInt(85,100).toFloat()/100f
            val offensiveStat : Int = if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * attacker.battleData!!.spAtkMultiplicator).roundToInt()
            val defensiveStat : Int = if (move.category == MoveCategory.PHYSICAL) (opponent.defense.toFloat() * opponent.battleData!!.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * opponent.battleData!!.spDefMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * move.power * offensiveStat)/(defensiveStat * 50)) + 2) * type * stab * multiplicator* random).roundToInt()
        }

        fun computeDamage(attacker : Pokemon, move : Move, opponent : Pokemon) : Int{
            var multiplicator = 1f
            val stab = if (attacker.data.type1 == move.type || attacker.data.type2 == move.type) 1.5f else 1f
            if (move.category == MoveCategory.PHYSICAL) {
                if (attacker.status == Status.BURN)
                    multiplicator *= 0.5f
            }
            val type : Float = move.type.isEffectiveAgainst(opponent.data.type2) * move.type.isEffectiveAgainst(opponent.data.type1)
            val crit = getCritMultiplicator(attacker, move)
            val random : Float = Random.nextInt(85,100).toFloat()/100f
            val offensiveStat : Int = if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * attacker.battleData!!.spAtkMultiplicator).roundToInt()
            val defensiveStat : Int = if (move.category == MoveCategory.PHYSICAL) (opponent.defense.toFloat() * opponent.battleData!!.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * opponent.battleData!!.spDefMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * move.power * offensiveStat)/(defensiveStat * 50)) + 2) * type * crit * stab * multiplicator* random).roundToInt()
        }

        fun computeConfusionDamage(attacker : Pokemon) : Int{
            var multiplicator = 1f
            if (attacker.status == Status.BURN)
                multiplicator *= 0.5f
            val random : Float = Random.nextInt(85,100).toFloat()/100f
            val offensiveStat : Int = (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt()
            val defensiveStat : Int = (attacker.defense.toFloat() * attacker.battleData!!.defenseMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * 40 * offensiveStat)/(defensiveStat * 50)) + 2) * multiplicator* random).roundToInt()
        }
    }
}