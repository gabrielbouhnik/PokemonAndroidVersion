package com.pokemon.android.version.model

import com.pokemon.android.version.entity.pokemon.EvolutionConditionEntity

class EvolutionCondition(var level: Int?, var itemId: Int?, var dayTime: String?, var moveId: Int?) {
    companion object {
        fun of(evolutionConditionEntity: EvolutionConditionEntity): EvolutionCondition {
            return EvolutionCondition(
                evolutionConditionEntity.level, evolutionConditionEntity.itemId,
                evolutionConditionEntity.dayTime, evolutionConditionEntity.moveId
            )
        }
    }
}
