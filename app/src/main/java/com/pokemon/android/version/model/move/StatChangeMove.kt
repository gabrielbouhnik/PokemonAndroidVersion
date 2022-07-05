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
    var multiplicator: Float, var probability: Int?,
    status: List<StatusMove>,
    highCritRate: Boolean = false,
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate) {
    companion object {
        fun of(StatChangeMoveEntity: StatChangeMoveEntity): StatChangeMove {
            return StatChangeMove(
                StatChangeMoveEntity.id,
                StatChangeMoveEntity.name,
                Type.of(StatChangeMoveEntity.type),
                MoveCategory.valueOf(StatChangeMoveEntity.category),
                StatChangeMoveEntity.power,
                StatChangeMoveEntity.pp,
                StatChangeMoveEntity.accuracy,
                StatChangeMoveEntity.priorityLevel,
                StatChangeMoveEntity.statsAffected.map { Stats.valueOf(it) },
                Target.valueOf(StatChangeMoveEntity.target),
                StatChangeMoveEntity.multiplicator,
                StatChangeMoveEntity.probability,
                StatChangeMoveEntity.status.map(StatusMove::of),
                StatChangeMoveEntity.highCritRate
            )
        }
    }
}