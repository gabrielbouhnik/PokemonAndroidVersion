package com.pokemon.android.version.model.battle

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.utils.IAUtils
import kotlin.random.Random

class BattleFrontierBattle() : Battle() {
    companion object {
        const val BACKGROUND_IMAGE = "images/battle_background/battle_frontier_background.png"

        fun generateTeam(gameDataService: GameDataService): List<Pokemon> {
            val team =
                gameDataService.battleFrontierPokemons.asSequence().shuffled().take(3).toList()
            return team.map { (id, pkmnData) ->
                gameDataService.generatePokemonWithMoves(
                    id,
                    50,
                    pkmnData[Random.nextInt(pkmnData.size)].moves,
                    pkmnData[Random.nextInt(pkmnData.size)].heldItem
                )
            }
        }

        private fun validateTeamTypes(data1: PokemonData, data2: PokemonData, data3: PokemonData): Boolean {
            val types: List<Type> = listOf(data1.type1, data1.type2, data2.type1, data2.type2, data3.type1, data3.type2).filter { it != Type.NONE}
            return types.distinct().size == types.size
        }

        fun generateTrainerTeam(gameDataService: GameDataService): List<Pokemon> {
            var team = generateTeam(gameDataService)
            while (team.none { it.speed > 80 } || !validateTeamTypes(team[0].data,team[1].data,team[2].data))
                team = generateTeam(gameDataService)
            return team
        }
    }

    lateinit var opponentTrainer: OpponentTrainer
    var team: List<Pokemon> = listOf()
    var area: BattleFrontierArea = BattleFrontierArea.BATTLE_FACTORY

    constructor(activity: MainActivity, team: List<Pokemon>, area: BattleFrontierArea) : this() {
        this.activity = activity
        this.team = team
        this.area = area
        this.pokemon = team[0]
        this.pokemon.battleData = PokemonBattleData()
        val opponentTeam = generateTeam(activity.gameDataService)
        for (pokemon in opponentTeam){
            if (Random.nextInt(50) == 10)
                pokemon.shiny = true
        }
        this.opponentTrainer = OpponentTrainer("Elite Trainer", opponentTeam, "images/Sprite_Topdresseuse.png",3)
        opponentTeam.forEach {it.trainer = this.opponentTrainer}
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
    }

    override fun getBattleState(): State {
        if (!team.any { it.currentHP > 0 })
            return State.TRAINER_LOSS
        return if (opponentTrainer.canStillBattle())
            State.IN_PROGRESS
        else
            State.TRAINER_VICTORY
    }

    override fun updateOpponent() {
        val pokemon = if (pokemon.currentHP > 0)
            IAUtils.getBestPokemonToSentAfterKo(pokemon, opponentTrainer.getTrainerTeam())
        else
            opponentTrainer.getFirstPokemonThatCanFight()
        if (pokemon != null)
            opponent = pokemon
    }
}