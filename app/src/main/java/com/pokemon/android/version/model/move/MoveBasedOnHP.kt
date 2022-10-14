package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MoveEntity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
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
    characteristics : List<MoveCharacteristic> = listOf()
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, highCritRate, description, characteristics) {
    companion object {
        fun of(moveEntity: MoveEntity): MoveBasedOnHP {
            return MoveBasedOnHPBuilder()
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
                .characteristics(moveEntity.characteristics.map{MoveCharacteristic.valueOf(it)})
                .build()
        }

        fun getPower(pokemon: Pokemon): Int{
            val hpLeft : Float = pokemon.currentHP.toFloat()/pokemon.hp.toFloat()
            if (hpLeft > 70)
                return 20
            if (hpLeft > 36)
                return 40
            if (hpLeft > 21)
                return 80
            if (hpLeft > 11)
                return 1000
            if (hpLeft > 5)
                return 150
            return 200;
        }
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
                characteristics
            )
    }
}