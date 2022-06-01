package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.MoveEntity
import com.pokemon.android.version.model.Type

open class Move(var id : Int = 0,
                var name : String = "",
                var type : Type = Type.NONE,
                var power : Int = 0,
                var pp : Int = 0,
                var accuracy : Int? = 100,
                var priorityLevel : Int = 0) {
    companion object {
        fun of(moveEntity: MoveEntity) :Move{
            return MoveBuilder()
                .id(moveEntity.id)
                .name(moveEntity.name)
                .type(Type.of(moveEntity.type))
                .power(moveEntity.power)
                .pp(moveEntity.pp)
                .accuracy(moveEntity.accuracy)
                .priorityLevel(moveEntity.priorityLevel)
                .build()
        }
    }

    data class MoveBuilder(var id : Int = 0,
                           var name : String = "",
                           var power : Int = 0,
                           var pp : Int = 0,
                           var type : Type = Type.NONE,
                           var accuracy : Int = 100,
                           var priorityLevel : Int = 0){
        fun id(id : Int) = apply { this.id = id }
        fun name(name : String) = apply { this.name = name }
        fun type(type: Type) = apply { this.type = type }
        fun power(power : Int) = apply { this.power = power }
        fun pp(pp : Int) = apply { this.pp = pp }
        fun accuracy(accuracy : Int) = apply { this.accuracy = accuracy }
        fun priorityLevel(priorityLevel : Int) = apply { this.priorityLevel = priorityLevel }
        fun build() = Move(id,name,type, pp, power, accuracy, priorityLevel)
    }
}