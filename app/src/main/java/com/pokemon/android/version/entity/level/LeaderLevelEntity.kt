package com.pokemon.android.version.entity.level

data class LeaderLevelEntity(
    var id: Int,
    var name: String,
    var description: String,
    var sprite: String,
    var title: String,
    var battle1: ArrayList<PokemonOpponentTrainerEntity>,
    var battle2: ArrayList<PokemonOpponentTrainerEntity>,
    var rewards: ArrayList<RewardEntity>,
    var startDialog1: String,
    var startDialog2: String,
    var endDialogWin1: String,
    var endDialogWin2: String,
    var endDialogLoose: String,
    var music: Int,
    var icon: String,
    var background: String,
    var exp: Int,
    var iaLevel: Int,
    var mandatory: Boolean,
    var megaPokemonId: Int,
    var megaPokemonIdOnRematch: Int
) {
}