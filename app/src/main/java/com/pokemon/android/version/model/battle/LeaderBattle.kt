package com.pokemon.android.version.model.battle

import android.widget.ImageView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.LeaderLevelData
import com.pokemon.android.version.ui.BattleFrontierMenu
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.MoveUtils

class LeaderBattle() : Battle() {
    private lateinit var opponentTrainer: OpponentTrainer
    private lateinit var opponentTrainerSprite: ImageView

    constructor(activity: MainActivity, leaderLevelData: LeaderLevelData) : this() {
        this.activity = activity
        this.opponentTrainerSprite = activity.findViewById(R.id.opponentTrainerSpriteView)
        this.levelData = leaderLevelData
        this.pokemon = if (leaderLevelData.id == BattleFrontierMenu.FRONTIER_BRAIN_LEVEL_ID)
            activity.trainer!!.battleTowerProgression!!.team[0]
        else activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        val team = if (leaderLevelData.id == 99) {
            if (activity.trainer!!.battleTowerProgression!!.progression == 7)
                leaderLevelData.battle1
            else
                leaderLevelData.battle2
        } else {
            if (activity.trainer!!.progression < LevelMenu.ELITE_4_LAST_LEVEL_ID)
                leaderLevelData.battle1
            else
                leaderLevelData.battle2
        }
        this.opponentTrainer = OpponentTrainer(
            team.map {
                activity.gameDataService.generatePokemonWithMoves(
                    it.id, it.level,
                    it.moves
                )
            }, leaderLevelData.sprite)
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun updateOpponent() {
        if (opponentTrainer.canStillBattle())
            opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun getBattleState(): State {
        if (!opponentTrainer.canStillBattle()) {
            return State.TRAINER_VICTORY
        }
        if (levelData.id == 99) {
            return if (!activity.trainer!!.battleTowerProgression!!.team.any { it.currentHP > 0 })
                State.TRAINER_LOSS
            else State.IN_PROGRESS
        }
        if (!activity.trainer!!.canStillBattle() || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }
}