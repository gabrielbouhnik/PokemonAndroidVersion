package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.RecoilMoveEntity
import com.pokemon.android.version.model.Type

class RecoilMove(
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
    description: String,
    var recoil: Recoil
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description) {
    companion object {
        fun of(recoilMoveEntity: RecoilMoveEntity): RecoilMove {
            return RecoilMoveBuilder()
                .id(recoilMoveEntity.id)
                .name(recoilMoveEntity.name)
                .type(Type.of(recoilMoveEntity.type))
                .category(MoveCategory.valueOf(recoilMoveEntity.category))
                .power(recoilMoveEntity.power)
                .pp(recoilMoveEntity.pp)
                .accuracy(recoilMoveEntity.accuracy)
                .priorityLevel(recoilMoveEntity.priorityLevel)
                .highCritRate(recoilMoveEntity.highCritRate)
                .status(ArrayList(recoilMoveEntity.status.map(StatusMove::of)))
                .recoil(Recoil.valueOf(recoilMoveEntity.recoil))
                .description(recoilMoveEntity.description)
                .build()
        }
    }

    data class RecoilMoveBuilder(
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
        var description: String = "",
        var recoil: Recoil = Recoil.ALL
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
        fun recoil(recoil: Recoil) = apply { this.recoil = recoil }

        fun build() =
            RecoilMove(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description, recoil)
    }
}