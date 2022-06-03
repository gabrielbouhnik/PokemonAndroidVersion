package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.VariableHitMoveEntity
import com.pokemon.android.version.model.Type

class VariableHitMove(id : Int,
                      name : String,
                      type : Type,
                      category: MoveCategory,
                      power : Int,
                      pp : Int,
                      accuracy : Int,
                      priorityLevel : Int) : Move(id,name,type,category, power,pp,accuracy,priorityLevel) {

    companion object {
        fun of(variableHitMoveEntity: VariableHitMoveEntity) : VariableHitMove{
            return VariableHitMoveBuilder()
                .id(variableHitMoveEntity.id)
                .name(variableHitMoveEntity.name)
                .type(Type.of(variableHitMoveEntity.type))
                .category(MoveCategory.valueOf(variableHitMoveEntity.category))
                .power(variableHitMoveEntity.power)
                .pp(variableHitMoveEntity.pp)
                .accuracy(variableHitMoveEntity.accuracy)
                .priorityLevel(variableHitMoveEntity.priorityLevel)
                .build()
        }
    }

    data class VariableHitMoveBuilder(var id : Int = 0,
                               var name : String = "",
                               var power : Int = 0,
                               var pp : Int = 0,
                               var type : Type = Type.NONE,
                                      var category: MoveCategory = MoveCategory.PHYSICAL,
                               var accuracy : Int = 100,
                               var priorityLevel : Int = 0){
        fun id(id : Int) = apply { this.id = id }
        fun name(name : String) = apply { this.name = name }
        fun type(type: Type) = apply { this.type = type }
        fun category(category: MoveCategory) = apply { this.category = category }
        fun power(power : Int) = apply { this.power = power }
        fun pp(pp : Int) = apply { this.pp = pp }
        fun accuracy(accuracy : Int) = apply { this.accuracy = accuracy }
        fun priorityLevel(priorityLevel : Int) = apply { this.priorityLevel = priorityLevel }
        fun build() = VariableHitMove(id,name,type, category, pp, power, accuracy, priorityLevel)
    }
}