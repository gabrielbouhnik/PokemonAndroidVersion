package com.pokemon.android.version.model.battle

import android.widget.TextView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.item.Ball
import com.pokemon.android.version.model.item.ItemData
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.ui.BattleUI
import com.pokemon.android.version.utils.ItemUtils

abstract class Battle {
    lateinit var activity: MainActivity
    lateinit var pokemon: Pokemon
    lateinit var opponent: Pokemon
    lateinit var levelData: LevelData
    lateinit var dialogTextView: TextView

    abstract fun getBattleState(): State

    abstract fun turn(trainerPokemonMove: PokemonMove)

    fun switchPokemon(pokemonToBeSent: Pokemon) {
        this.pokemon.battleData = PokemonBattleData()
        this.pokemon = pokemonToBeSent
    }

    fun turnWithSwitch(pokemonToBeSent: Pokemon) {
        var sb = StringBuilder()
        sb.append("${pokemonToBeSent.trainer!!.name} send ${pokemonToBeSent.data.name}\n")
        switchPokemon(pokemonToBeSent)
        sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
        var opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
        if (!opponentResponse.success)
            sb.append(opponentResponse.reason)
        dialogTextView.text = sb.toString()
    }

    fun turnWithItemUsed(itemId: Int) {
        var sb = StringBuilder()
        if (ItemUtils.getItemById(itemId) is Ball) {
            if (this is WildBattle) {
                if (activity.trainer!!.catchPokemon(opponent, itemId)) {
                    dialogTextView.text = activity.trainer!!.name + " caught ${opponent.data.name}!\n"
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
        sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
        var opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
        if (!opponentResponse.success)
            sb.append(opponentResponse.reason)
        dialogTextView.text = sb.toString()
    }

    companion object {
        fun checkStatus(pokemon: Pokemon): String {
            if (pokemon.status != Status.OK) {
                when (pokemon.status) {
                    Status.POISON -> {
                        pokemon.currentHP = pokemon.battleData!!.poisonCounter * (pokemon.hp / 16)
                        pokemon.battleData!!.poisonCounter++
                        return pokemon.data.name + " suffers from the poison\n"
                    }
                    Status.BURN -> {
                        pokemon.currentHP -= pokemon.hp / 16
                        return pokemon.data.name + " suffers from its burn\n"
                    }
                    Status.ASLEEP -> {
                        pokemon.battleData!!.sleepCounter++
                        return pokemon.data.name + " is sleeping\n"
                    }
                }
            }
            return ""
        }
    }
}