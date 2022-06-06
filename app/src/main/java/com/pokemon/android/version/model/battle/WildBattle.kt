package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.level.WildBattleLevelData
import com.pokemon.android.version.model.move.pokemon.PokemonMove
import com.pokemon.android.version.ui.BattleUI
import com.pokemon.android.version.utils.BattleUtils
import java.lang.StringBuilder
import kotlin.random.Random

class WildBattle() : Battle() {
    var encountersLeft: Int = 0

    constructor(activity: MainActivity, wildBattleLevelData: WildBattleLevelData) : this() {
        this.activity = activity
        this.levelData = wildBattleLevelData
        this.encountersLeft = wildBattleLevelData.encounter
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
    }

    override fun turn(trainerPokemonMove: PokemonMove) {
        var sb = StringBuilder()
        var trainerStarts = BattleUtils.trainerStarts(pokemon, opponent, trainerPokemonMove.move)
        if (trainerStarts) {
            sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
            if (!pokemon.attack(trainerPokemonMove, opponent))
                sb.append("${pokemon.data.name}'s attack missed!\n")
            if (opponent.currentHP > 0) {
                sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
                if(!opponent.attack(opponent.IA(pokemon), pokemon))
                    sb.append("${opponent.data.name}'s attack missed!\n")
            }
        } else {
            sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
            opponent.attack(opponent.IA(pokemon), pokemon)
            if (pokemon.currentHP > 0) {
                if (!pokemon.attack(trainerPokemonMove, opponent))
                    sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
            }
        }
        if (opponent.currentHP == 0 && encountersLeft > 0) {
            encountersLeft--
            generateRandomEncounter()
        }
        BattleUI.dialogTextView!!.text = sb.toString()
    }

    override fun getBattleState(): State {
        if (encountersLeft == 0){
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle()) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    fun generateRandomEncounter(): Pokemon {
        var randomLevel = Random.nextInt(
            (levelData as WildBattleLevelData).possibleEncounters.minLevel,
            (levelData as WildBattleLevelData).possibleEncounters.maxLevel
        )
        opponent = activity.gameDataService.generatePokemon(
            (levelData as WildBattleLevelData).possibleEncounters.encounters.random().id,
            randomLevel
        )
        return opponent
    }
}