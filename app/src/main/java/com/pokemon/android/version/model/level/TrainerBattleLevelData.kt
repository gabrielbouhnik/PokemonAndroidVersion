package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.TrainerBattleLevelEntity

class TrainerBattleLevelData(
    id: Int,
    name: String,
    description: String,
    rewards: ArrayList<Reward>,
    music: Int,
    background: String,
    var startDialog    : String,
    var endDialogWin   : String,
    var endDialogLoose : String,
    var opponentTrainerData : List<OpponentTrainerData>,
    ) : LevelData(id,name,description,rewards,music,background) {
    companion object{
        fun of(trainerBattleLevelEntity : TrainerBattleLevelEntity, gameDataService : GameDataService) : TrainerBattleLevelData {
            return TrainerBattleLevelData(
                trainerBattleLevelEntity.id,
                trainerBattleLevelEntity.name,
                trainerBattleLevelEntity.description,
                trainerBattleLevelEntity.rewards.map { Reward(it.id, it.quantity) } as ArrayList<Reward>,
                trainerBattleLevelEntity.music,
                trainerBattleLevelEntity.background,
                trainerBattleLevelEntity.startDialog,
                trainerBattleLevelEntity.endDialogWin,
                trainerBattleLevelEntity.endDialogLoose,
                trainerBattleLevelEntity.opponents.map{OpponentTrainerData.of(it, gameDataService)}
            )
        }
    }

}