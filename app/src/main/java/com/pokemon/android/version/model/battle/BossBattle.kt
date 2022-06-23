package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.level.BossBattleLevelData
import com.pokemon.android.version.model.move.Stats
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.BattleUtils
import com.pokemon.android.version.utils.MoveUtils
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
        if (!activity.trainer!!.canStillBattle() || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    override fun updateOpponent(){

    }
}