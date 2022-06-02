package com.pokemon.android.version.model.level

abstract class Level(
    var id: Int,
    var name: String,
    var description: String,
    var rewards: List<Reward>,
    var music: Int,
    var background: String
) {
}