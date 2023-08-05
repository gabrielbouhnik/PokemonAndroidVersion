package com.pokemon.android.version.model.level

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.level.PokemonOpponentTrainerEntity
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.Move

open class PokemonOpponentTrainerData(var id: Int, var level: Int, var moves: List<Move>, var holdItem: HoldItem?) {
    companion object {
        fun of(
            pokemonOpponentTrainerEntity: PokemonOpponentTrainerEntity,
            gameDataService: GameDataService
        ): PokemonOpponentTrainerData {
            return PokemonOpponentTrainerData(
                pokemonOpponentTrainerEntity.id,
                pokemonOpponentTrainerEntity.level,
                pokemonOpponentTrainerEntity.moveIds.map { gameDataService.getMoveById(it) },
                if (pokemonOpponentTrainerEntity.itemHeld == null || pokemonOpponentTrainerEntity.itemHeld == "") null else HoldItem.valueOf(pokemonOpponentTrainerEntity.itemHeld)
            )
        }
    }
}