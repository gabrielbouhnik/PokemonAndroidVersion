package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.level.WildBattleLevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.utils.BattleUtils
import kotlin.random.Random

class WildBattle() {
    lateinit var activity: MainActivity
    lateinit var wildBattleLevelData: WildBattleLevelData
    var encountersLeft: Int = 0
    lateinit var pokemon: Pokemon
    lateinit var opponent: Pokemon

    constructor(activity: MainActivity, wildBattleLevelData: WildBattleLevelData) : this() {
        this.activity = activity
        this.wildBattleLevelData = wildBattleLevelData
        this.encountersLeft = wildBattleLevelData.encounter
        this.pokemon = activity.trainer!!.pokemons[0]
    }

    fun turn(trainerPokemonMove: PokemonMove) {
        var trainerStarts = BattleUtils.trainerStarts(pokemon, opponent, trainerPokemonMove.move)
        if (trainerStarts) {
            pokemon.attack(trainerPokemonMove, opponent)
            if (opponent.currentHP > 0)
                opponent.attack(opponent.IA(pokemon), pokemon)
        } else {
            opponent.IA(pokemon)
            if (pokemon.currentHP > 0)
                pokemon.attack(trainerPokemonMove, opponent)
        }
        if (opponent.currentHP == 0 && encountersLeft > 0) {
            encountersLeft--
            generateRandomEncounter()
        }
    }

    fun getBattleState(): State {
        if (encountersLeft <= 0)
            return State.TRAINER_VICTORY
        if (!activity.trainer!!.canStillBattle())
            return State.TRAINER_LOSS
        return State.IN_PROGRESS
    }

    fun generateRandomEncounter(): Pokemon {
        var randomLevel = Random.nextInt(
            wildBattleLevelData.possibleEncounters.minLevel,
            wildBattleLevelData.possibleEncounters.maxLevel
        )
        opponent = activity.gameDataService.generatePokemon(
            wildBattleLevelData.possibleEncounters.encounters.random().id,
            randomLevel
        )
        return opponent
    }
}