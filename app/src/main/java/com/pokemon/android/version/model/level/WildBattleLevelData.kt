package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.WildBattleLevelEntity

class WildBattleLevelData(
    id: Int,
    name: String,
    description: String,
    rewards: ArrayList<Reward>,
    music: Int,
    icon: String,
    background: String,
    exp: Int,
    var encounter: Int,
    var possibleEncounters: PossibleEncounters,

    ) : LevelData(id, name, description, rewards, music, icon, background, exp) {
    companion object {
        fun of(wildBattleLevelEntity: WildBattleLevelEntity, gameDataService: GameDataService): WildBattleLevelData {
            return WildBattleLevelData(
                wildBattleLevelEntity.id,
                wildBattleLevelEntity.name,
                wildBattleLevelEntity.description,
                wildBattleLevelEntity.rewards.map { Reward(it.id, it.quantity) } as ArrayList<Reward>,
                wildBattleLevelEntity.music,
                wildBattleLevelEntity.icon,
                wildBattleLevelEntity.background,
                wildBattleLevelEntity.exp,
                wildBattleLevelEntity.encounter,
                PossibleEncounters.of(wildBattleLevelEntity.possibleEncounters, gameDataService))
        }
    }
}