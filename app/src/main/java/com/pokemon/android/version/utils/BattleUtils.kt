package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.battle.DamageCalculator
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.MoveBasedOnLevel
import com.pokemon.android.version.model.move.MoveCategory
import com.pokemon.android.version.model.move.MoveCharacteristic
import kotlin.random.Random

class BattleUtils {
    companion object {
        fun contactMovesCheck(attacker: Pokemon, move: Move, opponent: Pokemon): String {
            val random: Int = Random.nextInt(100)
            var details = ""
            var statusByContact = false
            if (move.characteristics.contains(MoveCharacteristic.CONTACT) && random < 30) {
                if (attacker.hasAbility(Ability.POISON_TOUCH)
                    && attacker.status != Status.OK
                    && Status.isAffectedByStatus(0, Status.POISON, opponent)
                ) {
                    opponent.status = Status.POISON
                    statusByContact = true
                    checkForStatsRaiseAbility(attacker)
                    details += "${attacker.data.name}'s Poison Touch: ${opponent.data.name} is poisoned!\n"
                }
                if (opponent.hasAbility(Ability.ROUGH_SKIN)) {
                    attacker.takeDamage(attacker.hp / 8)
                    details += "${opponent.data.name}'s Rough Skin: ${attacker.data.name} was hurt!\n"
                }
                if (opponent.hasItem(HoldItem.ROCKY_HELMET)) {
                    attacker.takeDamage(attacker.hp / 8)
                    details += "${opponent.data.name}'s Rocky Helmet: ${attacker.data.name} was hurt!\n"
                }
                if (opponent.hasAbility(Ability.POISON_POINT)
                    && attacker.status != Status.OK
                    && Status.isAffectedByStatus(0, Status.POISON, attacker)
                ) {
                    attacker.status = Status.POISON
                    statusByContact = true
                    checkForStatsRaiseAbility(attacker)
                    details += "${opponent.data.name}'s Poison Point: ${attacker.data.name} is poisoned!\n"
                }
                if (opponent.hasAbility(Ability.STATIC)
                    && attacker.status != Status.OK
                    && Status.isAffectedByStatus(0, Status.PARALYSIS, attacker)
                ) {
                    attacker.status = Status.PARALYSIS
                    statusByContact = true
                    checkForStatsRaiseAbility(attacker)
                    details +=  "${opponent.data.name}'s Static: ${attacker.data.name} is paralyzed!\n"
                }
                if (opponent.hasAbility(Ability.FLAME_BODY)
                    && attacker.status != Status.OK
                    && Status.isAffectedByStatus(0, Status.BURN, attacker)
                ) {
                    attacker.status = Status.BURN
                    statusByContact = true
                    checkForStatsRaiseAbility(attacker)
                    details +=  "${opponent.data.name}'s Flame Body: ${attacker.data.name} is burned!\n"
                }
                if (opponent.hasAbility(Ability.EFFECT_SPORE)
                    && attacker.status != Status.OK) {
                    if (random < 10 && Status.isAffectedByStatus(34, Status.POISON, attacker)) {
                        attacker.status = Status.POISON
                        statusByContact = true
                        checkForStatsRaiseAbility(attacker)
                        details +=  "${opponent.data.name}'s Effect Spore: ${attacker.data.name} is poisoned!\n"
                    } else if (random < 20 && Status.isAffectedByStatus(35, Status.ASLEEP, attacker)) {
                        attacker.status = Status.ASLEEP
                        statusByContact = true
                        details +=  "${opponent.data.name}'s Effect Spore: ${attacker.data.name} fell asleep!\n"
                    }
                    else if (random >= 20 && Status.isAffectedByStatus(-1, Status.PARALYSIS, attacker)){
                        attacker.status = Status.PARALYSIS
                        statusByContact = true
                        checkForStatsRaiseAbility(attacker)
                        details +=  "${opponent.data.name}'s Effect Spore: ${attacker.data.name} is paralysed\n"
                    }
                }
            }
            if (statusByContact && opponent.hasItem(HoldItem.LUM_BERRY)){
                details += "${opponent.data.name}'s Lum Berry cured its status\n"
                opponent.status = Status.OK
                opponent.heldItem = null
            }
            return details
        }

        fun abilitiesCheck(pokemon: Pokemon, opponent: Pokemon): String {
            val sb = StringBuilder()
            if (pokemon.status != Status.OK)
                checkForStatsRaiseAbility(pokemon)
            if (pokemon.hasAbility(Ability.PRESSURE))
                sb.append("${pokemon.data.name}'s Pressure: ${pokemon.data.name} is exerting its pressure!\n")
            if (pokemon.hasAbility(Ability.INTIMIDATE) && !opponent.hasAbility(Ability.OWN_TEMPO)) {
                sb.append("${pokemon.data.name}'s Intimidate: ${opponent.data.name}'s attack fell!\n")
                when {
                    opponent.hasAbility(Ability.CLEAR_BODY) -> sb.append("${opponent.data.name}'s Clear Body: ${opponent.data.name}'s stats cannot be lowered!\n")
                    opponent.hasAbility(Ability.HYPER_CUTTER) -> sb.append("${opponent.data.name}'s Hyper Cutter: ${opponent.data.name}'s attack cannot be lowered!\n")
                    opponent.hasAbility(Ability.DEFIANT) -> {
                        opponent.battleData!!.attackMultiplicator *= 1.5f
                        sb.append("${opponent.data.name}'s Defiant: ${opponent.data.name}'s attack rose!\n")
                    }
                        else -> {
                        if (opponent.battleData != null)
                            opponent.battleData!!.attackMultiplicator *= 0.75f
                    }
                }
            }
            if (pokemon.hasItem(HoldItem.AIR_BALLOON))
                sb.append("${pokemon.data.name} floats in the air with its Air Balloon\n")
            if (opponent.heldItem != null && pokemon.hasAbility(Ability.FRISK)){
                sb.append("${pokemon.data.name}'s Frisk: ${pokemon.data.name} frisked ${opponent.data.name} and found its ${opponent.heldItem!!.heldItemToString()}!\n")
            }
            if (pokemon.heldItem != null && opponent.hasAbility(Ability.FRISK)){
                sb.append("${opponent.data.name}'s Frisk: ${opponent.data.name} frisked ${pokemon.data.name} and found its ${pokemon.heldItem!!.heldItemToString()}!\n")
            }
            if (pokemon.hasItem(HoldItem.ASSAULT_VEST) && !MoveUtils.getMoveList(pokemon).map{it.move.category}.contains(MoveCategory.OTHER))
                pokemon.battleData!!.spDefMultiplicator *= 1.5f
            if (pokemon.hasItem(HoldItem.EVIOLITE) && pokemon.data.evolutions.isNotEmpty()) {
                pokemon.battleData!!.spDefMultiplicator *= 1.5f
                pokemon.battleData!!.defenseMultiplicator *= 1.5f
            }
            if (pokemon.hasAbility(Ability.SUPER_LUCK))
                pokemon.battleData!!.criticalRate *= 1.5f
            if (pokemon.hasAbility(Ability.ARENA_TRAP) && !opponent.hasAbility(Ability.LEVITATE) && pokemon.data.type2 != Type.GHOST && pokemon.data.type2 != Type.FLYING && pokemon.data.type1 != Type.GHOST && pokemon.data.type1 != Type.FLYING)
                opponent.battleData!!.battleStatus.add(Status.TRAPPED_WITHOUT_DAMAGE)
            if (opponent.hasAbility(Ability.ARENA_TRAP) && !pokemon.hasAbility(Ability.LEVITATE) && opponent.data.type1 != Type.GHOST && opponent.data.type1 != Type.FLYING && opponent.data.type2 != Type.GHOST && opponent.data.type2 != Type.FLYING)
                pokemon.battleData!!.battleStatus.add(Status.TRAPPED_WITHOUT_DAMAGE)
            if (pokemon.hasAbility(Ability.SHADOW_TAG) && pokemon.data.type2 != Type.GHOST && pokemon.data.type1 != Type.GHOST)
                opponent.battleData!!.battleStatus.add(Status.TRAPPED_WITHOUT_DAMAGE)
            if (opponent.hasAbility(Ability.SHADOW_TAG) && opponent.data.type1 != Type.GHOST && opponent.data.type2 != Type.GHOST)
                pokemon.battleData!!.battleStatus.add(Status.TRAPPED_WITHOUT_DAMAGE)
            if (pokemon.hasAbility(Ability.MAGNET_PULL) && pokemon.data.type2 == Type.STEEL && pokemon.data.type1 == Type.STEEL)
                opponent.battleData!!.battleStatus.add(Status.TRAPPED_WITHOUT_DAMAGE)
            if (opponent.hasAbility(Ability.MAGNET_PULL) && opponent.data.type1 == Type.STEEL && opponent.data.type2 == Type.STEEL)
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
                move.priorityLevel > opponentMove.priorityLevel
                        || (pokemon.hasAbility(Ability.PRANKSTER) && move.category == MoveCategory.OTHER) -> {
                    true
                }
                move.priorityLevel < opponentMove.priorityLevel
                        || (other.hasAbility(Ability.PRANKSTER) && opponentMove.category == MoveCategory.OTHER) -> {
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

        fun checkForStatsRaiseAbility(pokemon: Pokemon){
            if (pokemon.hasAbility(Ability.QUICK_FEET))
                pokemon.battleData!!.speedMultiplicator *= 1.5f
            if (pokemon.hasAbility(Ability.GUTS))
                pokemon.battleData!!.attackMultiplicator *= 1.5f
            if (pokemon.hasAbility(Ability.COMPETITIVE))
                pokemon.battleData!!.spAtkMultiplicator *= 1.5f
            if (pokemon.hasAbility(Ability.MARVEL_SCALE))
                pokemon.battleData!!.defenseMultiplicator *= 1.5f
        }
    }
}