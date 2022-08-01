package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MoveEntity
import com.pokemon.android.version.model.Type

open class Move(
    var id: Int,
    var name: String,
    var type: Type,
    var category: MoveCategory,
    var power: Int,
    var pp: Int,
    var accuracy: Int?,
    var priorityLevel: Int = 0,
    var status: List<StatusMove>,
    var highCritRate: Boolean = false,
    var description: String
) {
    companion object {
        fun of(moveEntity: MoveEntity): Move {
            return MoveBuilder()
                .id(moveEntity.id)
                .name(moveEntity.name)
                .type(Type.of(moveEntity.type))
                .category(MoveCategory.valueOf(moveEntity.category))
                .power(moveEntity.power)
                .pp(moveEntity.pp)
                .accuracy(moveEntity.accuracy)
                .priorityLevel(moveEntity.priorityLevel)
                .highCritRate(moveEntity.highCritRate)
                .status(moveEntity.status.map(StatusMove::of))
                .description(moveEntity.description)
                .build()
        }
    }

    data class MoveBuilder(
        var id: Int = 0,
        var name: String = "",
        var power: Int = 0,
        var pp: Int = 0,
        var type: Type = Type.NONE,
        var category: MoveCategory = MoveCategory.PHYSICAL,
        var accuracy: Int? = null,
        var priorityLevel: Int = 0,
        var status: List<StatusMove> = arrayListOf(),
        var highCritRate: Boolean = false,
        var description: String = ""
    ) {
        fun id(id: Int) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun type(type: Type) = apply { this.type = type }
        fun category(category: MoveCategory) = apply { this.category = category }
        fun power(power: Int) = apply { this.power = power }
        fun pp(pp: Int) = apply { this.pp = pp }
        fun accuracy(accuracy: Int?) = apply { this.accuracy = accuracy }
        fun priorityLevel(priorityLevel: Int) = apply { this.priorityLevel = priorityLevel }
        fun highCritRate(highCritRate: Boolean) = apply { this.highCritRate = highCritRate }
        fun description(description: String) = apply { this.description = description }
        fun status(status: List<StatusMove>) = apply { this.status = status }
        fun build() =
            Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description)
    }
}