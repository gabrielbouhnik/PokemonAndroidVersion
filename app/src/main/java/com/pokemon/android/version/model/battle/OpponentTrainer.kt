package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.ITrainer
import com.pokemon.android.version.model.Pokemon

class OpponentTrainer(var team: List<Pokemon>, var sprite: String) : ITrainer {

    override fun canStillBattle(): Boolean {
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                return true
        }
        return false
    }

    override fun getFirstPokemonThatCanFight(): Pokemon? {
        for (pokemon in team) {
            if (pokemon.currentHP > 0)
                return pokemon
        }
        return null
    }

    override fun getTrainerTeam(): List<Pokemon> {
        return team
    }
}