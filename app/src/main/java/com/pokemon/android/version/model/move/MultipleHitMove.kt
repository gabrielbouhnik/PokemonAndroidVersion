package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MultipleHitMoveEntity
import com.pokemon.android.version.model.Type

open class MultipleHitMove(
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
        fun of(multipleHitMoveEntity: MultipleHitMoveEntity): MultipleHitMove {
            return MultipleHitMoveBuilder()
                .id(multipleHitMoveEntity.id)
                .name(multipleHitMoveEntity.name)
                .type(Type.of(multipleHitMoveEntity.type))
                .category(MoveCategory.valueOf(multipleHitMoveEntity.category))
                .power(multipleHitMoveEntity.power)
                .pp(multipleHitMoveEntity.pp)
                .accuracy(multipleHitMoveEntity.accuracy)
                .priorityLevel(multipleHitMoveEntity.priorityLevel)
                .highCritRate(multipleHitMoveEntity.highCritRate)
                .status(multipleHitMoveEntity.status.map(StatusMove::of))
                .description(multipleHitMoveEntity.description)
                .build()
        }
    }

    data class MultipleHitMoveBuilder(
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
        var description: String = "",
        var characteristics: List<MoveCharacteristic> = listOf()
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
        fun characteristics(characteristics: List<MoveCharacteristic>) =
            apply { this.characteristics = characteristics }

        fun build() =
            MultipleHitMove(
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
            )
    }
}