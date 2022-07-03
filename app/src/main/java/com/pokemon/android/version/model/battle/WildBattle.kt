package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.level.WildBattleLevelData
import com.pokemon.android.version.utils.MoveUtils
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

    override fun updateOpponent(){
        if (encountersLeft > 0) {
            generateRandomEncounter()
        }
    }

    override fun getBattleState(): State {
        if (encountersLeft == 0 && (opponent.trainer != null || opponent.currentHP == 0)){
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle()  || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    fun generateRandomEncounter(): Pokemon {
        encountersLeft--
        val randomLevel = Random.nextInt(
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