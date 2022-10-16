package com.pokemon.android.version.model.battle

import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.*
import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.BattleUtils
import com.pokemon.android.version.utils.ItemUtils

abstract class Battle {
    lateinit var activity: MainActivity
    lateinit var pokemon: Pokemon
    lateinit var opponent: Pokemon
    lateinit var levelData: LevelData
    lateinit var dialogTextView: TextView
    var trainerHasUsedMegaEvolution = false

    abstract fun getBattleState(): State

    abstract fun updateOpponent()

    protected fun opponentTurn(opponentPokemonMove: PokemonMove): String {
        var action = ""
        if (opponent.battleData!!.confusionCounter == 4) {
            opponent.battleData!!.battleStatus.remove(Status.CONFUSED)
            opponent.battleData!!.confusionCounter = 0
            action += "The opposing ${opponent.data.name} is no longer confused!\n"
        }
        val opponentResponse = opponent.attack(opponentPokemonMove, pokemon)
        return if (!opponentResponse.success)
            action + opponentResponse.reason
        else {
            action +
                "The opposing ${opponent.data.name} uses ${opponentPokemonMove.move.name}\n" + opponentResponse.reason
        }
    }

    private fun trainerTurn(trainerPokemonMove: PokemonMove): String {
        var action = ""
        if (pokemon.battleData!!.confusionCounter == 4) {
            pokemon.battleData!!.battleStatus.remove(Status.CONFUSED)
            pokemon.battleData!!.confusionCounter = 0
            action += pokemon.data.name + " is no longer confused!\n"
        }
        val response = pokemon.attack(trainerPokemonMove, opponent)
        return if (!response.success)
            action + response.reason
        else {
            action + "${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n" + response.reason
        }
    }

    fun turn(trainerPokemonMove: PokemonMove, megaEvolution: Boolean) {
        val sb = StringBuilder()
        if (megaEvolution) {
            sb.append("${pokemon.data.name} has Mega Evolved into Mega-${pokemon.data.name}\n")
            pokemon.megaEvolve()
            trainerHasUsedMegaEvolution = true
        }
        if (this is TrainerBattle && opponent.canMegaEvolve()) {
            when (levelData.id) {
                60 -> {
                    if (opponent.data.id == 6) {
                        opponent.megaEvolve()
                        sb.append("${opponent.data.name} has Mega Evolved into Mega-${opponent.data.name}\n")
                    }
                }
                65 -> {
                    if (opponent.data.id == 3) {
                        opponent.megaEvolve()
                        sb.append("${opponent.data.name} has Mega Evolved into Mega-${opponent.data.name}\n")
                    }
                }
            }
        }
        val opponentMove: PokemonMove = opponent.ia(pokemon)
        if (BattleUtils.trainerStarts(pokemon, opponent, trainerPokemonMove.move, opponentMove.move)) {
            sb.append(trainerTurn(trainerPokemonMove))
            if (opponent.currentHP > 0 && pokemon.currentHP > 0) {
                sb.append(opponentTurn(opponentMove))
            }
        } else {
            sb.append(opponentTurn(opponentMove))
            if (opponent.currentHP > 0 && pokemon.currentHP > 0) {
                sb.append(trainerTurn(trainerPokemonMove))
            }
        }
        endTurn(sb)
        if (pokemon.currentHP > 0 && getBattleState() == State.IN_PROGRESS && pokemon.battleData!!.unableToMoveCounter == 1) {
            sb.append(pokemon.data.name + " needs to recharge!\n")
            sb.append(opponentTurn(opponent.ia(pokemon)))
            endTurn(sb)
        }
        dialogTextView.text = sb.toString()
    }

    fun itemIsUsable(itemId: Int): Boolean {
        if (itemId < 15 && itemId != 8 && itemId != 9) {
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
        if (this.pokemon.hasAbility(Ability.REGENERATOR))
            this.pokemon.currentHP = if (this.pokemon.hp/3 + this.pokemon.currentHP > this.pokemon.hp) this.pokemon.hp else this.pokemon.hp/3 + this.pokemon.currentHP
        this.pokemon = pokemonToBeSent
    }

    open fun turnWithSwitch(pokemonToBeSent: Pokemon) {
        val sb = StringBuilder()
        sb.append("${(pokemonToBeSent.trainer!! as Trainer).name} sends ${pokemonToBeSent.data.name}\n")
        switchPokemon(pokemonToBeSent)
        sb.append(BattleUtils.abilitiesCheck(pokemon, opponent))
        sb.append(opponentTurn(opponent.ia(pokemon)))
        endTurn(sb)
        dialogTextView.text = sb.toString()
    }

    fun turnWithItemUsed(itemId: Int) {
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
                    dialogTextView.text = sb.toString()
                    if (this.encountersLeft > 0)
                        this.generateRandomEncounter()
                    return
                } else {
                    sb.append(opponent.data.name + " broke free!\n")
                }
            }
        } else {
            sb.append(activity.trainer!!.name + " uses " + activity.gameDataService.items.first { it.id == itemId }.name + "!\n")
            activity.trainer!!.useItem(itemId, pokemon)
        }
        sb.append(opponentTurn(opponent.ia(pokemon)))
        endTurn(sb)
        dialogTextView.text = sb.toString()
    }

    protected fun endTurn(sb: StringBuilder) {
        if (opponent.currentHP > 0) {
            sb.append(checkStatus(opponent))
            if (pokemon.currentHP == 0){
                if (opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE))
                    opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
                if (opponent.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE))
                    opponent.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE)
            }
            if (opponent.hasAbility(Ability.SPEED_BOOST) && opponent.battleData != null) {
                opponent.battleData!!.speedMultiplicator *= 1.5f
                sb.append("Speed Boost: the opposing ${opponent.data.name}'s speed rose!\n")
            }
        }
        if (pokemon.currentHP > 0) {
            if (opponent.currentHP == 0){
                if (pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE))
                    pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
                if (pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITHOUT_DAMAGE))
                    pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITHOUT_DAMAGE)
            }
            sb.append(checkStatus(pokemon))
            if (pokemon.hasAbility(Ability.SPEED_BOOST) && pokemon.battleData != null) {
                pokemon.battleData!!.speedMultiplicator *= 1.5f
                sb.append("Speed Boost: ${pokemon.data.name}'s speed rose!\n")
            }
        }
        if (pokemon.currentHP == 0) {
            sb.append(pokemon.data.name + " fainted\n")
            pokemon.status = Status.OK
            pokemon.battleData = null
            if (pokemon.isMegaEvolved()) {
                pokemon.recomputeStat()
            }
            if (opponent.currentHP > 0 && opponent.hasAbility(Ability.MOXIE)) {
                opponent.battleData!!.attackMultiplicator *= 1.5f
                sb.append("Moxie: the opposing ${opponent.data.name}'s attack rose!\n")
            }
        }
        if (opponent.currentHP == 0) {
            sb.append("The opposing " + opponent.data.name + " fainted\n")
            if (opponent.isMegaEvolved()) {
                opponent.recomputeStat()
            }
            if (pokemon.currentHP > 0 && pokemon.hasAbility(Ability.MOXIE)) {
                pokemon.battleData!!.attackMultiplicator *= 1.5f
                sb.append("Moxie: ${pokemon.data.name}'s attack rose!\n")
            }
            updateOpponent()
            if (getBattleState() == State.IN_PROGRESS) {
                sb.append(BattleUtils.abilitiesCheck(opponent, pokemon))
            }
        }
    }

    companion object {

        fun checkStatus(pokemon: Pokemon): String {
            var details = ""
            if (pokemon.battleData!!.battleStatus.contains(Status.CONFUSED)) {
                pokemon.battleData!!.confusionCounter++
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.TRAPPED_WITH_DAMAGE)) {
                pokemon.battleData!!.trapCounter++
                if (pokemon.battleData!!.trapCounter == 5) {
                    pokemon.battleData!!.battleStatus.remove(Status.TRAPPED_WITH_DAMAGE)
                    pokemon.battleData!!.trapCounter = 0
                } else if (!pokemon.hasAbility(Ability.MAGIC_GUARD)) {
                    pokemon.currentHP =
                        if ((pokemon.hp / 8) >= pokemon.currentHP) 0 else pokemon.currentHP - (pokemon.hp / 8)
                }
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.FLINCHED))
                pokemon.battleData!!.battleStatus.remove(Status.FLINCHED)
            if (pokemon.battleData!!.battleStatus.contains(Status.TIRED)) {
                if (pokemon.battleData!!.sleepCounter == 0)
                    pokemon.battleData!!.sleepCounter += 1
                else {
                    pokemon.battleData!!.battleStatus.remove(Status.TIRED)
                    pokemon.battleData!!.sleepCounter = 0
                    pokemon.status = Status.ASLEEP
                    details += pokemon.data.name + " fell asleep!\n"
                }
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.UNABLE_TO_MOVE)) {
                if (pokemon.battleData!!.unableToMoveCounter == 1) {
                    pokemon.battleData!!.battleStatus.remove(Status.UNABLE_TO_MOVE)
                    pokemon.battleData!!.unableToMoveCounter = 0
                } else
                    pokemon.battleData!!.unableToMoveCounter++
            }
            if (pokemon.status != Status.OK && (!pokemon.hasAbility(Ability.MAGIC_GUARD) || pokemon.status != Status.OK)) {
                when (pokemon.status) {
                    Status.POISON -> {
                        pokemon.currentHP -= pokemon.hp / 8
                        if (pokemon.currentHP <= 0) {
                            pokemon.currentHP = 0
                            pokemon.status = Status.OK
                        }
                        details += pokemon.data.name + " suffers from the poison!\n"
                    }
                    Status.BADLY_POISON -> {
                        pokemon.currentHP -= pokemon.battleData!!.poisonCounter * (pokemon.hp / 16)
                        pokemon.battleData!!.poisonCounter++
                        if (pokemon.currentHP <= 0) {
                            pokemon.currentHP = 0
                            pokemon.status = Status.OK
                        }
                        details += pokemon.data.name + " suffers from the poison!\n"
                    }
                    Status.BURN -> {
                        pokemon.currentHP -= pokemon.hp / 16
                        if (pokemon.currentHP <= 0) {
                            pokemon.currentHP = 0
                            pokemon.status = Status.OK
                        }
                        details += pokemon.data.name + " suffers from its burn!\n"
                    }
                    Status.ASLEEP -> {
                        pokemon.battleData!!.sleepCounter++
                    }
                    else -> {}
                }
            }
            return details
        }
    }
}