package com.pokemon.android.version.entity.level

data class BossEntity(
    var id: Int,
    var level: Int,
    var hpMultiplier: Int,
    var moveIds: List<Int>,
    var boostedStats: List<String>,
    var itemHeld: String
)