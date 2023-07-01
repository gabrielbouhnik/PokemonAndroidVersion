package com.pokemon.android.version.entity.level

data class LevelsEntity(
    var randomWildEncounters: ArrayList<WildBattleLevelEntity>,
    var trainerBattles: ArrayList<TrainerBattleLevelEntity>,
    var bossBattles: ArrayList<BossBattleLevelEntity>,
    var leaderBattles: ArrayList<LeaderLevelEntity>
)