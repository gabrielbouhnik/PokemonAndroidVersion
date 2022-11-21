package com.pokemon.android.version.model.battle

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.move.MoveCategory
import kotlin.random.Random

class BattleFrontierBattle() : Battle() {
    companion object {
        const val BACKGROUND_IMAGE = "images/battle_background/battle_frontier_background.png"

        fun generateTeam(gameDataService: GameDataService): List<Pokemon> {
            val team =
                gameDataService.battleFrontierPokemons.asSequence().shuffled().take(3).toList()
            return team.map { (id, moveSets) ->
                gameDataService.generatePokemonWithMoves(
                    id,
                    50,
                    moveSets[Random.nextInt(moveSets.size)]
                )
            }
        }

        private fun haveSameTypes(data1: PokemonData, data2: PokemonData): Boolean {
            val types: List<Type> = listOf(data1.type1, data1.type2, data2.type1, data2.type2)
            return types.toSet().size == 2
        }

        private fun haveATypeInCommon(data1: PokemonData, data2: PokemonData, data3: PokemonData): Boolean {
            val types: List<Type> = listOf(data1.type1, data1.type2, data2.type1, data2.type2, data3.type1, data3.type2)
            return types.toSet().size == 4
        }

        fun generateTrainerTeam(gameDataService: GameDataService): List<Pokemon> {
            var team = generateTeam(gameDataService)
            team.filter { it.speed > 70 }.count()
            while (team.filter { it.speed >= 80 }.count() == 0 || ((haveSameTypes(
                    team[0].data,
                    team[1].data
                ) || haveSameTypes(team[1].data, team[2].data))
                        || haveATypeInCommon(team[0].data, team[1].data, team[2].data))
            ) {
                team = generateTeam(gameDataService)
            }
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
        this.opponentTrainer = OpponentTrainer(generateTeam(activity.gameDataService), "images/Sprite_Topdresseuse.png")
        this.opponent = this.opponentTrainer.getFirstPokemonThatCanFight()!!
    }

    override fun turnWithSwitch(pokemonToBeSent: Pokemon): String {
        val sb = StringBuilder()
        val previousPokemon = pokemon
        sb.append("${activity.trainer!!.name} sends ${pokemonToBeSent.data.name}\n")
        switchPokemon(pokemonToBeSent)
        val move = opponent.ia(previousPokemon)
        sb.append(opponentTurn(move, move.move.category != MoveCategory.OTHER))
        endTurn(sb)
        return sb.toString()
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
        val pokemon = opponentTrainer.getFirstPokemonThatCanFight()
        if (pokemon != null)
            opponent = pokemon
    }
}