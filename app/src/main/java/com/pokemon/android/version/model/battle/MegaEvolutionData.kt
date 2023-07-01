package com.pokemon.android.version.model.battle

import com.pokemon.android.version.entity.pokemon.MegaEvolutionDataEntity
import com.pokemon.android.version.model.Type

class MegaEvolutionData(
    var type1: Type, var type2: Type, var attack: Int,
    var defense: Int, var spAtk: Int, var spDef: Int, var speed: Int) {
    companion object {
        fun of(megaEvolutionDataEntity: MegaEvolutionDataEntity?): MegaEvolutionData? {
            if (megaEvolutionDataEntity == null)
                return null
            return MegaEvolutionData(
                Type.of(megaEvolutionDataEntity.type1),
                Type.of(megaEvolutionDataEntity.type2),
                megaEvolutionDataEntity.attack,
                megaEvolutionDataEntity.defense,
                megaEvolutionDataEntity.spAtk,
                megaEvolutionDataEntity.spDef,
                megaEvolutionDataEntity.speed
            )
        }
    }
}