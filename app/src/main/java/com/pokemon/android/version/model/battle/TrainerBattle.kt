package com.pokemon.android.version.model.battle

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.level.TrainerBattleLevelData
import com.pokemon.android.version.utils.MoveUtils
import java.io.InputStream

class TrainerBattle() : Battle(){
    var numberOfTrainers: Int = 0
    var trainersIdx : Int = 0
    lateinit var opponentTrainer : OpponentTrainer
    lateinit var opponentTrainerSprite : ImageView

    constructor(activity: MainActivity, trainerBattleLevelData: TrainerBattleLevelData) : this() {
        this.activity = activity
        this.dialogTextView = activity.findViewById(R.id.dialogTextView)
        this.opponentTrainerSprite = activity.findViewById(R.id.opponentTrainerSpriteView)
        this.levelData = trainerBattleLevelData
        this.numberOfTrainers = trainerBattleLevelData.opponentTrainerData.size
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        this.opponentTrainer = OpponentTrainerFactory.createOpponentTrainer(trainerBattleLevelData.opponentTrainerData.first(), activity.gameDataService)
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
    }

    override fun updateOpponent(){
        if (opponentTrainer.canStillBattle())
            opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
        else
            nextTrainer()
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

    fun nextTrainer(){
        trainersIdx ++
        if (numberOfTrainers > trainersIdx){
            opponentTrainer = OpponentTrainerFactory.createOpponentTrainer((levelData as TrainerBattleLevelData).opponentTrainerData[trainersIdx], activity.gameDataService)
            opponent = opponentTrainer.getFirstPokemonThatCanFight()!!
            val img : InputStream = activity.assets.open(opponentTrainer.sprite)
            opponentTrainerSprite.setImageDrawable(Drawable.createFromStream(img,opponentTrainer.sprite))
        }
    }
}