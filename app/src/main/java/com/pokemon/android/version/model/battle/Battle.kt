package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
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

    abstract fun getBattleState(): State

    abstract fun turn(trainerPokemonMove: PokemonMove)

    fun switchPokemon(pokemonToBeSent : Pokemon) {
        this.pokemon.battleData = PokemonBattleData()
        this.pokemon = pokemonToBeSent
    }

    fun turnWithSwitch(pokemonToBeSent : Pokemon){
        var sb = StringBuilder()
        sb.append("${pokemonToBeSent.trainer!!.name} send ${pokemonToBeSent.data.name}")
        switchPokemon(pokemonToBeSent)
        sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
        if(!opponent.attack(opponent.IA(pokemon), pokemon))
            sb.append("${opponent.data.name}'s attack missed!\n")
        BattleUI.dialogTextView!!.text = sb.toString()
    }

    fun turnWithItemUsed(itemData : ItemData){
        //TODO
        if (ItemUtils.getItemById(itemData.id) is Ball){
        }
        else {
            var sb = StringBuilder()
            sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
            if (!opponent.attack(opponent.IA(pokemon), pokemon))
                sb.append("${opponent.data.name}'s attack missed!\n")
            BattleUI.dialogTextView!!.text = sb.toString()
        }
    }
}