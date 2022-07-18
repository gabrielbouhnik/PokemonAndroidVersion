package com.pokemon.android.version.entity.level

data class LevelsEntity(
    var trainerBattles: ArrayList<TrainerBattleLevelEntity>,
    var randomWildEncounters: ArrayList<WildBattleLevelEntity>,
    var bossBattles: ArrayList<BossBattleLevelEntity>,
    var leaderBattles: ArrayList<LeaderLevelEntity>
)