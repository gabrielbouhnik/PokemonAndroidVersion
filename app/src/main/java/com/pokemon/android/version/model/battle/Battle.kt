package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.*
import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.level.LeaderLevelData
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.move.ChargedMove
import com.pokemon.android.version.model.move.MoveCategory
import com.pokemon.android.version.model.move.RampageMove
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.BattleUtils
import com.pokemon.android.version.utils.IAUtils
import com.pokemon.android.version.utils.ItemUtils
import kotlin.random.Random

abstract class Battle {
    lateinit var activity: MainActivity
    lateinit var pokemon: Pokemon
    lateinit var opponent: Pokemon
    lateinit var levelData: LevelData
    var trainerHasUsedMegaEvolution = false

    abstract fun getBattleState(): State

    abstract fun updateOpponent()

    private fun opponentTurn(opponentPokemonMove: PokemonMove, opponentMoveIsOffensive: Boolean): String {
        if (opponentPokemonMove.move.id == 208 && !opponentMoveIsOffensive) {
            val response = opponent.canAttack(opponentPokemonMove)
            return if (!response.success) response.reason else "${opponent.data.name} uses Sucker Punch!\nBut it failed!\n"
        }
        var action = ""
        if (opponentPokemonMove.move.id == 185) {
            action += "${opponent.data.name} uses Teleport!\n"
            if (opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE)
                || opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE)
            )
                action += "But it failed!\n"
            else if (this is WildBattle) {
                action += "${opponent.data.name} disappeared!\n"
                if (encountersLeft > 0) {
                    generateRandomEncounter()
                }
            }
            return action
        }
        if (opponent.battleData!!.confusionCounter == 4) {
            opponent.battleData!!.battleStatus.remove(Status.CONFUSED)
            opponent.battleData!!.confusionCounter = 0
            action += "The opposing ${opponent.data.name} is no longer confused!\n"
        }
        if (opponentPokemonMove.move is ChargedMove && opponent.battleData!!.chargedMove == null) {

            action += "The opposing ${opponent.data.name} uses ${opponentPokemonMove.move.name}!\nThe opposing " + opponent.data.name + (opponentPokemonMove.move as ChargedMove).chargeText
            if (opponent.hasItem(HoldItem.POWER_HERB)) {
                action += "${opponent.data.name} became fully charged due to its Power Herb!\n"
                opponent.heldItem = null
            }
            else {
                opponent.battleData!!.chargedMove = opponentPokemonMove
                return action
            }
        }
        val opponentResponse = opponent.attack(opponentPokemonMove, pokemon)
        return if (!opponentResponse.success) {
            action + opponentResponse.reason
        } else {
            action +
                    "The opposing ${opponent.data.name} uses ${opponentPokemonMove.move.name}!\n" + opponentResponse.reason
        }
    }

    private fun trainerTurn(trainerPokemonMove: PokemonMove): String {
        var action = ""
        if (pokemon.battleData!!.confusionCounter == 4) {
            pokemon.battleData!!.battleStatus.remove(Status.CONFUSED)
            pokemon.battleData!!.confusionCounter = 0
            action += pokemon.data.name + " is no longer confused!\n"
        }
        if (trainerPokemonMove.move is ChargedMove && pokemon.battleData!!.chargedMove == null) {
            action += "${pokemon.data.name} uses ${trainerPokemonMove.move.name}!\n" + pokemon.data.name + (trainerPokemonMove.move as ChargedMove).chargeText
            if (pokemon.hasItem(HoldItem.POWER_HERB)) {
                action += "${pokemon.data.name} became fully charged due to its Power Herb!\n"
                pokemon.heldItem = null
            }
            else {
                pokemon.battleData!!.chargedMove = trainerPokemonMove
                return action
            }
        }
        val response = pokemon.attack(trainerPokemonMove, opponent)
        return if (!response.success)
            action + response.reason
        else {
            action + "${pokemon.data.name} uses ${trainerPokemonMove.move.name}!\n" + response.reason
        }
    }

    private fun turn(trainerPokemonMove: PokemonMove, sb: StringBuilder) {
        val opponentMove: PokemonMove =
            if (this is WildBattle) IAUtils.iaWildPokemon(opponent) else IAUtils.ia(opponent, pokemon)
        if (opponentMove.move is RampageMove) {
            opponent.battleData!!.rampageMove = opponentMove
        }
        if (this is TrainerBattle || this is BattleFrontierBattle || this is LeaderBattle) {
            val opponentTrainer = opponent.trainer!! as OpponentTrainer
            if (opponentTrainer.iaLevel == 3
                && !opponent.battleData!!.battleStatus.contains(Status.UNABLE_TO_MOVE)
                && opponent.battleData!!.chargedMove == null
                && !opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE)
                && !opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE)
            ) {
                val pokemonToSend = IAUtils.shouldSwitch(opponent, pokemon, opponentTrainer.getTrainerTeam())
                if (pokemonToSend != null) {
                    sb.append(turnWithOpponentSwitch(opponentTrainer, pokemonToSend, trainerPokemonMove))
                    return
                }
            }
        }
        if (BattleUtils.trainerStarts(pokemon, opponent, trainerPokemonMove.move, opponentMove.move)) {
            if (trainerPokemonMove.move.id == 208 && opponentMove.move.category == MoveCategory.OTHER)
                sb.append("${pokemon.data.name} uses Sucker Punch!\nBut it failed!\n")
            else
                sb.append(trainerTurn(trainerPokemonMove))
            if (opponent.currentHP > 0 && pokemon.currentHP > 0) {
                sb.append(opponentTurn(opponentMove, trainerPokemonMove.move.category != MoveCategory.OTHER))
            }
        } else {
            sb.append(opponentTurn(opponentMove, trainerPokemonMove.move.category != MoveCategory.OTHER))
            if (opponent.currentHP > 0 && pokemon.currentHP > 0) {
                sb.append(trainerTurn(trainerPokemonMove))
            }
        }
    }

    fun turn(trainerPokemonMove: PokemonMove, megaEvolution: Boolean): String {
        val sb = StringBuilder()
        if (megaEvolution) {
            sb.append("${pokemon.data.name} has Mega Evolved into Mega ${pokemon.data.name}\n")
            pokemon.megaEvolve()
            trainerHasUsedMegaEvolution = true
        }
        var shouldMegaEvolve = true
        if (this is TrainerBattle) {
            val opponentTrainer = opponent.trainer!! as OpponentTrainer
            shouldMegaEvolve = !(opponentTrainer.iaLevel == 3
                    && !opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE)
                    && !opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE)
                    && IAUtils.shouldSwitch(opponent, pokemon, opponentTrainer.getTrainerTeam()) != null)
        }
        if (opponent.canMegaEvolve() && shouldMegaEvolve && opponent.heldItem == null) {
            if (this is LeaderBattle) {
                val leaderLevelData: LeaderLevelData = levelData as LeaderLevelData
                val megaPokemonId : Int = if ((pokemon.trainer!! as Trainer).progression < LevelMenu.ELITE_4_LAST_LEVEL_ID) leaderLevelData.megaPokemonId else leaderLevelData.megaPokemonIdOnRematch
                if (opponent.data.id != megaPokemonId) {
                    shouldMegaEvolve = false
                }
            }
            else if (this is TrainerBattle) {
                if ((this.levelData as TrainerBattleLevelData).megaPokemonId != opponent.data.id) {
                    shouldMegaEvolve = false
                }
            } else
                shouldMegaEvolve = false
            if (shouldMegaEvolve) {
                opponent.megaEvolve()
                sb.append("The opposing ${opponent.data.name} is reacting to ${(opponent.trainer!! as OpponentTrainer).name}'s Key Stone!\n")
                sb.append("The opposing ${opponent.data.name} has Mega Evolved into Mega ${opponent.data.name}\n")
            }
        }
        turn(trainerPokemonMove, sb)
        endTurn(sb)
        if (pokemon.currentHP > 0 && getBattleState() == State.IN_PROGRESS) {
            val opponentMove: PokemonMove =
                if (this is WildBattle) IAUtils.iaWildPokemon(opponent) else IAUtils.ia(opponent, pokemon)
            if (opponentMove.move is RampageMove) {
                opponent.battleData!!.rampageMove = opponentMove
            }
            when {
                pokemon.battleData!!.unableToMoveCounter == 1 -> {
                    sb.append(pokemon.data.name + " needs to recharge!\n")
                    sb.append(opponentTurn(opponentMove, trainerPokemonMove.move.category != MoveCategory.OTHER))
                    endTurn(sb)
                }
                pokemon.battleData!!.chargedMove != null -> {
                    turn(trainerPokemonMove, sb)
                    pokemon.battleData!!.chargedMove = null
                    endTurn(sb)
                }
                trainerPokemonMove.move is RampageMove -> {
                    turn(trainerPokemonMove, sb)
                    if (pokemon.currentHP > 0 && !pokemon.hasAbility(Ability.OWN_TEMPO)
                        && !pokemon.battleData!!.battleStatus.contains(Status.CONFUSED)
                    ) {
                        pokemon.battleData!!.battleStatus.add(Status.CONFUSED)
                        sb.append("${pokemon.data.name} became confused due to fatigue!\n")
                    }
                    endTurn(sb)
                }
            }
        }
        return sb.toString()
    }

    fun itemIsUsable(itemId: Int): Boolean {
        if (itemId < 16 && itemId != 8 && itemId != 9) {
            if (itemId > 10)
                return this is WildBattle
            return true
        }
        return false
    }

    fun switchPokemon(pokemonToBeSent: Pokemon) {
        pokemonToBeSent.battleData = PokemonBattleData()
        this.pokemon.battleData = PokemonBattleData()
        if (this.pokemon.hasAbility(Ability.NATURAL_CURE))
            this.pokemon.status = Status.OK
        if (this.pokemon.hasAbility(Ability.REGENERATOR) && this.pokemon.currentHP > 0)
            this.pokemon.heal(this.pokemon.hp / 3)
        if (opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE))
            opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
        if (opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE))
            opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE)
        this.pokemon = pokemonToBeSent
    }

    private fun switchOpponent(pokemonToBeSent: Pokemon) {
        pokemonToBeSent.battleData = PokemonBattleData()
        this.opponent.battleData = PokemonBattleData()
        if (this.opponent.hasAbility(Ability.REGENERATOR) && this.opponent.currentHP > 0)
            this.opponent.heal(this.pokemon.hp / 3)
        if (this.opponent.hasAbility(Ability.NATURAL_CURE))
            this.opponent.status = Status.OK
        if (pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE))
            pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
        if (pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE))
            pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE)
        this.opponent = pokemonToBeSent
    }

    private fun turnWithOpponentSwitch(
        trainer: OpponentTrainer,
        pokemonToBeSent: Pokemon,
        trainerPokemonMove: PokemonMove
    ): String {
        val sb = StringBuilder()
        sb.append("${trainer.name} withdrew ${opponent.data.name}!\n${trainer.name} sends out ${pokemonToBeSent.data.name}!\n")
        switchOpponent(pokemonToBeSent)
        sb.append(BattleUtils.abilitiesCheck(opponent, pokemon))
        if (trainerPokemonMove.move.id == 208) {
            val response = opponent.canAttack(trainerPokemonMove)
            sb.append(if (!response.success) response.reason else "${pokemon.data.name} uses Sucker Punch!\nBut it failed!\n")
        }
        else
            sb.append(trainerTurn(trainerPokemonMove))
        return sb.toString()
    }

    fun turnWithSwitch(pokemonToBeSent: Pokemon): String {
        val sb = StringBuilder()
        sb.append("${activity.trainer!!.name} withdrew ${pokemon.data.name}!\n${activity.trainer!!.name} sends ${pokemonToBeSent.data.name}\n")
        var opponentMove: PokemonMove =
            if (this is WildBattle) IAUtils.iaWildPokemon(opponent) else IAUtils.ia(opponent, pokemon)
        switchPokemon(pokemonToBeSent)
        sb.append(BattleUtils.abilitiesCheck(pokemon, opponent))
        if (this is BossBattle)
            opponentMove = IAUtils.ia(opponent, pokemon)
        if (opponentMove.move.id == 208) {
            val response = opponent.canAttack(opponentMove)
            sb.append(if (response.success) "${opponent.data.name} uses Sucker Punch!\nBut it failed!\n" else response.reason)
        }
        else
            sb.append(opponentTurn(opponentMove, false))
        endTurn(sb)
        return sb.toString()
    }

    fun turnWithItemUsed(itemId: Int): String {
        val sb = StringBuilder()
        if (ItemUtils.getItemById(itemId) is Ball) {
            if (this is WildBattle) {
                sb.append(activity.trainer!!.name + " throws a " + activity.gameDataService.items.first { it.id == itemId }.name + "!\n")
                if (activity.trainer!!.getMaxLevel() > opponent.level && activity.trainer!!.catchPokemon(
                        opponent,
                        itemId
                    )
                ) {
                    sb.append(activity.trainer!!.name + " caught ${opponent.data.name}!\n")
                    opponent.battleData = null
                    if (this.encountersLeft > 0)
                        this.generateRandomEncounter()
                    return sb.toString()
                } else {
                    sb.append(opponent.data.name + " broke free!\n")
                }
            }
        } else {
            sb.append(activity.trainer!!.name + " uses " + activity.gameDataService.items.first { it.id == itemId }.name + "!\n")
            activity.trainer!!.useItem(itemId, pokemon)
        }
        val opponentMove: PokemonMove =
            if (this is WildBattle) IAUtils.iaWildPokemon(opponent) else IAUtils.ia(opponent, pokemon)
        sb.append(opponentTurn(opponentMove, false))
        endTurn(sb)
        return sb.toString()
    }

    private fun endTurn(sb: StringBuilder) {
        if (pokemon.status != Status.OK && pokemon.hasAbility(Ability.SHED_SKIN) && Random.nextInt(2) == 0) {
            sb.append("${pokemon.data.name}'s Shed Skin: ${pokemon.data.name} cured its status!\n")
            pokemon.status = Status.OK
        }
        if (opponent.status != Status.OK && opponent.hasAbility(Ability.SHED_SKIN) && Random.nextInt(2) == 0) {
            sb.append("${opponent.data.name}'s Shed Skin: ${opponent.data.name} cured its status!\n")
            opponent.status = Status.OK
        }
        if (pokemon.currentHP > 0 && opponent.currentHP > 0) {
            if (opponent.battleData!!.battleStatus.contains(Status.LEECH_SEEDED)) {
                val damage = if (opponent.currentHP < 16) 1 else opponent.currentHP / 8
                if (opponent.hasAbility(Ability.LIQUID_OOZE)) {
                    sb.append("${opponent.data.name}'s Liquid Ooze: ${pokemon.data.name} loses some hp.\n")
                    pokemon.takeDamage(damage)
                } else {
                    sb.append("The opposing ${opponent.data.name}'s health is sapped by Leech Seed\n")
                    opponent.takeDamage(damage)
                    pokemon.heal(damage)
                }
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.LEECH_SEEDED)) {
                val damage = if (pokemon.currentHP < 16) 1 else pokemon.currentHP / 8
                if (pokemon.hasAbility(Ability.LIQUID_OOZE)) {
                    sb.append("${pokemon.data.name}'s Liquid Ooze: ${opponent.data.name} loses some hp.\n")
                    opponent.takeDamage(damage)
                } else {
                    sb.append("${pokemon.data.name}'s health is sapped by Leech Seed\n")
                    pokemon.takeDamage(damage)
                    opponent.heal(damage)
                }
            }
        }
        if (pokemon.currentHP > 0 && opponent.currentHP > 0) {
            if (pokemon.hasAbility(Ability.SAND_STREAM) && !opponent.hasType(Type.ROCK) && !opponent.hasType(Type.GROUND) && !opponent.hasType(
                    Type.STEEL
                )
            ) {
                sb.append("${opponent.data.name} is buffeted by the sandstorm!\n")
                opponent.takeDamage(opponent.hp / 8)
            }
            if (opponent.hasAbility(Ability.SAND_STREAM) && !pokemon.hasType(Type.ROCK) && !pokemon.hasType(Type.GROUND) && !pokemon.hasType(
                    Type.STEEL
                )
            ) {
                pokemon.takeDamage(pokemon.hp / 8)
                sb.append("${pokemon.data.name} is buffeted by the sandstorm!\n")
            }
        }
        if (opponent.currentHP > 0) {
            if (opponent.battleData!!.rampageMove != null) {
                opponent.battleData!!.rampageCounter += 1
                if (opponent.battleData!!.rampageCounter == 2) {
                    opponent.battleData!!.rampageCounter = 0
                    opponent.battleData!!.rampageMove = null
                }
                if (!opponent.hasAbility(Ability.OWN_TEMPO)
                    && !opponent.battleData!!.battleStatus.contains(Status.CONFUSED)
                ) {
                    opponent.battleData!!.battleStatus.add(Status.CONFUSED)
                    sb.append("The opposing ${opponent.data.name} became confused due to fatigue!\n")
                }
            }
            sb.append(checkStatus(opponent))
            if (pokemon.currentHP == 0) {
                if (opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE))
                    opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
                if (opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE))
                    opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE)
            }
            if (opponent.hasAbility(Ability.SPEED_BOOST) && opponent.battleData != null) {
                opponent.battleData!!.speedMultiplicator *= 1.5f
                sb.append("${opponent.data.name}'s Speed Boost: the opposing ${opponent.data.name}'s speed rose!\n")
            }
        }
        if (pokemon.currentHP > 0) {
            if (opponent.currentHP == 0) {
                if (pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE))
                    pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
                if (pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE))
                    pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE)
            }
            sb.append(checkStatus(pokemon))
            if (pokemon.hasAbility(Ability.SPEED_BOOST) && pokemon.battleData != null) {
                pokemon.battleData!!.speedMultiplicator *= 1.5f
                sb.append("${pokemon.data.name}'s Speed Boost: ${pokemon.data.name}'s speed rose!\n")
            }
        }
        pokemon.battleData!!.lastHitReceived = null
        opponent.battleData!!.lastHitReceived = null
        if (pokemon.currentHP == 0) {
            sb.append(pokemon.data.name + " fainted\n")
            pokemon.status = Status.OK
            pokemon.battleData = null
            if (pokemon.isMegaEvolved) {
                pokemon.recomputeStat()
            }
            if (opponent.currentHP > 0 && opponent.hasAbility(Ability.MOXIE)) {
                opponent.battleData!!.attackMultiplicator *= 1.5f
                sb.append("${opponent.data.name}'s Moxie: the opposing ${opponent.data.name}'s attack rose!\n")
            }
        }
        if (opponent.currentHP == 0) {
            sb.append("The opposing " + opponent.data.name + " fainted\n")
            if (opponent.isMegaEvolved) {
                opponent.recomputeStat()
            }
            if (pokemon.currentHP > 0 && pokemon.hasAbility(Ability.MOXIE)) {
                pokemon.battleData!!.attackMultiplicator *= 1.5f
                sb.append("${pokemon.data.name}'s Moxie: ${pokemon.data.name}'s attack rose!\n")
            }
            updateOpponent()
            if (this is BossBattle && opponent.isMegaEvolved && !this.megaPhase) {
                activity.showCustomDialog("${this.opponent.data.name} has absorbed energy from your Mega Ring and has Mega Evolved! All its HP has been restored")
                this.megaPhase = true
            }
            if (getBattleState() == State.IN_PROGRESS) {
                sb.append(BattleUtils.abilitiesCheck(opponent, pokemon))
            }
        }
    }

    companion object {
        fun checkStatus(pokemon: Pokemon): String {
            var details = ""
            if (pokemon.hasItem(HoldItem.TOXIC_ORB) && Status.isAffectedByStatus(83, Status.BADLY_POISON, pokemon)) {
                pokemon.status = Status.BADLY_POISON
                details += pokemon.data.name + " is badly poisoned by its Toxic Orb\n"
            }
            if (pokemon.hasItem(HoldItem.FLAME_ORB) && Status.isAffectedByStatus(138, Status.BURN, pokemon)) {
                pokemon.status = Status.BURN
                details += pokemon.data.name + " is burned by its Flame Orb\n"
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.CONFUSED)) {
                pokemon.battleData!!.confusionCounter++
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.ROOSTED)) {
                pokemon.battleData!!.battleStatus.remove(Status.ROOSTED)
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE)) {
                pokemon.battleData!!.trapCounter++
                if (pokemon.battleData!!.trapCounter == 5) {
                    pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
                    pokemon.battleData!!.trapCounter = 0
                } else if (!pokemon.hasAbility(Ability.MAGIC_GUARD))
                    pokemon.takeDamage(pokemon.hp / 8)
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.FLINCHED))
                pokemon.battleData!!.battleStatus.remove(Status.FLINCHED)
            if (pokemon.battleData!!.battleStatus.contains(Status.TIRED)) {
                if (pokemon.status != Status.OK) {
                    pokemon.battleData!!.battleStatus.remove(Status.TIRED)
                } else if (pokemon.battleData!!.sleepCounter == 0)
                    pokemon.battleData!!.sleepCounter += 1
                else {
                    pokemon.battleData!!.battleStatus.remove(Status.TIRED)
                    pokemon.battleData!!.sleepCounter = 0
                    pokemon.status = Status.ASLEEP
                    details += pokemon.data.name + " fell asleep!\n"
                    if (pokemon.hasItem(HoldItem.LUM_BERRY)) {
                        details += "${pokemon.data.name}'s Lum Berry cured its status\n"
                        pokemon.status = Status.OK
                        pokemon.consumeItem()
                    }
                }
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.UNABLE_TO_MOVE)) {
                if (pokemon.battleData!!.unableToMoveCounter == 1) {
                    pokemon.battleData!!.battleStatus.remove(Status.UNABLE_TO_MOVE)
                    pokemon.battleData!!.unableToMoveCounter = 0
                } else
                    pokemon.battleData!!.unableToMoveCounter++
            }
            if (pokemon.status != Status.OK) {
                when (pokemon.status) {
                    Status.POISON -> {
                        if (pokemon.hasAbility(Ability.POISON_HEAL)) {
                            pokemon.heal(pokemon.hp / 8)
                            details += "${pokemon.data.name}'s Poison Heal: ${pokemon.data.name} had its hp restored.\n"
                        } else if (!pokemon.hasAbility(Ability.MAGIC_GUARD)) {
                            pokemon.takeDamage(pokemon.hp / 8)
                            details += pokemon.data.name + " suffers from the poison!\n"
                        }
                    }
                    Status.BADLY_POISON -> {
                        if (pokemon.hasAbility(Ability.POISON_HEAL)) {
                            pokemon.heal(pokemon.hp / 8)
                            details += "${pokemon.data.name}'s Poison Heal: ${pokemon.data.name} had its hp restored.\n"
                        } else if (!pokemon.hasAbility(Ability.MAGIC_GUARD)) {
                            pokemon.battleData!!.poisonCounter++
                            pokemon.takeDamage(pokemon.battleData!!.poisonCounter * (pokemon.hp / 16))
                            pokemon.data.name + " suffers from the poison!\n"
                        }
                    }
                    Status.BURN -> {
                        if (!pokemon.hasAbility(Ability.MAGIC_GUARD)) {
                            pokemon.takeDamage(pokemon.hp / 16)
                            details += pokemon.data.name + " suffers from its burn!\n"
                        }
                    }
                    Status.ASLEEP -> {
                        pokemon.battleData!!.sleepCounter++
                    }
                    else -> {}
                }
            }
            if (pokemon.hp > pokemon.currentHP && pokemon.hasItem(HoldItem.LEFTOVERS)) {
                pokemon.heal(pokemon.hp / 16)
                details += pokemon.data.name + " restored a little of its hp using its Leftovers!\n"
            }
            if (pokemon.hasItem(HoldItem.BLACK_SLUDGE) && pokemon.hp > pokemon.currentHP) {
                details += if (pokemon.hasType(Type.POISON)) {
                    pokemon.heal(pokemon.hp / 16)
                    pokemon.data.name + " restored a little of its hp using its Black Sludge!\n"
                } else {
                    pokemon.takeDamage(pokemon.hp / 8)
                    pokemon.data.name + " lost some of its hp because of its Black Sludge!\n"
                }
            }
            return details
        }
    }
}