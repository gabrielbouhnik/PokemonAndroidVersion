package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MoveBasedOnHPEntity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Type

open class MoveBasedOnHP(
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
    private var strongerWhenHPisLow: Boolean = false,
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
        fun of(moveBasedOnHPEntity: MoveBasedOnHPEntity): MoveBasedOnHP {
            return MoveBasedOnHPBuilder()
                .id(moveBasedOnHPEntity.id)
                .name(moveBasedOnHPEntity.name)
                .type(Type.of(moveBasedOnHPEntity.type))
                .category(MoveCategory.valueOf(moveBasedOnHPEntity.category))
                .power(moveBasedOnHPEntity.power)
                .pp(moveBasedOnHPEntity.pp)
                .accuracy(moveBasedOnHPEntity.accuracy)
                .priorityLevel(moveBasedOnHPEntity.priorityLevel)
                .highCritRate(moveBasedOnHPEntity.highCritRate)
                .status(moveBasedOnHPEntity.status.map(StatusMove::of))
                .description(moveBasedOnHPEntity.description)
                .strongerWhenHPisLow(moveBasedOnHPEntity.strongerWhenHPisLow)
                .characteristics(moveBasedOnHPEntity.characteristics.map { MoveCharacteristic.valueOf(it) })
                .build()
        }
    }

    fun getPower(pokemon: Pokemon): Int {
        val hpLeft: Float = pokemon.currentHP.toFloat() / pokemon.hp.toFloat()
        if (this.strongerWhenHPisLow) {
            if (hpLeft > 0.7f)
                return 20
            if (hpLeft > 0.36f)
                return 40
            if (hpLeft > 0.21f)
                return 80
            if (hpLeft > 0.11f)
                return 100
            if (hpLeft > 0.05f)
                return 150
            return 200
        }
        else
            return hpLeft.toInt() * 150
    }

    data class MoveBasedOnHPBuilder(
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
        var strongerWhenHPisLow: Boolean = false,
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
        fun strongerWhenHPisLow(strongerWhenHPisLow: Boolean) = apply { this.strongerWhenHPisLow = strongerWhenHPisLow }
        fun characteristics(characteristics: List<MoveCharacteristic>) =
            apply { this.characteristics = characteristics }

        fun status(status: List<StatusMove>) = apply { this.status = status }
        fun build() =
            MoveBasedOnHP(
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
                strongerWhenHPisLow,
                characteristics
            )
    }
}