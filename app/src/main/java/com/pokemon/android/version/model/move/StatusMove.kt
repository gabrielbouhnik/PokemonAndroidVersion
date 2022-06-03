package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.StatusMoveEntity
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type

class StatusMove(
    id: Int,
    name: String,
    type: Type,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int,
    priorityLevel: Int,
    var status: Status,
    var probability: Int
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel) {
    companion object {
        fun of(statusMoveEntity: StatusMoveEntity): StatusMove {
            return StatusMoveBuilder()
                .id(statusMoveEntity.id)
                .name(statusMoveEntity.name)
                .type(Type.of(statusMoveEntity.type))
                .category(MoveCategory.valueOf(statusMoveEntity.category))
                .power(statusMoveEntity.power)
                .pp(statusMoveEntity.pp)
                .accuracy(statusMoveEntity.accuracy)
                .priorityLevel(statusMoveEntity.priorityLevel)
                .status(Status.valueOf(statusMoveEntity.status))
                .probability(statusMoveEntity.probability)
                .build()
        }
    }

    data class StatusMoveBuilder(
        var id: Int = 0,
        var name: String = "",
        var power: Int = 0,
        var pp: Int = 0,
        var type: Type = Type.NONE,
        var category: MoveCategory = MoveCategory.PHYSICAL,
        var accuracy: Int = 100,
        var priorityLevel: Int = 0,
        var status: Status = Status.OK,
        var probability: Int = 0
    ) {
        fun id(id: Int) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun type(type: Type) = apply { this.type = type }
        fun category(category: MoveCategory) = apply { this.category = category }
        fun power(power: Int) = apply { this.power = power }
        fun pp(pp: Int) = apply { this.pp = pp }
        fun accuracy(accuracy: Int) = apply { this.accuracy = accuracy }
        fun priorityLevel(priorityLevel: Int) = apply { this.priorityLevel = priorityLevel }
        fun status(status: Status) = apply { this.status = status }
        fun probability(probability: Int) = apply { this.probability = probability }
        fun build() = StatusMove(id, name, type, category, pp, power, accuracy, priorityLevel, status, probability)
    }
}