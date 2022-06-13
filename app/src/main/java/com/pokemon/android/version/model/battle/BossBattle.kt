package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.level.BossBattleLevelData
import com.pokemon.android.version.model.move.Stats
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.BattleUtils
import java.lang.StringBuilder

class BossBattle() : Battle() {

    constructor(activity: MainActivity, bossBattleLevelData: BossBattleLevelData) : this() {
        this.activity = activity
        this.dialogTextView = activity.findViewById(R.id.dialogTextView)
        this.levelData = bossBattleLevelData
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        this.opponent = activity.gameDataService.generatePokemonWithMoves(bossBattleLevelData.boss.id,
            bossBattleLevelData.boss.level,
            bossBattleLevelData.boss.moves)
        this.opponent.hp *= 4
        this.opponent.currentHP *= 4
        Stats.increaseStat(opponent, bossBattleLevelData.boss.boostedStats)
    }

    override fun getBattleState(): State {
        if (opponent.currentHP == 0){
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle()) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    override fun turn(trainerPokemonMove: PokemonMove) {
        var sb = StringBuilder()
        if (BattleUtils.trainerStarts(pokemon, opponent!!, trainerPokemonMove.move)) {
            sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
            var response = pokemon.attack(trainerPokemonMove, opponent!!)
            if (!response.success)
                sb.append(response.reason)
            if (opponent.currentHP > 0) {
                sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
                var opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
                if (!opponentResponse.success)
                    sb.append(opponentResponse.reason)
            }
        } else {
            sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
            var opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
            if (!opponentResponse.success)
                sb.append(opponentResponse.reason)
            if (pokemon.currentHP > 0) {
                sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
                var response = pokemon.attack(trainerPokemonMove, opponent!!)
                if (!response.success)
                    sb.append(response.reason)
            }
        }
        if (pokemon.currentHP > 0)
            sb.append(checkStatus(pokemon))
        else{
            pokemon.status = Status.OK
            pokemon.battleData = null
        }
        if (opponent.currentHP > 0){
            sb.append(checkStatus(opponent!!))
        }
        else{
            sb.append(opponent.data.name + " fainted\n")
        }
        dialogTextView.text = sb.toString()
    }
}