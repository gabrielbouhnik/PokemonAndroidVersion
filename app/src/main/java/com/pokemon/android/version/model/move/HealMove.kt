package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.HealMoveEntity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type

class HealMove(
    id: Int,
    name: String,
    type: Type,
    category: MoveCategory,
    power: Int,
    pp: Int,
    accuracy: Int?,
    priorityLevel: Int,
    description: String,
    status: List<StatusMove>
) : Move(id, name, type, category, power, pp, accuracy, priorityLevel, status, false, description) {
    companion object {
        fun of(healMoveEntity: HealMoveEntity): HealMove {
            return HealMove(
                healMoveEntity.id,
                healMoveEntity.name,
                Type.of(healMoveEntity.type),
                MoveCategory.valueOf(healMoveEntity.category),
                healMoveEntity.power,
                healMoveEntity.pp,
                healMoveEntity.accuracy,
                healMoveEntity.priorityLevel,
                healMoveEntity.description,
                healMoveEntity.status.map{StatusMove.of(it)}
            )
        }

        fun heal(pokemon: Pokemon) {
            val hpToBeRestored = pokemon.hp / 2
            if (hpToBeRestored + pokemon.currentHP > pokemon.hp)
                pokemon.currentHP = pokemon.hp
            else
                pokemon.currentHP += hpToBeRestored
        }
    }
}