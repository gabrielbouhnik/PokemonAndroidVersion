package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.BattleFrontierProgression

class BattleFrontierSave(var progression : Int, var team : List<PokemonSave>) {
    companion object {
        fun of(battleFactoryProgression: BattleFrontierProgression): BattleFrontierSave{
            return BattleFrontierSave(battleFactoryProgression.progression,
                battleFactoryProgression.team.map{ PokemonSave.of(it)}
            )
        }
    }
}