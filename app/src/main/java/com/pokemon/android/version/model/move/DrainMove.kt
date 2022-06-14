package com.pokemon.android.version.model.move


import com.pokemon.android.version.entity.move.DrainMoveEntity
import com.pokemon.android.version.model.Type

class DrainMove(
    id: Int = 0,
    name: String,
    type: Type = Type.NONE,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int,
    priorityLevel: Int = 0
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel) {
    companion object {
        fun of(DrainMoveEntity: DrainMoveEntity): DrainMove {
            return DrainMoveBuilder()
                .id(DrainMoveEntity.id)
                .name(DrainMoveEntity.name)
                .type(Type.of(DrainMoveEntity.type))
                .category(MoveCategory.valueOf(DrainMoveEntity.category))
                .power(DrainMoveEntity.power)
                .pp(DrainMoveEntity.pp)
                .accuracy(DrainMoveEntity.accuracy)
                .priorityLevel(DrainMoveEntity.priorityLevel)
                .build()
        }
    }

    data class DrainMoveBuilder(
        var id: Int = 0,
        var name: String = "",
        var power: Int = 0,
        var pp: Int = 0,
        var type: Type = Type.NONE,
        var category: MoveCategory = MoveCategory.PHYSICAL,
        var accuracy: Int = 100,
        var priorityLevel: Int = 0
    ) {
        fun id(id: Int) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun type(type: Type) = apply { this.type = type }
        fun category(category: MoveCategory) = apply { this.category = category }
        fun power(power: Int) = apply { this.power = power }
        fun pp(pp: Int) = apply { this.pp = pp }
        fun accuracy(accuracy: Int) = apply { this.accuracy = accuracy }
        fun priorityLevel(priorityLevel: Int) = apply { this.priorityLevel = priorityLevel }
        fun build() = DrainMove(id, name, type, category, power, pp, accuracy, priorityLevel)
    }
}