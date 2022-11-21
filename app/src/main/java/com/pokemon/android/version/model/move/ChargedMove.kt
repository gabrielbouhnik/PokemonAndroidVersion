package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.ChargedMoveEntity
import com.pokemon.android.version.model.Type

class ChargedMove(
    id: Int = 0,
    name: String,
    type: Type = Type.NONE,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int?,
    priorityLevel: Int = 0,
    status: ArrayList<StatusMove>,
    highCritRate: Boolean = false,
    description: String,
    characteristics: List<MoveCharacteristic> = listOf(),
    val chargeText: String
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
        fun of(chargedMoveEntity: ChargedMoveEntity): ChargedMove {
            return ChargedMoveBuilder()
                .id(chargedMoveEntity.id)
                .name(chargedMoveEntity.name)
                .type(Type.of(chargedMoveEntity.type))
                .category(MoveCategory.valueOf(chargedMoveEntity.category))
                .power(chargedMoveEntity.power)
                .pp(chargedMoveEntity.pp)
                .accuracy(chargedMoveEntity.accuracy)
                .priorityLevel(chargedMoveEntity.priorityLevel)
                .highCritRate(chargedMoveEntity.highCritRate)
                .status(ArrayList(chargedMoveEntity.status.map(StatusMove::of)))
                .description(chargedMoveEntity.description)
                .chargeText(chargedMoveEntity.chargeText)
                .characteristics(chargedMoveEntity.characteristics.map { MoveCharacteristic.valueOf(it) })
                .build()
        }
    }

    data class ChargedMoveBuilder(
        var id: Int = 0,
        var name: String = "",
        var power: Int = 0,
        var pp: Int = 0,
        var type: Type = Type.NONE,
        var category: MoveCategory = MoveCategory.PHYSICAL,
        var accuracy: Int? = 100,
        var priorityLevel: Int = 0,
        var status: ArrayList<StatusMove> = arrayListOf(),
        var highCritRate: Boolean = false,
        var description: String = "",
        var characteristics: List<MoveCharacteristic> = listOf(),
        var chargeText: String = "",
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
        fun characteristics(characteristics: List<MoveCharacteristic>) =
            apply { this.characteristics = characteristics }

        fun status(status: ArrayList<StatusMove>) = apply { this.status = status }
        fun chargeText(chargeText: String) = apply { this.chargeText = chargeText }

        fun build() =
            ChargedMove(
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
                characteristics,
                chargeText
            )
    }
}