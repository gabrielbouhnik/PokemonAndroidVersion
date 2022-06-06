package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.ui.BattleUI
import com.pokemon.android.version.utils.BattleUtils
import java.lang.StringBuilder

class TrainerBattle() : Battle(){
    var trainersLeft: Int = 0
    lateinit var opponentTrainer : OpponentTrainer

    constructor(activity: MainActivity, trainerBattleLevelData: TrainerBattleLevelData) : this() {
        this.activity = activity
        this.dialogTextView = activity.findViewById(R.id.dialogTextView)
        this.levelData = trainerBattleLevelData
        this.trainersLeft = trainerBattleLevelData.opponentTrainerData.size
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.opponentTrainer = OpponentTrainerFactory.createOpponentTrainer(trainerBattleLevelData.opponentTrainerData.first(), activity.gameDataService)
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
    }

    override fun turn(trainerPokemonMove: PokemonMove) {
        val sb = StringBuilder()
        if (BattleUtils.trainerStarts(pokemon, opponent, trainerPokemonMove.move)) {
            sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
            var response = pokemon.attack(trainerPokemonMove, opponent)
            if (!response.success)
                sb.append(response.reason)
            if (opponent.currentHP > 0) {
                sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
                var opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
                if(!opponentResponse.success)
                    sb.append(opponentResponse.reason)
            }
        } else {
            sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
            var opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
            if(!opponentResponse.success)
                sb.append(opponentResponse.reason)
            if (pokemon.currentHP > 0) {
                sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
                var response = pokemon.attack(trainerPokemonMove, opponent)
                if (!response.success)
                    sb.append(response.reason)
            }
        }
        if (opponent.currentHP == 0) {
            if (opponentTrainer.canStillBattle())
                opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
            else
                nextTrainer()
        }
        if (pokemon.currentHP > 0)
            sb.append(checkStatus(pokemon))
        if (opponent.currentHP> 0)
            sb.append(checkStatus(opponent))
        dialogTextView.text = sb.toString()
    }

    override fun getBattleState(): State {
        if (trainersLeft == 0) {
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle()) {
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