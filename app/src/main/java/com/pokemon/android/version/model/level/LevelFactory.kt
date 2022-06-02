package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.LevelsEntity

class LevelFactory {
    companion object{
        fun createLevels(levelsEntity: LevelsEntity?, gameDataService: GameDataService) : List<Level>{
            var res : ArrayList<Level> = ArrayList()
            if (levelsEntity != null) {
                levelsEntity.trainerBattles.forEach { res.add(TrainerBattleLevel.of(it, gameDataService)) }
                levelsEntity.randomWildEncounters.forEach { res.add(WildBattleLevel.of(it, gameDataService)) }
            }
            return res
        }
    }
}