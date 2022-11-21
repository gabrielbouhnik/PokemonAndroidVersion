package com.pokemon.android.version.entity.level

data class TrainerBattleLevelEntity(
    var id: Int,
    var name: String,
    var description: String,
    var opponents: ArrayList<OpponentTrainerEntity>,
    var rewards: ArrayList<RewardEntity>,
    var startDialog: String,
    var endDialogWin: String,
    var endDialogLoose: String,
    var music: Int,
    var icon: String,
    var background: String,
    var exp: Int,
    var mandatory: Boolean
)