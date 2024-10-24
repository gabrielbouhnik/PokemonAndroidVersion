package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.level.BossBattleLevelData
import com.pokemon.android.version.model.move.Stats
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.MoveUtils

class BossBattle() : Battle() {

    constructor(activity: MainActivity, bossBattleLevelData: BossBattleLevelData) : this() {
        this.activity = activity
        this.levelData = bossBattleLevelData
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        this.opponent = activity.gameDataService.generateBoss(
            bossBattleLevelData.boss.id,
            if ((bossBattleLevelData.boss.id == 144 || bossBattleLevelData.boss.id == 146) && activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID) 90 else bossBattleLevelData.boss.level,
            bossBattleLevelData.boss.moves
        )
        if (bossBattleLevelData.boss.id == 150){
            this.opponent.megaEvolve()
        }
        this.opponent.hp *= 3
        this.opponent.currentHP *= 3
        Stats.increaseBossStats(opponent, bossBattleLevelData.boss.boostedStats)
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun getBattleState(): State {
        if (opponent.currentHP == 0) {
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle() || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    override fun updateOpponent() {

    }
}