package com.pokemon.android.version.entity.level

data class BossBattleLevelEntity(
    var id: Int,
    var name: String,
    var description: String,
    var boss: BossEntity,
    var rewards: ArrayList<RewardEntity>,
    var music: Int,
    var icon: String,
    var background: String,
    var exp: Int
) {
}