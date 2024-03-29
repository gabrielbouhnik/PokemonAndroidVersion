package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.ITrainer
import com.pokemon.android.version.model.Pokemon

class OpponentTrainer(var pokemons: List<Pokemon>, var sprite: String) : ITrainer {

    override fun canStillBattle(): Boolean {
        for (pokemon in pokemons) {
            if (pokemon.currentHP > 0)
                return true
        }
        return false
    }

    override fun getFirstPokemonThatCanFight(): Pokemon? {
        for (pokemon in pokemons) {
            if (pokemon.currentHP > 0)
                return pokemon
        }
        return null
    }
}