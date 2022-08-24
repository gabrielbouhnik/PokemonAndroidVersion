package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MoveBasedOnLevelEntity
import com.pokemon.android.version.model.Type

open class MoveBasedOnLevel(
    id: Int,
    name: String,
    type: Type,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int?,
    priorityLevel: Int = 0,
    status: List<StatusMove>,
    highCritRate: Boolean = false,
    description: String
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description) {
    companion object {
        fun of(moveBasedOnLevelEntity: MoveBasedOnLevelEntity): MoveBasedOnLevel {
            return MoveBasedOnLevelBuilder()
                .id(moveBasedOnLevelEntity.id)
                .name(moveBasedOnLevelEntity.name)
                .type(Type.of(moveBasedOnLevelEntity.type))
                .category(MoveCategory.valueOf(moveBasedOnLevelEntity.category))
                .power(moveBasedOnLevelEntity.power)
                .pp(moveBasedOnLevelEntity.pp)
                .accuracy(moveBasedOnLevelEntity.accuracy)
                .priorityLevel(moveBasedOnLevelEntity.priorityLevel)
                .highCritRate(moveBasedOnLevelEntity.highCritRate)
                .status(moveBasedOnLevelEntity.status.map(StatusMove::of))
                .description(moveBasedOnLevelEntity.description)
                .build()
        }
    }

    data class MoveBasedOnLevelBuilder(
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
            MoveBasedOnLevel(
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
                description
            )
    }
}