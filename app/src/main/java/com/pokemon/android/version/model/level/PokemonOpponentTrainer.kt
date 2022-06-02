package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.PokemonOpponentTrainerEntity
import com.pokemon.android.version.model.move.Move

class PokemonOpponentTrainer(var id: Int, var level: Int, var moves: List<Move>) {
    companion object{
        fun of(pokemonOpponentTrainerEntity : PokemonOpponentTrainerEntity, gameDataService: GameDataService) : PokemonOpponentTrainer{
            return PokemonOpponentTrainer(pokemonOpponentTrainerEntity.id, pokemonOpponentTrainerEntity.level,pokemonOpponentTrainerEntity.moveIds.map{gameDataService.getMoveById(it)})
        }
    }
}