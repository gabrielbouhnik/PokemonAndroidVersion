package com.pokemon.android.version.entity.level

class WildBattleLevelEntity(
    var id: Int,
    var name: String,
    var description: String,
    var encounter: Int,
    var possibleEncounters: PossibleEncountersEntity,
    var rewards: ArrayList<RewardEntity>,
    var music: Int,
    var icon: String,
    var background: String,
    var exp: Int,
    var mandatory : Boolean
)