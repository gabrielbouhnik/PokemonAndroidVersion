package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.save.BattleFrontierSave

class BattleFrontierProgression(var progression : Int, var team : List<Pokemon>) {
    companion object{
        fun of(battleFactorySave: BattleFrontierSave, gameDataService: GameDataService, trainer: Trainer) : BattleFrontierProgression{
            return BattleFrontierProgression(battleFactorySave.progression, battleFactorySave.team.map{Pokemon.of(it, gameDataService, trainer)})
        }
    }
}