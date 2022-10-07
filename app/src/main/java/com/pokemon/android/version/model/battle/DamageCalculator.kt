package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveCategory
import kotlin.math.roundToInt
import kotlin.random.Random

class DamageCalculator {
    companion object {
        fun getCriticalMultiplicator(attacker: Pokemon, move: Move): Float {
            val random = Random.nextInt(100)
            if (move.highCritRate) {
                if (random < 13 * attacker.battleData!!.criticalRate)
                    return 1.5f
            } else if (random < 6 * attacker.battleData!!.criticalRate)
                return 1.5f
            return 1f
        }

        fun computeDamageWithoutAbility(attacker: Pokemon, move: Move, opponent: Pokemon): Int {
            var multiplicator = 1f
            var stab = if (attacker.data.type1 == move.type || attacker.data.type2 == move.type) 1.5f else 1f
            if (attacker.isMegaEvolved() && (attacker.data.megaEvolutionData!!.type1 == move.type || attacker.data.megaEvolutionData!!.type2 == move.type))
                stab = 1.5f
            if (move.category == MoveCategory.PHYSICAL && !attacker.data.abilities.contains(Ability.GUTS)) {
                if (attacker.status == Status.BURN)
                    multiplicator *= 0.5f
            }
            return try {
                var type: Float = move.type.isEffectiveAgainst(opponent.data.type2) * move.type.isEffectiveAgainst(opponent.data.type1)
                if (opponent.isMegaEvolved()){
                    type = move.type.isEffectiveAgainst(opponent.data.megaEvolutionData!!.type2) * move.type.isEffectiveAgainst(opponent.data.megaEvolutionData!!.type1)
                }
                val random: Float = Random.nextInt(85, 100).toFloat() / 100f
                val offensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * attacker.battleData!!.spAtkMultiplicator).roundToInt()
                val defensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL) (opponent.defense.toFloat() * opponent.battleData!!.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * opponent.battleData!!.spDefMultiplicator).roundToInt()
                ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * move.power * offensiveStat) / (defensiveStat * 50)) + 2) * type * stab * multiplicator * random).roundToInt()
            } catch (e : Exception){
                0
            }
        }

        fun computeDamage(attacker: Pokemon, move: Move, opponent: Pokemon, criticalMultiplicator: Float): Int {
            if (move.type == Type.GROUND && opponent.data.abilities.contains(Ability.LEVITATE))
                return 0
            if (move.type == Type.WATER && opponent.data.abilities.contains(Ability.WATER_ABSORB))
                return 0
            if (move.type == Type.ELECTRIC && (opponent.data.abilities.contains(Ability.LIGHTNING_ROD) || opponent.data.abilities.contains(Ability.VOLT_ABSORB)))
                return 0
            var multiplicator = 1f
            var stab = if (attacker.data.type1 == move.type || attacker.data.type2 == move.type) 1.5f else 1f
            if (attacker.isMegaEvolved() && (attacker.data.megaEvolutionData!!.type1 == move.type || attacker.data.megaEvolutionData!!.type2 == move.type))
                stab = 1.5f
            if (stab == 1.5f && attacker.data.abilities.contains(Ability.ADAPTABILITY))
                stab = 2f
            if (move.category == MoveCategory.PHYSICAL && !attacker.data.abilities.contains(Ability.GUTS)) {
                if (attacker.status == Status.BURN)
                    multiplicator *= 0.5f
            }
            if ((move.type == Type.FIRE || move.type == Type.ICE) && opponent.data.abilities.contains(Ability.THICK_FAT))
                multiplicator *= 0.5f
            if (move.power <= 60 && attacker.data.abilities.contains(Ability.TECHNICIAN))
                multiplicator *= 2f
            return try {
                var type: Float = move.type.isEffectiveAgainst(opponent.data.type2) * move.type.isEffectiveAgainst(opponent.data.type1)
                if (opponent.isMegaEvolved()){
                    type = move.type.isEffectiveAgainst(opponent.data.megaEvolutionData!!.type2) * move.type.isEffectiveAgainst(opponent.data.megaEvolutionData!!.type1)
                }
                val random: Float = Random.nextInt(85, 100).toFloat() / 100f
                val offensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL) (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt() else (attacker.spAtk.toFloat() * attacker.battleData!!.spAtkMultiplicator).roundToInt()
                val defensiveStat: Int =
                    if (move.category == MoveCategory.PHYSICAL) (opponent.defense.toFloat() * opponent.battleData!!.defenseMultiplicator).roundToInt() else (opponent.spDef.toFloat() * opponent.battleData!!.spDefMultiplicator).roundToInt()
                ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * move.power * offensiveStat) / (defensiveStat * 50)) + 2) * type * criticalMultiplicator * stab * multiplicator * random).roundToInt()
            } catch (e : Exception){
                0
            }
        }

        fun computeConfusionDamage(attacker: Pokemon): Int {
            var multiplicator = 1f
            if (attacker.status == Status.BURN)
                multiplicator *= 0.5f
            val random: Float = Random.nextInt(85, 100).toFloat() / 100f
            val offensiveStat: Int =
                (attacker.attack.toFloat() * attacker.battleData!!.attackMultiplicator).roundToInt()
            val defensiveStat: Int =
                (attacker.defense.toFloat() * attacker.battleData!!.defenseMultiplicator).roundToInt()
            return ((((((attacker.level.toFloat() * 0.4f).roundToInt() + 2) * 40 * offensiveStat) / (defensiveStat * 50)) + 2) * multiplicator * random).roundToInt()
        }
    }
}