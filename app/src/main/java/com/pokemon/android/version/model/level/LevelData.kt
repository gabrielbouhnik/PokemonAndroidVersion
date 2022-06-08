package com.pokemon.android.version.model.level

abstract class LevelData(
    var id: Int,
    var name: String,
    var description: String,
    var rewards: ArrayList<Reward>,
    var music: Int,
    var icon: String,
    var background: String,
    var exp: Int
) {
}