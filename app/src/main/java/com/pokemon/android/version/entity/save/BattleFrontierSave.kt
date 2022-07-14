package com.pokemon.android.version.entity.save

import com.pokemon.android.version.model.BattleFrontierProgression

class BattleFrontierSave(var progression : Int, var team : List<PokemonSave>) {
    companion object {
        fun of(battleFrontierProgression: BattleFrontierProgression): BattleFrontierSave{
            return BattleFrontierSave(battleFrontierProgression.progression,
                battleFrontierProgression.team.map{ PokemonSave.of(it)}
            )
        }
    }
}