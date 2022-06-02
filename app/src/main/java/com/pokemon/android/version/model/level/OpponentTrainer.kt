package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.OpponentTrainerEntity

class OpponentTrainer(var sprite : String, var name : String,
                      var pokemons : List<PokemonOpponentTrainer>) {
    companion object{
        fun of(opponentTrainerEntity : OpponentTrainerEntity, gameDataService: GameDataService) : OpponentTrainer{
            return OpponentTrainer(opponentTrainerEntity.sprite, opponentTrainerEntity.name,
            opponentTrainerEntity.pokemons.map{PokemonOpponentTrainer.of(it, gameDataService)})
        }
    }
}