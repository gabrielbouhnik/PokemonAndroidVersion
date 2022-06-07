package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.CritMoveEntity
import com.pokemon.android.version.model.Type

class CritMove(
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
        fun of(critMoveEntity: CritMoveEntity): CritMove {
            return CritMoveBuilder()
                .id(critMoveEntity.id)
                .name(critMoveEntity.name)
                .type(Type.of(critMoveEntity.type))
                .category(MoveCategory.valueOf(critMoveEntity.category))
                .power(critMoveEntity.power)
                .pp(critMoveEntity.pp)
                .accuracy(critMoveEntity.accuracy)
                .priorityLevel(critMoveEntity.priorityLevel)
                .build()
        }
    }

    data class CritMoveBuilder(
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
        fun build() = CritMove(id, name, type, category, power, pp, accuracy, priorityLevel)
    }
}