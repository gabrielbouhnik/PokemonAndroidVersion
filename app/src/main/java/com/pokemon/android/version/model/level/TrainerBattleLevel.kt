package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.TrainerBattleLevelEntity

class TrainerBattleLevel(
    id: Int,
    name: String,
    description: String,
    rewards: List<Reward>,
    music: Int,
    background: String,
    var startDialog    : String,
    var endDialogWin   : String,
    var endDialogLoose : String,
    var opponentTrainers : List<OpponentTrainer>,
    ) : Level(id,name,description,rewards,music,background) {
    companion object{
        fun of(trainerBattleLevelEntity : TrainerBattleLevelEntity, gameDataService : GameDataService) : TrainerBattleLevel {
            return TrainerBattleLevel(
                trainerBattleLevelEntity.id,
                trainerBattleLevelEntity.name,
                trainerBattleLevelEntity.description,
                trainerBattleLevelEntity.rewards.map { Reward(it.id, it.quantity) },
                trainerBattleLevelEntity.music,
                trainerBattleLevelEntity.background,
                trainerBattleLevelEntity.startDialog,
                trainerBattleLevelEntity.endDialogWin,
                trainerBattleLevelEntity.endDialogLoose,
                trainerBattleLevelEntity.opponents.map{OpponentTrainer.of(it, gameDataService)}
            )
        }
    }

}