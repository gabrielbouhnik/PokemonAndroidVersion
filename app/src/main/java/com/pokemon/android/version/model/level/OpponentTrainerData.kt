package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.OpponentTrainerEntity

class OpponentTrainerData(var sprite : String, var name : String,
                          var pokemons : List<PokemonOpponentTrainerData>) {
    companion object{
        fun of(opponentTrainerEntity : OpponentTrainerEntity, gameDataService: GameDataService) : OpponentTrainerData{
            return OpponentTrainerData(opponentTrainerEntity.sprite, opponentTrainerEntity.name,
            opponentTrainerEntity.pokemons.map{PokemonOpponentTrainerData.of(it, gameDataService)})
        }
    }
}