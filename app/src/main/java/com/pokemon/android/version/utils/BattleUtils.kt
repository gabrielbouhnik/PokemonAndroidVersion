package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveCharacteristic
import kotlin.random.Random

class BattleUtils {
    companion object {
        fun contactMovesCheck(attacker: Pokemon, move: Move, opponent: Pokemon) : String{
            val random: Int = Random.nextInt(100)
            if (move.characteristics.contains(MoveCharacteristic.CONTACT) && random < 30){
                if (attacker.hasAbility(Ability.POISON_TOUCH)
                    && Status.isAffectedByStatus(0,Status.POISON, opponent)) {
                        opponent.status = Status.POISON
                    if (opponent.hasAbility(Ability.QUICK_FEET))
                        opponent.battleData!!.speedMultiplicator *= 1.5f
                    return "Poison Touch: ${opponent.data.name} is poisoned!\n"
                }
                if (opponent.hasAbility(Ability.ROUGH_SKIN)){
                    attacker.currentHP = if (attacker.currentHP < attacker.hp/8) 0 else attacker.currentHP - attacker.hp/8
                    return "Rough Skin: ${attacker.data.name} was hurt!\n"
                }
                if (opponent.hasAbility(Ability.POISON_POINT)
                    && Status.isAffectedByStatus(0,Status.POISON, attacker)) {
                    attacker.status = Status.POISON
                    if (attacker.hasAbility(Ability.QUICK_FEET))
                        attacker.battleData!!.speedMultiplicator *= 1.5f
                    return "Poison Point: ${attacker.data.name} is poisoned!\n"
                }
                if (opponent.hasAbility(Ability.STATIC)
                    && Status.isAffectedByStatus(0,Status.PARALYSIS, attacker)) {
                    attacker.status = Status.PARALYSIS
                    if (attacker.hasAbility(Ability.QUICK_FEET))
                        attacker.battleData!!.speedMultiplicator *= 1.5f
                    return "Static: ${attacker.data.name} is paralyzed!\n"
                }
                if (opponent.hasAbility(Ability.FLAME_BODY)
                    && Status.isAffectedByStatus(0,Status.BURN, attacker)) {
                    attacker.status = Status.BURN
                    if (attacker.hasAbility(Ability.QUICK_FEET))
                        attacker.battleData!!.speedMultiplicator *= 1.5f
                    return "Flame Body: ${attacker.data.name} is burned!\n"
                }
                if (opponent.hasAbility(Ability.EFFECT_SPORE)){
                    if (random < 10 && Status.isAffectedByStatus(34,Status.POISON,attacker))
                        attacker.status = Status.POISON
                    else if (random < 20 && Status.isAffectedByStatus(35,Status.ASLEEP,attacker))
                        attacker.status = Status.ASLEEP
                }
            }
            return ""
        }

        fun abilitiesCheck(pokemon: Pokemon, opponent: Pokemon) : String{
            val sb = StringBuilder()
            if (pokemon.hasAbility(Ability.QUICK_FEET) && pokemon.status != Status.OK)
                pokemon.battleData!!.speedMultiplicator *= 1.5f
            if (pokemon.hasAbility(Ability.GUTS) && pokemon.status != Status.OK)
                pokemon.battleData!!.attackMultiplicator *= 1.5f
            if (pokemon.hasAbility(Ability.PRESSURE))
                sb.append("Pressure: ${pokemon.data.name} is exerting its pressure!\n")
            if (pokemon.hasAbility(Ability.INTIMIDATE) && !opponent.hasAbility(Ability.OWN_TEMPO)){
                sb.append("Intimidate: ${opponent.data.name}'s attack fell!\n")
                when {
                    opponent.hasAbility(Ability.CLEAR_BODY) -> sb.append("Clear Body: ${opponent.data.name}'s stats cannot be lowered!\n")
                    opponent.hasAbility(Ability.HYPER_CUTTER) -> sb.append("Hyper Cutter: ${opponent.data.name}'s attack cannot be lowered!\n")
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

        fun getEffectiveness(move: Move, opponent: Pokemon) : String{
            var effectiveness = move.type.isEffectiveAgainst(opponent.data.type1) *
                    move.type.isEffectiveAgainst(opponent.data.type2)
            if (opponent.isMegaEvolved())
                effectiveness = move.type.isEffectiveAgainst(opponent.data.megaEvolutionData!!.type1) *
                        move.type.isEffectiveAgainst(opponent.data.megaEvolutionData!!.type2)
            if (move.type == Type.ELECTRIC && opponent.hasAbility(Ability.VOLT_ABSORB))
                return "Volt Absorb: ${opponent.data.name}'s HP was restored\n"
            if (move.type == Type.WATER){
                if (opponent.hasAbility(Ability.WATER_ABSORB))
                    return "Water Absorb: ${opponent.data.name}'s HP was restored\n"
                if (opponent.hasAbility(Ability.DRY_SKIN))
                    return "Water Absorb: ${opponent.data.name}'s HP was restored\n"
            }
            return when {
                effectiveness == 0f -> {
                    "It does not affect ${opponent.data.name}\n"
                }
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