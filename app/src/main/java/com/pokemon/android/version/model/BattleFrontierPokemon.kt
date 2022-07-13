package com.pokemon.android.version.model

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.entity.battlefrontier.BattleFrontierPokemonEntity
import com.pokemon.android.version.model.level.PokemonOpponentTrainerData
import com.pokemon.android.version.model.move.Move

class BattleFrontierPokemon(id: Int, moves: List<Move>) : PokemonOpponentTrainerData(id, 50, moves) {
    companion object {
        fun of(
            battleFrontierPokemonEntity: BattleFrontierPokemonEntity,
            gameDataService: GameDataService
        ): BattleFrontierPokemon {
            return BattleFrontierPokemon(
                battleFrontierPokemonEntity.id,
                battleFrontierPokemonEntity.moveIds.map { gameDataService.getMoveById(it) })
        }
    }
}