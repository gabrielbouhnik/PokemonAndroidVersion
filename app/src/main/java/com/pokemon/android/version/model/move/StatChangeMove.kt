package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.StatChangeMoveEntity
import com.pokemon.android.version.model.Type

class StatChangeMove(
    id: Int,
    name: String,
    type: Type,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int?,
    priorityLevel: Int,
    var statsAffected: List<Stats>, var target: Target,
    var multiplicator: Float, var probability: Int?
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel) {
    companion object {
        fun of(statusMoveEntity: StatChangeMoveEntity): StatChangeMove {
            return StatChangeMove(
                statusMoveEntity.id,
                statusMoveEntity.name,
                Type.of(statusMoveEntity.type),
                MoveCategory.valueOf(statusMoveEntity.category),
                statusMoveEntity.power,
                statusMoveEntity.pp,
                statusMoveEntity.accuracy,
                statusMoveEntity.priorityLevel,
                statusMoveEntity.statsAffected.map{Stats.valueOf(it)},
                Target.valueOf(statusMoveEntity.target),
                statusMoveEntity.multiplicator,
                statusMoveEntity.probability)
        }
    }
}