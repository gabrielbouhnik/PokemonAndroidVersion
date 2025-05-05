package com.pokemon.android.version.entity.move

class BattleFieldSideMoveEntity(
    var id: Int, var name: String, var power: Int, var pp: Int,
    var type: Int, var category: String, var accuracy: Int?, var description: String,
    var priorityLevel: Int, var target: String, var characteristics: List<String>
)