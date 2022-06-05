package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.BattleUtils

class TrainerBattle() : Battle(){
    lateinit var activity: MainActivity
    var trainersLeft: Int = 0
    lateinit var opponentTrainer : OpponentTrainer

    constructor(activity: MainActivity, trainerBattleLevelData: TrainerBattleLevelData) : this() {
        this.activity = activity
        this.levelData = trainerBattleLevelData
        this.trainersLeft = trainerBattleLevelData.opponentTrainerData.size
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.opponentTrainer = OpponentTrainerFactory.createOpponentTrainer(trainerBattleLevelData.opponentTrainerData.first(), activity.gameDataService)
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
    }

    override fun turn(trainerPokemonMove: PokemonMove) {
        var trainerStarts = BattleUtils.trainerStarts(pokemon, opponent, trainerPokemonMove.move)
        if (trainerStarts) {
            pokemon.attack(trainerPokemonMove, opponent)
            if (opponent.currentHP > 0)
                opponent.attack(opponent.IA(pokemon), pokemon)
        } else {
            opponent.IA(pokemon)
            if (pokemon.currentHP > 0)
                pokemon.attack(trainerPokemonMove, opponent)
        }
        if (opponent.currentHP == 0) {
            if (opponentTrainer.canStillBattle())
                opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
            else
                nextTrainer()
        }
    }

    override fun getBattleState(): State {
        if (trainersLeft == 0) {
            this.pokemon.resetStatChanges()
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle()) {
            this.pokemon.resetStatChanges()
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    fun nextTrainer(){
        if (trainersLeft > 0){
            trainersLeft --
            opponentTrainer = OpponentTrainerFactory.createOpponentTrainer((levelData as TrainerBattleLevelData).opponentTrainerData.reversed()[trainersLeft], activity.gameDataService)
            opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
        }
    }
}