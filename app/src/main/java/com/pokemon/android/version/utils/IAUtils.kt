package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.battle.BattleField
import com.pokemon.android.version.model.battle.BattleSide
import com.pokemon.android.version.model.battle.BattleSideEffect
import com.pokemon.android.version.model.battle.DamageCalculator
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.*
import com.pokemon.android.version.model.move.Target
import com.pokemon.android.version.model.move.pokemon.PokemonMove

class IAUtils {
    companion object {

        private fun canTakeTwoHits(damageReceiver: Pokemon, opponent: Pokemon, battleField: BattleField, damageReceiverBattleSide: BattleSide): Boolean {
            val offensiveMove = MoveUtils.getMoveList(opponent).filter { it.pp > 0 && it.move.power > 0 }
            for (move in offensiveMove) {
                var damage: Int = DamageCalculator.computeDamage(opponent, move.move, damageReceiver, 1f, battleField, damageReceiverBattleSide)
                if (move.move !is ChargedMove && move.move !is UltimateMove)
                    damage *= 2
                if (move.move is VariableHitMove) {
                    damage *= if (opponent.hasAbility(Ability.SKILL_LINK) || opponent.hasItem(HoldItem.LOADED_DICE)) 5 else 3
                } else if (move.move is MultipleHitMove)
                    damage *= 2
                if (damage * 2 >= damageReceiver.currentHP)
                    return false
            }
            return true
        }

        fun iaWildPokemon(attacker: Pokemon): PokemonMove {
            val usableMoves = MoveUtils.getUsableMoves(attacker)
            if (attacker.battleData!!.rampageMove != null)
                return attacker.battleData!!.rampageMove!!
            if (attacker.battleData!!.chargedMove != null) {
                val move = attacker.battleData!!.chargedMove!!
                attacker.battleData!!.chargedMove = null
                return move
            }
            return usableMoves.random()
        }

        private fun shouldUpdateStats(statsChangeMove: StatChangeMove, pokemon: Pokemon): Boolean {
            return (statsChangeMove.increaseStat(Stats.ATTACK) && pokemon.battleData!!.statsMultiplier.attackMultiplicator <= 1f)
                    || (statsChangeMove.increaseStat(Stats.SPATK) && pokemon.battleData!!.statsMultiplier.spAtkMultiplicator <= 1f)
        }

        fun ia(attacker: Pokemon, opponent: Pokemon, battleField: BattleField, attackerBattleSide: BattleSide, opponentBattleSide: BattleSide): PokemonMove {
            val usableMoves = MoveUtils.getUsableMoves(attacker)
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
                var damage: Int = DamageCalculator.computeDamage(attacker, move.move, opponent, 1f, battleField, opponentBattleSide)
                if (damage == 0 && move.move.category != MoveCategory.OTHER)
                    continue
                if (move.move is MultipleHitMove)
                    damage *= 2
                if(move.move is VariableHitMove)
                    damage *= if (opponent.hasAbility(Ability.SKILL_LINK) || opponent.hasItem(HoldItem.LOADED_DICE)) 5 else 3
                if (attacker.canBeKOdBy(opponent, battleField, attackerBattleSide) && move.move is ChargedMove)
                    continue
                if ((move.move.id == 246 || move.move.id == 282) && attacker.battleData!!.hadATurn) {
                    //Don't use FAKE OUT and FIRST IMPRESSION
                    continue
                }
                if (move.move is RecoilMove && attacker.currentHP == attacker.hp && (move.move as RecoilMove).recoil == Recoil.ALL)
                    continue
                if (damage >= opponent.currentHP) {
                    if (move.move !is ChargedMove || attacker.hasItem(HoldItem.POWER_HERB))
                        return move
                }
                if (damage > 0
                    && attacker.canBeKOdBy(opponent, battleField, opponentBattleSide)
                    && move.move.priorityLevel > 0
                    && move.move.power > 0
                    && BattleUtils.isFaster(opponent, attacker, battleField, false)) {
                        //IF ATTACKER IS GOING TO GET KO FROM A FASTER OPPONENT, THEN IT WILL USE AN OFFENSIVE PRIORITY MOVE
                        return move
                }
                if (move.move is BattleFieldSideMove
                    && ((move.move as BattleFieldSideMove).target == Target.SELF || !opponent.hasAbility(Ability.MAGIC_BOUNCE))
                    && !opponentBattleSide.battleSideEffects.contains(BattleSideEffect.moveNameToTeamEffect(move.move.name))
                    && !attackerBattleSide.battleSideEffects.contains(BattleSideEffect.moveNameToTeamEffect(move.move.name)))
                        return move
                if (move.move is HealMove && attacker.hp / attacker.currentHP > 3)
                    return move
                move.move.status.forEach {
                    if (Status.isAffectedByStatus(move.move.id, it.status, opponent)
                        && it.probability == null
                        && (!opponentBattleSide.battleSideEffects.contains(BattleSideEffect.SAFEGUARD) || (!it.status.activeOutsideBattle && it.status != Status.TIRED && it.status != Status.CONFUSED))
                        && !opponent.hasAbility(Ability.MAGIC_BOUNCE))
                        return move
                }
                if (move.move is StatChangeMove) {
                    val statChangeMove: StatChangeMove = move.move as StatChangeMove
                    if (statChangeMove.target == Target.SELF) {
                        if (statChangeMove.increaseStat(Stats.SPEED)) {
                            if (BattleUtils.isFaster(opponent, attacker, battleField, false)
                                && !opponent.canBeKOdBy(attacker, battleField, opponentBattleSide)) {
                                //OPPONENT IS FASTER AND CAN'T KO ATTACKER
                                return move
                            }
                        } else if (shouldUpdateStats(statChangeMove, attacker) &&
                            ((BattleUtils.isFaster(attacker, opponent, battleField, false)
                                    && !attacker.canBeKOdBy(opponent, battleField, opponentBattleSide))
                            || canTakeTwoHits(attacker, opponent, battleField, opponentBattleSide))) {
                            //ATTACKER IS FASTER AND CAN SURVIVE 1 HIT OR CAN SURVIVE 2 HITS FROM OPPONENT
                            return move
                        }

                    } else if (move.move.category != MoveCategory.OTHER || !opponent.hasAbility(Ability.MAGIC_BOUNCE)) {
                        //ATTACKER SHOULD ONLY LOWER SPEED AND ACCURACY OF OPPONENT
                        if (statChangeMove.decreaseOpponentStat(Stats.ACCURACY)
                            && opponent.battleData!!.statsMultiplier.accuracyMultiplicator == 1f
                            && !opponent.hasAbility(Ability.KEEN_EYE)
                            && !opponent.hasAbility(Ability.CLEAR_BODY))
                            return move
                        if (statChangeMove.decreaseOpponentStat(Stats.SPEED)
                            && opponent.battleData!!.statsMultiplier.speedMultiplicator == 1f
                            && !opponent.hasAbility(Ability.CLEAR_BODY))
                            return move
                    }
                }
                if (move.move is RemoveStatChangesMove && (opponent.battleData!!.statsMultiplier.attackMultiplicator > 1f
                            || opponent.battleData!!.statsMultiplier.spAtkMultiplicator > 1f
                            || opponent.battleData!!.statsMultiplier.speedMultiplicator > 1f))
                    return move
                if (damage > maxDamage) {
                    maxDamageIdx = Idx
                    maxDamage = damage
                }
            }
            return usableMoves[maxDamageIdx]
        }

        fun shouldSwitch(attacker: Pokemon, opponent: Pokemon, trainerTeam: List<Pokemon>, battleField: BattleField, battleSide: BattleSide, opponentBattleSide: BattleSide): Pokemon? {
            val team = trainerTeam.filter { it.currentHP > 0 && it != attacker }
            if (attacker.battleData!!.battleStatus.contains(Status.TIRED)
                || (attacker.canBeKOdBy(opponent, battleField, battleSide)
                        && opponent.currentHP >= opponent.hp * 0.4f)
            ) {
                for (pokemon in team) {
                    if ((pokemon.speed > opponent.speed * opponent.battleData!!.statsMultiplier.speedMultiplicator
                                || canTakeTwoHits(pokemon, opponent, battleField, battleSide))
                        && opponent.canBeKOdBy(pokemon, battleField, opponentBattleSide)
                        && pokemon.canBeKOdBy(opponent, battleField, battleSide)
                    )
                        return pokemon
                }
            }
            return null
        }

        fun getBestPokemonToSentAfterKo(opponent: Pokemon, trainerTeam: List<Pokemon>, battleField: BattleField, battleSide: BattleSide, opponentBattleSide: BattleSide): Pokemon? {
            val team = trainerTeam.filter { it.currentHP > 0 }
            if (team.isEmpty())
                return null
            val scores: List<Int> = team.map {
                var score = 0
                if (it.speed > opponent.speed * opponent.battleData!!.statsMultiplier.speedMultiplicator) {
                    score += 1
                    if (opponent.canBeKOdBy(it, battleField, opponentBattleSide))
                        score += 3
                    if (canTakeTwoHits(it, opponent, battleField, battleSide))
                        score += 1
                } else if (!it.canBeKOdBy(opponent, battleField, battleSide)
                            && opponent.canBeKOdBy(it, battleField, opponentBattleSide))
                    score += 2
                if (canTakeTwoHits(it, opponent, battleField, battleSide))
                    score += 1
                score
            }
            return team[scores.indexOf(scores.maxOrNull())]
        }
    }
}