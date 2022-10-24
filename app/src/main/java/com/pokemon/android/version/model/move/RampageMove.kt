package com.pokemon.android.version.model.move


import com.pokemon.android.version.entity.move.MoveEntity
import com.pokemon.android.version.model.Type

class RampageMove(
    id: Int = 0,
    name: String,
    type: Type = Type.NONE,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int,
    priorityLevel: Int = 0,
    status: ArrayList<StatusMove>,
    highCritRate: Boolean = false,
    description: String
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description) {
    companion object {
        fun of(moveEntity: MoveEntity): RampageMove {
            return RampageMoveBuilder()
                .id(moveEntity.id)
                .name(moveEntity.name)
                .type(Type.of(moveEntity.type))
                .category(MoveCategory.valueOf(moveEntity.category))
                .power(moveEntity.power)
                .pp(moveEntity.pp)
                .accuracy(moveEntity.accuracy!!)
                .priorityLevel(moveEntity.priorityLevel)
                .highCritRate(moveEntity.highCritRate)
                .status(ArrayList(moveEntity.status.map(StatusMove::of)))
                .description(moveEntity.description)
                .build()
        }
    }

    data class RampageMoveBuilder(
        var id: Int = 0,
        var name: String = "",
        var power: Int = 0,
        var pp: Int = 0,
        var type: Type = Type.NONE,
        var category: MoveCategory = MoveCategory.PHYSICAL,
        var accuracy: Int = 100,
        var priorityLevel: Int = 0,
        var status: ArrayList<StatusMove> = arrayListOf(),
        var highCritRate: Boolean = false,
        var description: String = ""
    ) {
        fun id(id: Int) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun type(type: Type) = apply { this.type = type }
        fun category(category: MoveCategory) = apply { this.category = category }
        fun power(power: Int) = apply { this.power = power }
        fun pp(pp: Int) = apply { this.pp = pp }
        fun accuracy(accuracy: Int) = apply { this.accuracy = accuracy }
        fun priorityLevel(priorityLevel: Int) = apply { this.priorityLevel = priorityLevel }
        fun highCritRate(highCritRate: Boolean) = apply { this.highCritRate = highCritRate }
        fun description(description: String) = apply { this.description = description }
        fun status(status: ArrayList<StatusMove>) = apply { this.status = status }

        fun build() =
            RampageMove(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description)
    }
}