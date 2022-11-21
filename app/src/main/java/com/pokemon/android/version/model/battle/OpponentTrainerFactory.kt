package com.pokemon.android.version.model.battle

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.model.level.OpponentTrainerData

class OpponentTrainerFactory {
    companion object {
        fun createOpponentTrainer(
            opponentTrainerData: OpponentTrainerData,
            gameDataService: GameDataService
        ): OpponentTrainer {
            val opponentTrainer = OpponentTrainer(opponentTrainerData.pokemons.map {
                gameDataService.generatePokemonWithMoves(
                    it.id,
                    it.level,
                    it.moves
                )
            }, opponentTrainerData.sprite)
            opponentTrainer.team.forEach { it.trainer = opponentTrainer }
            return opponentTrainer
        }
    }
}