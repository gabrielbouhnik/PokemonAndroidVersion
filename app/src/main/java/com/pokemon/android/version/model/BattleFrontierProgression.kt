package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.entity.save.BattleFrontierSave

class BattleFrontierProgression(var progression : Int, var team : MutableList<Pokemon>) {
    companion object{
        fun of(battleFrontierSave: BattleFrontierSave, gameDataService: GameDataService, trainer: Trainer) : BattleFrontierProgression{
            return BattleFrontierProgression(battleFrontierSave.progression, battleFrontierSave.team.map{Pokemon.of(it, gameDataService, trainer)}.toMutableList())
        }
    }
}