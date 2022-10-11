package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MoveEntity
import com.pokemon.android.version.model.Type

class UltimateMove(
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
    characteristics : List<MoveCharacteristic> = listOf()
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description, characteristics) {
    companion object {
        fun of(ultimateMoveEntity: MoveEntity): UltimateMove {
            return UltimateMoveBuilder()
                .id(ultimateMoveEntity.id)
                .name(ultimateMoveEntity.name)
                .type(Type.of(ultimateMoveEntity.type))
                .category(MoveCategory.valueOf(ultimateMoveEntity.category))
                .power(ultimateMoveEntity.power)
                .pp(ultimateMoveEntity.pp)
                .accuracy(ultimateMoveEntity.accuracy)
                .priorityLevel(ultimateMoveEntity.priorityLevel)
                .highCritRate(ultimateMoveEntity.highCritRate)
                .status(ArrayList(ultimateMoveEntity.status.map(StatusMove::of)))
                .description(ultimateMoveEntity.description)
                .characteristics(ultimateMoveEntity.characteristics.map{MoveCharacteristic.valueOf(it)})
                .build()
        }
    }

    data class UltimateMoveBuilder(
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
        var characteristics : List<MoveCharacteristic> = listOf()
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
        fun characteristics(characteristics : List<MoveCharacteristic>) = apply { this.characteristics = characteristics }
        fun status(status: ArrayList<StatusMove>) = apply { this.status = status }

        fun build() =
            UltimateMove(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description)
    }
}