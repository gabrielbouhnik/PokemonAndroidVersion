package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.LevelsEntity

class LevelFactory {
    companion object {
        fun createLevels(levelsEntity: LevelsEntity?, gameDataService: GameDataService): List<LevelData> {
            val res: ArrayList<LevelData> = ArrayList()
            if (levelsEntity != null) {
                levelsEntity.trainerBattles.forEach { res.add(TrainerBattleLevelData.of(it, gameDataService)) }
                levelsEntity.randomWildEncounters.forEach { res.add(WildBattleLevelData.of(it, gameDataService)) }
                levelsEntity.bossBattles.forEach { res.add(BossBattleLevelData.of(it, gameDataService)) }
                levelsEntity.leaderBattles.forEach { res.add(LeaderLevelData.of(it, gameDataService)) }
            }
            res.sortBy { it.id }
            return res
        }
    }
}