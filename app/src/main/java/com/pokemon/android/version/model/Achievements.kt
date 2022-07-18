package com.pokemon.android.version.model

class Achievements(
    var leadersDefeatedAfterTheLeague: ArrayList<Int> = arrayListOf(),
    var leagueDefeatedSecondTime: Boolean = false,
    var dugtrioAchievement: Boolean = false,
    var articunoAchievement: Boolean = false,
    var moltresAchievement: Boolean = false,
    var zapdosAchievement: Boolean = false,
    var winstreak8Achievement: Boolean = false,
    var winstreak15Achievement: Boolean = false,
    var winstreak25Factory: Boolean = false,
    var leagueWithTeamOfFourAchievement: Boolean = false,
    var mewtwoAchievement: Boolean = false,
    //TODO add data for rewards
) {
}