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
    description: String,
    characteristics: List<MoveCharacteristic> = listOf()
) : Move(
    id,
    name,
    type,
    category,
    power,
    pp,
    accuracy,
    priorityLevel,
    status,
    highCritRate,
    description,
    characteristics
) {
    companion object {
        fun of(statChangeMoveEntity: StatChangeMoveEntity): StatChangeMove {
            return StatChangeMove(
                statChangeMoveEntity.id,
                statChangeMoveEntity.name,
                Type.of(statChangeMoveEntity.type),
                MoveCategory.valueOf(statChangeMoveEntity.category),
                statChangeMoveEntity.power,
                statChangeMoveEntity.pp,
                statChangeMoveEntity.accuracy,
                statChangeMoveEntity.priorityLevel,
                statChangeMoveEntity.statsAffected.map { Stats.valueOf(it) },
                Target.valueOf(statChangeMoveEntity.target),
                statChangeMoveEntity.multiplicator,
                statChangeMoveEntity.probability,
                statChangeMoveEntity.status.map(StatusMove::of),
                statChangeMoveEntity.highCritRate,
                statChangeMoveEntity.description,
                statChangeMoveEntity.characteristics.map { MoveCharacteristic.valueOf(it) }
            )
        }
    }
}