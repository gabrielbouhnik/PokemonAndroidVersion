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
    priorityLevel: Int = 0,
    status: ArrayList<StatusMove>,
    highCritRate: Boolean = false,
    description: String
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description) {
    companion object {
        fun of(drainMoveEntity: DrainMoveEntity): DrainMove {
            return DrainMoveBuilder()
                .id(drainMoveEntity.id)
                .name(drainMoveEntity.name)
                .type(Type.of(drainMoveEntity.type))
                .category(MoveCategory.valueOf(drainMoveEntity.category))
                .power(drainMoveEntity.power)
                .pp(drainMoveEntity.pp)
                .accuracy(drainMoveEntity.accuracy)
                .priorityLevel(drainMoveEntity.priorityLevel)
                .highCritRate(drainMoveEntity.highCritRate)
                .status(ArrayList(drainMoveEntity.status.map(StatusMove::of)))
                .description(drainMoveEntity.description)
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

        fun build() = DrainMove(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description)
    }
}