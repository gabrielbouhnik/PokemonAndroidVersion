package com.pokemon.android.version.model.level

abstract class LevelData(
    var id: Int,
    var name: String,
    var description: String,
    var rewards: ArrayList<Reward>,
    var music: Int,
    var background: String
) {
}