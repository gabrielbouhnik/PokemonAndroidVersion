package com.pokemon.android.version.model.battle

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.utils.IAUtils
import com.pokemon.android.version.utils.MoveUtils
import java.io.InputStream

class TrainerBattle() : Battle() {
    private var numberOfTrainers: Int = 0
    private var trainersIdx: Int = 0
    private lateinit var opponentTrainer: OpponentTrainer
    private lateinit var opponentTrainerSprite: ImageView

    constructor(activity: MainActivity, trainerBattleLevelData: TrainerBattleLevelData) : this() {
        this.activity = activity
        this.opponentTrainerSprite = activity.findViewById(R.id.opponentTrainerSpriteView)
        this.levelData = trainerBattleLevelData
        this.numberOfTrainers = trainerBattleLevelData.opponentTrainerData.size
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        this.opponentTrainer = OpponentTrainerFactory.createOpponentTrainer(
            trainerBattleLevelData.opponentTrainerData.first(),
            activity.gameDataService
        )
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun updateOpponent() {
        opponent.recomputeStat()
        if (opponentTrainer.canStillBattle()) {
            opponent = if (opponentTrainer.iaLevel != 1 && pokemon.currentHP > 0)
                IAUtils.getBestPokemonToSentAfterKo(pokemon, opponentTrainer.getTrainerTeam())!!
            else
                opponentTrainer.getFirstPokemonThatCanFight()!!
        }
        else
            nextTrainer()
        activity.trainer!!.updatePokedex(opponent)
    }

    override fun getBattleState(): State {
        if (numberOfTrainers == trainersIdx) {
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle() || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    private fun nextTrainer() {
        trainersIdx++
        if (numberOfTrainers > trainersIdx) {
            opponentTrainer = OpponentTrainerFactory.createOpponentTrainer(
                (levelData as TrainerBattleLevelData).opponentTrainerData[trainersIdx],
                activity.gameDataService
            )
            opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
            val img: InputStream = activity.assets.open(opponentTrainer.sprite)
            opponentTrainerSprite.setImageDrawable(Drawable.createFromStream(img, opponentTrainer.sprite))
        }
    }
}