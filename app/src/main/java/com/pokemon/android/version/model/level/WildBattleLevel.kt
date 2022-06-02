package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.PossibleEncountersEntity
import com.pokemon.android.version.entity.level.WildBattleLevelEntity

class WildBattleLevel(
    id: Int,
    name: String,
    description: String,
    rewards: List<Reward>,
    music: Int,
    background: String,
    var encounter: Int,
    var possibleEncounters: PossibleEncounters,

    ) : Level(id, name, description, rewards, music, background) {
    companion object {
        fun of(wildBattleLevelEntity: WildBattleLevelEntity, gameDataService: GameDataService): WildBattleLevel {
            return WildBattleLevel(
                wildBattleLevelEntity.id,
                wildBattleLevelEntity.name,
                wildBattleLevelEntity.description,
                wildBattleLevelEntity.rewards.map { Reward(it.id, it.quantity) },
                wildBattleLevelEntity.music,
                wildBattleLevelEntity.background,
                wildBattleLevelEntity.encounter,
                PossibleEncounters.of(wildBattleLevelEntity.possibleEncounters, gameDataService))
        }
    }
}