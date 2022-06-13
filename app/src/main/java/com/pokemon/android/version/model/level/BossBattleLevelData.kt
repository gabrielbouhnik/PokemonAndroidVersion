package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.BossBattleLevelEntity

class BossBattleLevelData(id: Int,
                          name: String,
                          description: String,
                          rewards: ArrayList<Reward>,
                          music: Int,
                          icon : String,
                          background: String,
                          exp : Int,
                          var boss : BossData,
) : LevelData(id,name,description,rewards,music,icon,background, exp) {
    companion object{
        fun of(bossBattleLevelEntity: BossBattleLevelEntity, gameDataService: GameDataService) : BossBattleLevelData{
            return BossBattleLevelData(
                bossBattleLevelEntity.id,
                bossBattleLevelEntity.name,
                bossBattleLevelEntity.description,
                bossBattleLevelEntity.rewards.map { Reward(it.id, it.quantity) } as ArrayList<Reward>,
                bossBattleLevelEntity.music,
                bossBattleLevelEntity.icon,
                bossBattleLevelEntity.background,
                bossBattleLevelEntity.exp,
                BossData.of(bossBattleLevelEntity.boss, gameDataService)
            )
        }
    }
}