package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.ExpGaugeType
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
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
        this.dialogTextView = activity.findViewById(R.id.dialogTextView)
        this.levelData = wildBattleLevelData
        this.encountersLeft = wildBattleLevelData.encounter
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
    }

    override fun turn(trainerPokemonMove: PokemonMove) {
        val sb = StringBuilder()
        if (BattleUtils.trainerStarts(pokemon, opponent!!, trainerPokemonMove.move)) {
            sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
            val response = pokemon.attack(trainerPokemonMove, opponent!!)
            if (!response.success)
                sb.append(response.reason)
            if (opponent.currentHP > 0) {
                sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
                val opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
                if (!opponentResponse.success)
                    sb.append(opponentResponse.reason)
            }
        } else {
            sb.append("${opponent.data.name} uses ${opponent.IA(pokemon).move.name}\n")
            val opponentResponse = opponent.attack(opponent.IA(pokemon), pokemon)
            if (!opponentResponse.success)
                sb.append(opponentResponse.reason)
            if (pokemon.currentHP > 0) {
                sb.append("${pokemon.data.name} uses ${trainerPokemonMove.move.name}\n")
                val response = pokemon.attack(trainerPokemonMove, opponent)
                if (!response.success)
                    sb.append(response.reason)
            }
        }
        if (pokemon.currentHP > 0)
            sb.append(checkStatus(pokemon))
        if (pokemon.currentHP == 0){
            sb.append(pokemon.data.name + " fainted\n")
            pokemon.status = Status.OK
            pokemon.battleData = null
        }
        if (opponent.currentHP > 0){
                sb.append(checkStatus(opponent))
        }
        if (opponent.currentHP == 0){
            sb.append(opponent.data.name + " fainted\n")
            if (encountersLeft > 0) {
                generateRandomEncounter()
            }
        }
        dialogTextView.text = sb.toString()
    }

    override fun getBattleState(): State {
        if (encountersLeft == 0 && (opponent.trainer != null || opponent.currentHP == 0)){
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle()) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    fun generateRandomEncounter(): Pokemon {
        encountersLeft--
        var randomLevel = Random.nextInt(
            (levelData as WildBattleLevelData).possibleEncounters.minLevel,
            (levelData as WildBattleLevelData).possibleEncounters.maxLevel
        )
        opponent = activity.gameDataService.generatePokemon(
            (levelData as WildBattleLevelData).possibleEncounters.encounters.random().id,
            randomLevel
        )
        return opponent!!
    }
}