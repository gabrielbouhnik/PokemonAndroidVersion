package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.OpponentTrainerEntity

class OpponentTrainerData(
    var sprite: String, var name: String,
    var pokemons: List<PokemonOpponentTrainerData>, var iaLevel: Int
) {
    companion object {
        fun of(opponentTrainerEntity: OpponentTrainerEntity, gameDataService: GameDataService, iaLevel: Int): OpponentTrainerData {
            return OpponentTrainerData(opponentTrainerEntity.sprite, opponentTrainerEntity.name,
                opponentTrainerEntity.pokemons.map { PokemonOpponentTrainerData.of(it, gameDataService) }, iaLevel)
        }
    }
}