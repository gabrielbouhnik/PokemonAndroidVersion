package com.pokemon.android.version.entity.move

class StatChangeMoveEntity(
    var id: Int, var name: String, var power: Int, var pp: Int,
    var type: Int, var category: String, var accuracy: Int?, var description: String,
    var priorityLevel: Int, var statsAffected: ArrayList<StatChangeEntity>, var target: String,
    var probability: Int?, var highCritRate: Boolean, var status: ArrayList<StatusEntity>, var characteristics: List<String>
)