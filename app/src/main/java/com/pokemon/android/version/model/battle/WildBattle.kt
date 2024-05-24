package com.pokemon.android.version.model.battle

import com.pokemon.android.version.MainActivity
import com.pokemon.android.version.R
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.level.WildBattleLevelData
import com.pokemon.android.version.ui.LevelMenu
import com.pokemon.android.version.utils.MoveUtils
import kotlin.random.Random

class WildBattle() : Battle() {
    var encountersLeft: Int = 0

    constructor(activity: MainActivity, wildBattleLevelData: WildBattleLevelData) : this() {
        this.activity = activity
        this.levelData = wildBattleLevelData
        if (activity.hardMode)
            wildBattleLevelData.possibleEncounters.encounters = wildBattleLevelData.possibleEncounters.encounters.filter { it.id != 129 && it.id != 63 }
        this.encountersLeft = wildBattleLevelData.encounter
        this.pokemon = activity.trainer!!.getFirstPokemonThatCanFight()!!
        this.pokemon.battleData = PokemonBattleData()
    }

    override fun updateOpponent() {
        if (opponent.data.id in 243..245)
            activity.updateMusic(R.raw.wild_battle)
        if (encountersLeft > 0) {
            generateRandomEncounter()
        }
    }

    override fun getBattleState(): State {
        if (encountersLeft == 0 && (opponent.trainer != null || opponent.currentHP == 0)) {
            return State.TRAINER_VICTORY
        }
        if (!activity.trainer!!.canStillBattle() || MoveUtils.getMoveList(opponent).none { it.pp > 0 }) {
            return State.TRAINER_LOSS
        }
        return State.IN_PROGRESS
    }

    fun generateRandomEncounter(): Pokemon {
        encountersLeft--
        if (activity.trainer!!.progression > LevelMenu.ELITE_4_LAST_LEVEL_ID
            && levelData.name.startsWith("\nRoute")
        ) {
            when (Random.nextInt(100)) {
                1 -> {
                    if (!activity.trainer!!.pokemons.map { it.data.id }.contains(243)) {
                        opponent = activity.gameDataService.generatePokemon(243, 70)
                        activity.updateMusic(R.raw.legendary_dogs)
                        return opponent
                    }
                }
                2 -> {
                    if (!activity.trainer!!.pokemons.map { it.data.id }.contains(244)) {
                        opponent = activity.gameDataService.generatePokemon(244, 70)
                        activity.updateMusic(R.raw.legendary_dogs)
                        return opponent
                    }
                }
                3 -> {
                    if (!activity.trainer!!.pokemons.map { it.data.id }.contains(245)) {
                        opponent = activity.gameDataService.generatePokemon(245, 70)
                        activity.updateMusic(R.raw.legendary_dogs)
                        return opponent
                    }
                }
            }
        }
        val randomLevel = Random.nextInt(
            (levelData as WildBattleLevelData).possibleEncounters.minLevel,
            (levelData as WildBattleLevelData).possibleEncounters.maxLevel
        )
        opponent = if (randomLevel < activity.trainer!!.getMaxLevel()) {
            activity.gameDataService.generateWildPokemon(
                (levelData as WildBattleLevelData).possibleEncounters.encounters.random().id,
                randomLevel
            )
        } else {
            activity.gameDataService.generatePokemon(
                (levelData as WildBattleLevelData).possibleEncounters.encounters.random().id,
                randomLevel
            )
        }
        activity.trainer!!.updatePokedex(opponent)
        return opponent
    }
}