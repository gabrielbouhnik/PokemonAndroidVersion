package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.PossibleEncountersEntity
import com.pokemon.android.version.model.PokemonData

class PossibleEncounters(
    var minLevel: Int,
    var maxLevel: Int,
    var encounters: List<PokemonData>
) {
    companion object {
        fun of(
            possibleEncountersEntity: PossibleEncountersEntity,
            gameDataService: GameDataService
        ): PossibleEncounters {
            return PossibleEncounters(
                possibleEncountersEntity.minLevel,
                possibleEncountersEntity.maxLevel,
                possibleEncountersEntity.encounters.map { gameDataService.getPokemonDataById(it) })
        }
    }

}
