package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.level.LevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove

abstract class Battle {
    lateinit var pokemon: Pokemon
    lateinit var opponent: Pokemon
    lateinit var levelData: LevelData

    abstract fun getBattleState(): State

    abstract fun turn(trainerPokemonMove: PokemonMove)

    fun switchPokemon(pokemonToBeSent : Pokemon) {
        this.pokemon.resetStatChanges()
        this.pokemon = pokemonToBeSent
    }

    fun turnWithSwitch(pokemonToBeSent : Pokemon){
        switchPokemon(pokemonToBeSent)
        opponent.attack(opponent.IA(pokemon), pokemon)
    }
}