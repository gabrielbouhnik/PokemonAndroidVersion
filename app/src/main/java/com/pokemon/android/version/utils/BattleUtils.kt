package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.battle.DamageCalculator
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveBasedOnLevel
import com.pokemon.android.version.model.move.MoveCategory
import com.pokemon.android.version.model.move.MoveCharacteristic
import kotlin.random.Random

class BattleUtils {
    companion object {
        fun contactMovesCheck(attacker: Pokemon, move: Move, opponent: Pokemon): String {
            val random: Int = Random.nextInt(100)
            if (move.characteristics.contains(MoveCharacteristic.CONTACT) && random < 30) {
                if (attacker.hasAbility(Ability.POISON_TOUCH)
                    && Status.isAffectedByStatus(0, Status.POISON, opponent)
                ) {
                    opponent.status = Status.POISON
                    if (opponent.hasAbility(Ability.QUICK_FEET))
                        opponent.battleData!!.speedMultiplicator *= 1.5f
                    if (opponent.hasAbility(Ability.GUTS))
                        opponent.battleData!!.attackMultiplicator *= 1.5f
                    return "${attacker.data.name}'s Poison Touch: ${opponent.data.name} is poisoned!\n"
                }
                if (opponent.hasAbility(Ability.ROUGH_SKIN)) {
                    attacker.takeDamage(attacker.hp / 8)
                    return "${opponent.data.name}'s Rough Skin: ${attacker.data.name} was hurt!\n"
                }
                if (opponent.hasAbility(Ability.POISON_POINT)
                    && Status.isAffectedByStatus(0, Status.POISON, attacker)
                ) {
                    attacker.status = Status.POISON
                    if (attacker.hasAbility(Ability.QUICK_FEET))
                        attacker.battleData!!.speedMultiplicator *= 1.5f
                    if (attacker.hasAbility(Ability.GUTS))
                        attacker.battleData!!.attackMultiplicator *= 1.5f
                    return "${opponent.data.name}'s Poison Point: ${attacker.data.name} is poisoned!\n"
                }
                if (opponent.hasAbility(Ability.STATIC)
                    && Status.isAffectedByStatus(0, Status.PARALYSIS, attacker)
                ) {
                    attacker.status = Status.PARALYSIS
                    if (attacker.hasAbility(Ability.QUICK_FEET))
                        attacker.battleData!!.speedMultiplicator *= 1.5f
                    if (attacker.hasAbility(Ability.GUTS))
                        attacker.battleData!!.attackMultiplicator *= 1.5f
                    return "${opponent.data.name}'s Static: ${attacker.data.name} is paralyzed!\n"
                }
                if (opponent.hasAbility(Ability.FLAME_BODY)
                    && Status.isAffectedByStatus(0, Status.BURN, attacker)
                ) {
                    attacker.status = Status.BURN
                    if (attacker.hasAbility(Ability.QUICK_FEET))
                        attacker.battleData!!.speedMultiplicator *= 1.5f
                    if (attacker.hasAbility(Ability.GUTS))
                        attacker.battleData!!.attackMultiplicator *= 1.5f
                    return "${opponent.data.name}'s Flame Body: ${attacker.data.name} is burned!\n"
                }
                if (opponent.hasAbility(Ability.EFFECT_SPORE)) {
                    if (random < 10 && Status.isAffectedByStatus(34, Status.POISON, attacker)) {
                        attacker.status = Status.POISON
                        if (attacker.hasAbility(Ability.QUICK_FEET))
                            attacker.battleData!!.speedMultiplicator *= 1.5f
                        if (attacker.hasAbility(Ability.GUTS))
                            attacker.battleData!!.attackMultiplicator *= 1.5f
                    } else if (random < 20 && Status.isAffectedByStatus(35, Status.ASLEEP, attacker))
                        attacker.status = Status.ASLEEP
                }
            }
            return ""
        }

        fun abilitiesCheck(pokemon: Pokemon, opponent: Pokemon): String {
            val sb = StringBuilder()
            if (pokemon.hasAbility(Ability.QUICK_FEET) && pokemon.status != Status.OK)
                pokemon.battleData!!.speedMultiplicator *= 1.5f
            if (pokemon.hasAbility(Ability.GUTS) && pokemon.status != Status.OK)
                pokemon.battleData!!.attackMultiplicator *= 1.5f
            if (pokemon.hasAbility(Ability.PRESSURE))
                sb.append("${pokemon.data.name}'s Pressure: ${pokemon.data.name} is exerting its pressure!\n")
            if (pokemon.hasAbility(Ability.INTIMIDATE) && !opponent.hasAbility(Ability.OWN_TEMPO)) {
                sb.append("${pokemon.data.name}'s Intimidate: ${opponent.data.name}'s attack fell!\n")
                when {
                    opponent.hasAbility(Ability.CLEAR_BODY) -> sb.append("${opponent.data.name}'s Clear Body: ${opponent.data.name}'s stats cannot be lowered!\n")
                    opponent.hasAbility(Ability.HYPER_CUTTER) -> sb.append("${opponent.data.name}'s Hyper Cutter: ${opponent.data.name}'s attack cannot be lowered!\n")
                    else -> opponent.battleData!!.attackMultiplicator *= 0.75f
                }
            }
            if (pokemon.hasAbility(Ability.SUPER_LUCK))
                pokemon.battleData!!.criticalRate *= 1.5f
            if (pokemon.hasAbility(Ability.ARENA_TRAP))
                opponent.battleData!!.battleStatus.add(Status.TRAPPED_WITHOUT_DAMAGE)
            if (opponent.hasAbility(Ability.ARENA_TRAP))
                pokemon.battleData!!.battleStatus.add(Status.TRAPPED_WITHOUT_DAMAGE)
            return sb.toString()
        }

        fun getEffectiveness(move: Move, opponent: Pokemon): String {
            var effectiveness = DamageCalculator.getEffectiveness(move, opponent)
            if (move is MoveBasedOnLevel)
                effectiveness = 1f
            if (move.type == Type.ELECTRIC && opponent.hasAbility(Ability.VOLT_ABSORB))
                return "${opponent.data.name}'s Volt Absorb: ${opponent.data.name}'s HP was restored\n"
            if (move.type == Type.WATER) {
                if (opponent.hasAbility(Ability.WATER_ABSORB))
                    return "${opponent.data.name}'s Water Absorb: ${opponent.data.name}'s HP was restored\n"
                if (opponent.hasAbility(Ability.DRY_SKIN))
                    return "${opponent.data.name}'s Dry Skin: ${opponent.data.name}'s HP was restored\n"
            }
            return when {
                effectiveness == 0f -> "It does not affect ${opponent.data.name}\n"
                effectiveness >= 2 -> "It's super effective!\n"
                effectiveness < 1f -> "It's not very effective effective!\n"
                else -> ""
            }
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
                    return isFaster(pokemon,other)
                }
            }
        }

        fun isFaster(pokemon: Pokemon, other: Pokemon): Boolean {
            val pokemonSpeed = pokemon.speed.toFloat() * pokemon.battleData!!.speedMultiplicator
            val otherSpeed = other.speed.toFloat() * other.battleData!!.speedMultiplicator
            val paralysisMultiplicator: Float = if (pokemon.status == Status.PARALYSIS) 0.5f else 1f
            val opponentParalysisMultiplicator: Float = if (other.status == Status.PARALYSIS) 0.5f else 1f
            return pokemonSpeed * paralysisMultiplicator >= otherSpeed * opponentParalysisMultiplicator
        }
    }
}