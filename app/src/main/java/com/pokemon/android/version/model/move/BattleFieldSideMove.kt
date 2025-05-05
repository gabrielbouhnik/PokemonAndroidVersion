package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.BattleFieldSideMoveEntity
import com.pokemon.android.version.model.Type

class BattleFieldSideMove(
    id: Int,
    name: String,
    type: Type,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int?,
    priorityLevel: Int,
    description: String,
    var target: Target
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, listOf(), false, description) {
    companion object {
        fun of(battleFieldSideMoveEntity: BattleFieldSideMoveEntity): BattleFieldSideMove {
            return BattleFieldSideMove(
                battleFieldSideMoveEntity.id,
                battleFieldSideMoveEntity.name,
                Type.of(battleFieldSideMoveEntity.type),
                MoveCategory.valueOf(battleFieldSideMoveEntity.category),
                battleFieldSideMoveEntity.power,
                battleFieldSideMoveEntity.pp,
                battleFieldSideMoveEntity.accuracy,
                battleFieldSideMoveEntity.priorityLevel,
                battleFieldSideMoveEntity.description,
                Target.valueOf(battleFieldSideMoveEntity.target)
            )
        }
    }
}