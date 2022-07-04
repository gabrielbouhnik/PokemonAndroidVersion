package com.pokemon.android.version.model.battle

import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.move.DrainMove
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

    abstract fun getBattleState(): State

    abstract fun updateOpponent()

    private fun opponentTurn(opponentPokemonMove: PokemonMove) : String{
        val opponentResponse = opponent.attack(opponentPokemonMove, pokemon)
        return if (!opponentResponse.success)
            opponentResponse.reason
        else{
            var action = "The opposing ${opponent.data.name} uses ${opponentPokemonMove.move.name}\n"
            if (opponentPokemonMove.move is DrainMove)
                action += "${pokemon.data.name} had its energy drained!\n"
            else if (opponentPokemonMove.move is RecoilMove)
                action += "The opposing ${opponent.data.name} is damaged by recoil!\n"
            action
        }
    }

    private fun trainerTurn(trainerPokemonMove: PokemonMove) : String{
        val response = pokemon.attack(trainerPokemonMove, opponent)
        return if (!response.success)
            response.reason
        else {
            var action = "${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n"
            if (trainerPokemonMove.move is DrainMove)
                action += "The opposing ${opponent.data.name} had its energy drained!\n"
            else if (trainerPokemonMove.move is RecoilMove)
                action += "${pokemon.data.name} is damaged by recoil!\n"
            action
        }
    }

    fun turn(trainerPokemonMove: PokemonMove){
        val sb = StringBuilder()
        val opponentMove : PokemonMove = opponent.IA(pokemon)
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

    fun itemIsUsable(itemId: Int) : Boolean{
        if (itemId < 15 && itemId != 8 && itemId != 9){
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

    fun turnWithSwitch(pokemonToBeSent: Pokemon) {
        val sb = StringBuilder()
        sb.append("${pokemonToBeSent.trainer!!.name} sends ${pokemonToBeSent.data.name}\n")
        switchPokemon(pokemonToBeSent)
        sb.append(opponentTurn(opponent.IA(pokemon)))
        endTurn(sb)
        dialogTextView.text = sb.toString()
    }

    fun turnWithItemUsed(itemId: Int) {
        val sb = StringBuilder()
        if (ItemUtils.getItemById(itemId) is Ball) {
            if (this is WildBattle) {
                if (activity.trainer!!.getMaxLevel() > opponent.level && activity.trainer!!.catchPokemon(opponent, itemId)) {
                    dialogTextView.text = activity.trainer!!.name + " caught ${opponent.data.name}!\n"
                    if (this.encountersLeft > 0)
                        this.generateRandomEncounter()
                    return
                } else {
                    sb.append(opponent.data.name + " broke free!\n")
                }
            }
        }
        else{
            activity.trainer!!.useItem(itemId, pokemon)
        }
        sb.append(opponentTurn(opponent.IA(pokemon)))
        endTurn(sb)
        dialogTextView.text = sb.toString()
    }

    private fun endTurn(sb : StringBuilder){
        if (opponent.currentHP > 0)
            sb.append(checkStatus(opponent))
        if (pokemon.currentHP > 0)
            sb.append(checkStatus(pokemon))
        if (pokemon.currentHP == 0){
            sb.append(pokemon.data.name + " fainted\n")
            pokemon.status = Status.OK
            pokemon.battleData = null
        }
        if (opponent.currentHP == 0) {
            sb.append("The opposing " + opponent.data.name + " fainted\n")
            updateOpponent()
        }
    }

    companion object {
        fun checkStatus(pokemon: Pokemon): String {
            if (pokemon.status != Status.OK) {
                when (pokemon.status) {
                    Status.POISON -> {
                        pokemon.currentHP -= pokemon.battleData!!.poisonCounter * (pokemon.hp / 16)
                        pokemon.battleData!!.poisonCounter++
                        if (pokemon.currentHP <= 0){
                            pokemon.currentHP = 0
                            pokemon.status = Status.OK
                            pokemon.battleData = null
                        }
                        return pokemon.data.name + " suffers from the poison\n"
                    }
                    Status.BURN -> {
                        pokemon.currentHP -= pokemon.hp / 16
                        if (pokemon.currentHP <= 0){
                            pokemon.currentHP = 0
                            pokemon.status = Status.OK
                            pokemon.battleData = null
                        }
                        return pokemon.data.name + " suffers from its burn\n"
                    }
                    Status.ASLEEP -> {
                        pokemon.battleData!!.sleepCounter++
                    }
                }
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.CONFUSED)) {
                pokemon.battleData!!.confusionCounter++
                if (pokemon.battleData!!.confusionCounter == 4) {
                    pokemon.battleData!!.battleStatus.remove(Status.CONFUSED)
                    pokemon.battleData!!.confusionCounter = 0
                }
            }
            if (pokemon.battleData!!.battleStatus.contains(Status.FLINCHED))
                pokemon.battleData!!.battleStatus.remove(Status.FLINCHED)
            return ""
        }
    }
}