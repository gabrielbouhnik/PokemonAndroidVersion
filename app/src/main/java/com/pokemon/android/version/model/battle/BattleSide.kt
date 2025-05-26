package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.item.HoldItem
import com.pokemon.android.version.model.move.StatChange

class BattleSide(var battleSideEffects: ArrayList<BattleSideEffect>, var lightScreenCounter: Int = 0, var auroraVeilCounter: Int = 0,
                 var reflectCounter: Int = 0, var safeguardCounter: Int = 0, var tailwindCounter: Int = 0) {
    fun addBattleSideEffect(pokemon: Pokemon, opponent: Pokemon, battleSideEffect :BattleSideEffect): String {
        if (!battleSideEffects.contains(battleSideEffect)) {
            battleSideEffects.add(battleSideEffect)
            return when (battleSideEffect) {
                BattleSideEffect.LIGHT_SCREEN -> {
                    lightScreenCounter = if (pokemon.hasItem(HoldItem.LIGHT_CLAY)) 8 else 5
                    "${pokemon.data.name} and its allies are stronger against special moves!\n"
                }
                BattleSideEffect.REFLECT -> {
                    reflectCounter = if (pokemon.hasItem(HoldItem.LIGHT_CLAY)) 8 else 5
                    "${pokemon.data.name} and its allies are stronger against physical moves!\n"
                }
                BattleSideEffect.AURORA_VEIL -> {
                    reflectCounter = if (pokemon.hasItem(HoldItem.LIGHT_CLAY)) 8 else 5
                    "${pokemon.data.name} and its allies are stronger against physical and special moves!\n"
                }
                BattleSideEffect.SAFEGUARD -> {
                    safeguardCounter = 5
                    "${pokemon.data.name} and its allies are cloaked in a mystical veil!\n"
                }
                BattleSideEffect.STICKY_WEB -> {
                    "A sticky web has been laid out on the ground around ${opponent.data.name}!\n"
                }
                BattleSideEffect.TAILWIND -> {
                    tailwindCounter = 4
                    pokemon.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_ONE_LEVEL_RAISE)
                    "The Tailwind blew behind ${pokemon.data.name}!\n"
                }
            }
        } else {
            return "But it failed!\n"
        }
    }

    fun updateBattleSide(pokemon: Pokemon) {
        for (battleSideEffect in battleSideEffects) {
            when (battleSideEffect) {
                BattleSideEffect.LIGHT_SCREEN -> {
                    lightScreenCounter -= 1
                    if (lightScreenCounter == 0) {
                        battleSideEffects.remove(BattleSideEffect.LIGHT_SCREEN)
                    }
                }
                BattleSideEffect.REFLECT -> {
                    reflectCounter -= 1
                    if (reflectCounter == 0) {
                        battleSideEffects.remove(BattleSideEffect.REFLECT)
                    }
                }
                BattleSideEffect.AURORA_VEIL -> {
                    auroraVeilCounter -= 1
                    if (auroraVeilCounter == 0) {
                        battleSideEffects.remove(BattleSideEffect.AURORA_VEIL)
                    }
                }
                BattleSideEffect.SAFEGUARD -> {
                    safeguardCounter -= 1
                    if (safeguardCounter == 0) {
                        battleSideEffects.remove(BattleSideEffect.SAFEGUARD)
                    }
                }
                BattleSideEffect.TAILWIND -> {
                    tailwindCounter -= 1
                    if (tailwindCounter == 0) {
                        battleSideEffects.remove(BattleSideEffect.TAILWIND)
                        if (pokemon.battleData != null) {
                            pokemon.battleData!!.statsMultiplier.updateStat(StatChange.SPEED_ONE_LEVEL_DECREASE)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}
