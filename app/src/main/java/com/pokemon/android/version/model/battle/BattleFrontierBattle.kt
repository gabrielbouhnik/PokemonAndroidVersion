package com.pokemon.android.version.model.battle

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import kotlin.random.Random

class BattleFrontierBattle() : Battle() {
    companion object{
        const val BACKGROUND_IMAGE = "images/building_battle_background.jpg"

        fun generateTeam(gameDataService: GameDataService) : List<Pokemon>{
            val team =
                gameDataService.battleFrontierPokemons.asSequence().shuffled().take(3).toList()
            return team.map{(id, moveSets) -> gameDataService.generatePokemonWithMoves(id,50,moveSets[Random.nextInt(moveSets.size)])}
        }
    }
    lateinit var opponentTrainer: OpponentTrainer
    var team : List<Pokemon> = listOf()
    var area : BattleFrontierArea = BattleFrontierArea.BATTLE_FACTORY

    constructor(activity: MainActivity, team : List<Pokemon>, area : BattleFrontierArea) : this() {
        this.activity = activity
        this.dialogTextView = activity.findViewById(R.id.dialogTextView)
        this.team = team
        this.area = area
        this.pokemon = team[0]
        this.pokemon.battleData = PokemonBattleData()
        this.opponentTrainer = OpponentTrainer(generateTeam(activity.gameDataService), "images/Sprite_Topdresseuse.png")
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
    }

    override fun turnWithSwitch(pokemonToBeSent: Pokemon) {
        val sb = StringBuilder()
        val previousPokemon = pokemon
        sb.append("${activity.trainer!!.name} sends ${pokemonToBeSent.data.name}\n")
        switchPokemon(pokemonToBeSent)
        sb.append(opponentTurn(opponent.ia(previousPokemon)))
        endTurn(sb)
        dialogTextView.text = sb.toString()
    }

    override fun getBattleState(): State {
        if (!team.any { it.currentHP > 0})
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