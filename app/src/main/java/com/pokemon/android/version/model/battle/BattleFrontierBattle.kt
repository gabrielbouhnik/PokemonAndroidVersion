package com.pokemon.android.version.model.battle

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon

class BattleFrontierBattle() : Battle() {
    companion object{
        const val BACKGROUND_IMAGE = "images/building_battle_background.jpg"

        fun generateTeam(gameDataService: GameDataService) : List<Pokemon>{
            val team =
                gameDataService.battleFrontierPokemons.asSequence().shuffled().take(3).toList()
            //TODO deal with duplicates
            return team.map{gameDataService.generatePokemonWithMoves(it.id,it.level,it.moves)}
        }
    }
    lateinit var opponentTrainer: OpponentTrainer

    constructor(activity: MainActivity) : this() {
        this.activity = activity
        this.dialogTextView = activity.findViewById(R.id.dialogTextView)
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
        this.opponentTrainer = OpponentTrainer(generateTeam(activity.gameDataService), "images/Sprite_Topdresseuse.png")
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
    }

    override fun getBattleState(): State {
        if (!activity.trainer!!.canStillBattle())
            return State.TRAINER_LOSS
        return if (opponentTrainer.canStillBattle())
            State.IN_PROGRESS
        else
            State.TRAINER_VICTORY
    }

    override fun updateOpponent() {
        val pokemon = opponentTrainer.getFirstPokemonThatCanFight()
        if (pokemon != null)
            opponent = pokemon
    }
}