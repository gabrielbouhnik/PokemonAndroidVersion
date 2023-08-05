package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.battle.DamageCalculator
import com.pokemon.android.version.model.move.*
import com.pokemon.android.version.model.move.pokemon.PokemonMove

class IAUtils {
    companion object {
        fun canBeKOdByOpponent(attacker: Pokemon, opponent: Pokemon): Boolean {
            val offensiveMove =
                MoveUtils.getMoveList(opponent).filter { it.pp > 0 && it.move.power > 0 && it.move !is ChargedMove }
            for (move in offensiveMove) {
                var damage: Int = DamageCalculator.computeDamage(opponent, move.move, attacker, 1f)
                if (move.move is VariableHitMove) {
                    damage *= if (opponent.hasAbility(Ability.SKILL_LINK)) 5 else 3
                } else if (move.move is MultipleHitMove)
                    damage *= 2
                if (damage >= attacker.currentHP)
                    return true
            }
            return false
        }

        fun canTakeTwoHits(attacker: Pokemon, opponent: Pokemon): Boolean {
            val offensiveMove = MoveUtils.getMoveList(opponent).filter { it.pp > 0 && it.move.power > 0 }
            for (move in offensiveMove) {
                var damage: Int = DamageCalculator.computeDamage(opponent, move.move, attacker, 1f)
                if (move.move !is ChargedMove && move.move !is UltimateMove)
                    damage *= 2
                if (move.move is VariableHitMove) {
                    damage *= if (opponent.hasAbility(Ability.SKILL_LINK)) 5 else 3
                } else if (move.move is MultipleHitMove)
                    damage *= 2
                if (damage >= attacker.currentHP)
                    return true
            }
            return false
        }

        fun iaWildPokemon(attacker: Pokemon): PokemonMove {
            var usableMoves = MoveUtils.getMoveList(attacker).filter { it.pp > 0 }
            if (attacker.battleData!!.battleStatus.contains(Status.TAUNTED)){
                usableMoves = usableMoves.filter { it.move.category != MoveCategory.OTHER}
            }
            if (attacker.battleData!!.rampageMove != null)
                return attacker.battleData!!.rampageMove!!
            if (attacker.battleData!!.chargedMove != null) {
                val move = attacker.battleData!!.chargedMove!!
                attacker.battleData!!.chargedMove = null
                return move
            }
            return usableMoves.random()
        }

        fun ia(attacker: Pokemon, opponent: Pokemon): PokemonMove {
            var usableMoves = MoveUtils.getMoveList(attacker).filter { it.pp > 0 }
            if (attacker.battleData!!.battleStatus.contains(Status.TAUNTED)){
                usableMoves = usableMoves.filter { it.move.category != MoveCategory.OTHER}
            }
            if (opponent.currentHP == 0)
                return usableMoves[0]
            if (attacker.battleData!!.chargedMove != null) {
                val move = attacker.battleData!!.chargedMove!!
                attacker.battleData!!.chargedMove = null
                return move
            }
            if (attacker.battleData!!.rampageMove != null)
                return attacker.battleData!!.rampageMove!!
            var maxDamage = 0
            var maxDamageIdx = 0
            for ((Idx, move) in usableMoves.withIndex()) {
                if (move.move.id == 213 && opponent.status != Status.ASLEEP)
                    continue
                var damage: Int = DamageCalculator.computeDamage(attacker, move.move, opponent, 1f)
                if (damage == 0 && move.move.category != MoveCategory.OTHER)
                    continue
                if (move.move is MultipleHitMove || move.move is VariableHitMove)
                    damage *= 2
                if (canBeKOdByOpponent(attacker, opponent) && move.move is ChargedMove)
                    continue
                if (damage >= opponent.currentHP) {
                    if (move.move !is ChargedMove || attacker.hp / attacker.currentHP > 2)
                        return move
                }
                if (damage > 0 && attacker.hp / attacker.currentHP < 4 && move.move.priorityLevel > 0 && attacker.speed * attacker.battleData!!.speedMultiplicator < opponent.speed * opponent.battleData!!.speedMultiplicator)
                    return move
                move.move.status.forEach {
                    if (Status.isAffectedByStatus(
                            move.move.id,
                            it.status,
                            opponent
                        ) && it.probability == null && !opponent.hasAbility(
                            Ability.MAGIC_BOUNCE
                        )
                    )
                        return move
                }
                if (move.move is HealMove && attacker.hp / attacker.currentHP > 3)
                    return move
                if (move.move is StatChangeMove) {
                    val statChangeMove: StatChangeMove = move.move as StatChangeMove
                    if (move.move.power == 0 && !opponent.hasAbility(Ability.MAGIC_BOUNCE) &&
                        statChangeMove.statsAffected.contains(Stats.SPEED) && BattleUtils.isFaster(opponent, attacker)
                    )
                        return move
                    if (statChangeMove.statsAffected.contains(Stats.ACCURACY) && opponent.battleData!!.accuracyMultiplicator == 1f && !opponent.hasAbility(
                            Ability.MAGIC_BOUNCE
                        ) && !opponent.hasAbility(Ability.KEEN_EYE)
                    )
                        return move
                }
                if (move.move is RemoveStatChangesMove && (opponent.battleData!!.attackMultiplicator > 1f
                            || opponent.battleData!!.spAtkMultiplicator > 1f
                            || opponent.battleData!!.speedMultiplicator > 1f)
                    && opponent.data.type1 != Type.STEEL && opponent.data.type2 != Type.STEEL
                )
                    return move
                if (damage > maxDamage) {
                    maxDamageIdx = Idx
                    maxDamage = damage
                }
            }
            return usableMoves[maxDamageIdx]
        }

        fun shouldSwitch(attacker: Pokemon, opponent: Pokemon, trainerTeam: List<Pokemon>): Pokemon? {
            val team = trainerTeam.filter { it.currentHP > 0 && it != attacker }
            if (opponent.battleData!!.battleStatus.contains(Status.TIRED) || (canBeKOdByOpponent(
                    attacker,
                    opponent
                ) && opponent.currentHP >= opponent.hp * 0.6f)
            ) {
                for (pokemon in team) {
                    if ((pokemon.speed > opponent.speed * opponent.battleData!!.speedMultiplicator
                                || canTakeTwoHits(pokemon, opponent))
                        && canBeKOdByOpponent(
                            opponent, pokemon
                        )
                        && !canBeKOdByOpponent(pokemon, opponent)
                    )
                        return pokemon
                }
            }
            return null
        }

        fun getBestPokemonToSentAfterKo(opponent: Pokemon, trainerTeam: List<Pokemon>): Pokemon? {
            val team = trainerTeam.filter { it.currentHP > 0 }
            if (team.isEmpty())
                return null
            val scores: List<Int> = team.map {
                var score = 0
                if (it.speed > opponent.speed * opponent.battleData!!.speedMultiplicator) {
                    score += 1
                    if (canBeKOdByOpponent(opponent, it))
                        score += 3
                    if (canTakeTwoHits(it, opponent))
                        score += 1
                } else if (!canBeKOdByOpponent(it, opponent) && canBeKOdByOpponent(opponent, it))
                    score += 2
                if (canTakeTwoHits(it, opponent))
                    score += 1
                score
            }
            return team[scores.indexOf(scores.maxOrNull())]
        }
    }
}