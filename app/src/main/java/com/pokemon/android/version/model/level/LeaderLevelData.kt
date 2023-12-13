package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.LeaderLevelEntity

class LeaderLevelData(
    id: Int,
    name: String,
    description: String,
    var sprite: String,
    var title: String,
    rewards: ArrayList<Reward>,
    music: Int,
    icon: String,
    background: String,
    exp: Int,
    mandatory: Boolean,
    var startDialog1: String,
    var startDialog2: String,
    var endDialogWin1: String,
    var endDialogWin2: String,
    var endDialogLoose: String,
    var battle1: List<PokemonOpponentTrainerData>,
    var battle2: List<PokemonOpponentTrainerData>,
    var iaLevel: Int
) : LevelData(id, name, description, rewards, music, icon, background, exp, mandatory) {
    companion object {
        fun of(
            gymLeaderBattleLevelEntity: LeaderLevelEntity,
            gameDataService: GameDataService
        ): LeaderLevelData {
            return LeaderLevelData(
                gymLeaderBattleLevelEntity.id,
                gymLeaderBattleLevelEntity.name,
                gymLeaderBattleLevelEntity.description,
                gymLeaderBattleLevelEntity.sprite,
                gymLeaderBattleLevelEntity.title,
                gymLeaderBattleLevelEntity.rewards.map { Reward(it.id, it.quantity) } as ArrayList<Reward>,
                gymLeaderBattleLevelEntity.music,
                gymLeaderBattleLevelEntity.icon,
                gymLeaderBattleLevelEntity.background,
                gymLeaderBattleLevelEntity.exp,
                gymLeaderBattleLevelEntity.mandatory,
                gymLeaderBattleLevelEntity.startDialog1,
                gymLeaderBattleLevelEntity.startDialog2,
                gymLeaderBattleLevelEntity.endDialogWin1,
                gymLeaderBattleLevelEntity.endDialogWin2,
                gymLeaderBattleLevelEntity.endDialogLoose,
                gymLeaderBattleLevelEntity.battle1.map { PokemonOpponentTrainerData.of(it, gameDataService) },
                gymLeaderBattleLevelEntity.battle2.map { PokemonOpponentTrainerData.of(it, gameDataService) },
                gymLeaderBattleLevelEntity.iaLevel
            )
        }
    }

}