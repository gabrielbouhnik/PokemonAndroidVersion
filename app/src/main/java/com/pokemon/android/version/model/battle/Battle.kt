package com.pokemon.android.version.model.battle

import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Trainer
import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.move.DrainMove
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.RecoilMove
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
            action +=
                "The opposing ${opponent.data.name} uses ${opponentPokemonMove.move.name}\n" + opponentResponse.reason
            action + getTurnDescription(opponent, pokemon, opponentPokemonMove.move)
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
            action += "${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n" + response.reason
            action + getTurnDescription(pokemon, opponent, trainerPokemonMove.move)
        }
    }

    fun turn(trainerPokemonMove: PokemonMove, megaEvolution : Boolean) {
        val sb = StringBuilder()
        if (megaEvolution) {
            sb.append("${pokemon.data.name} has Mega Evolved into Mega-${pokemon.data.name}\n")
            pokemon.megaEvolve()
            trainerHasUsedMegaEvolution = true
        }
        /*if (this is TrainerBattle && opponent.canMegaEvolve()) {
            opponent.megaEvolve()
            sb.append("${opponent.data.name} has Mega Evolved into Mega-${opponent.data.name}\n")
        }*/
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
        this.pokemon = pokemonToBeSent
    }

    open fun turnWithSwitch(pokemonToBeSent: Pokemon) {
        val sb = StringBuilder()
        sb.append("${(pokemonToBeSent.trainer!! as Trainer).name} sends ${pokemonToBeSent.data.name}\n")
        switchPokemon(pokemonToBeSent)
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
            if (pokemon.currentHP == 0 && opponent.battleData!!.battleStatus.contains(Status.TRAPPED))
                opponent.battleData!!.battleStatus.remove(Status.TRAPPED)
        }
        if (pokemon.currentHP > 0) {
            sb.append(checkStatus(pokemon))
            if (opponent.currentHP == 0 && pokemon.battleData!!.battleStatus.contains(Status.TRAPPED))
                pokemon.battleData!!.battleStatus.remove(Status.TRAPPED)
        }
        if (pokemon.currentHP == 0) {
            sb.append(pokemon.data.name + " fainted\n")
            pokemon.status = Status.OK
            pokemon.battleData = null
            if (pokemon.isMegaEvolved()) {
                pokemon.recomputeStat()
            }
        }
        if (opponent.currentHP == 0) {
            sb.append("The opposing " + opponent.data.name + " fainted\n")
            updateOpponent()
        }
    }

    companion object {
        protected fun getTurnDescription(attacker: Pokemon, other: Pokemon, move: Move): String {
            var action = ""
            if (move.power > 0) {
                var effectiveness = move.type.isEffectiveAgainst(other.data.type1) *
                        move.type.isEffectiveAgainst(other.data.type2)
                if (other.isMegaEvolved())
                    effectiveness = move.type.isEffectiveAgainst(other.data.megaEvolutionData!!.type1) *
                            move.type.isEffectiveAgainst(other.data.megaEvolutionData!!.type2)
                if (effectiveness >= 2)
                    action += "It's super effective!\n"
                if (effectiveness == 0f)
                    action += "It does not affect ${other.data.name}\n"
                else if (effectiveness < 1)
                    action += "It's not very effective effective!\n"
            }
            if (move is DrainMove)
                action += "The opposing ${other.data.name} had its energy drained!\n"
            else if (move is RecoilMove)
                action += "${attacker.data.name} is damaged by recoil!\n"
            return action
        }

        fun checkStatus(pokemon: Pokemon): String {
            var details = ""
            if (pokemon.battleData!!.battleStatus.contains(Status.CONFUSED)) {
                pokemon.battleData!!.confusionCounter++
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.TRAPPED)) {
                pokemon.battleData!!.trapCounter++
                if (pokemon.battleData!!.trapCounter == 5) {
                    pokemon.battleData!!.battleStatus.remove(Status.TRAPPED)
                    pokemon.battleData!!.trapCounter = 0
                } else {
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
            if (pokemon.status != Status.OK) {
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