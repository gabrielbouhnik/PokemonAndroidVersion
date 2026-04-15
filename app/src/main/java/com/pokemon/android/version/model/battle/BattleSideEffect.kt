package com.pokemon.android.version.model.battle

import java.util.*

enum class BattleSideEffect {
    AURORA_VEIL,
    HEALING_WISH,
    LIGHT_SCREEN,
    REFLECT,
    SAFEGUARD,
    STICKY_WEB,
    TAILWIND,
    WISH;

    companion object {
        fun moveNameToTeamEffect(moveName: String): BattleSideEffect {
            return valueOf(moveName.replace(" ", "_").uppercase(Locale.getDefault()))
        }
    }
}