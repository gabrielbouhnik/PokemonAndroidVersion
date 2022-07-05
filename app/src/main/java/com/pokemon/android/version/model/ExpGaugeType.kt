package com.pokemon.android.version.model

import kotlin.math.pow

enum class ExpGaugeType {
    FAST, MEDIUM_FAST, MEDIUM_SLOW, SLOW;

    companion object {
        fun getExpGauge(pokemon: Pokemon): Int {
            return when (pokemon.data.expGaugeType) {
                FAST -> (0.8 * pokemon.level.toDouble().pow(3.0)).toInt()
                MEDIUM_FAST -> pokemon.level.toDouble().pow(3.0).toInt()
                MEDIUM_SLOW -> (1.2 * pokemon.level.toDouble().pow(3.0)
                        - 15.0 * pokemon.level.toDouble().pow(2.0)
                        + 100.0 * pokemon.level.toDouble()
                        - 140.0).toInt()
                SLOW -> (1.25 * pokemon.level.toDouble().pow(3.0)).toInt()
            }
        }

        fun getExpToNextLevel(pokemon: Pokemon): Int {
            val nextLevel = (pokemon.level + 1).toDouble()
            return when (pokemon.data.expGaugeType) {
                FAST -> (0.8 * nextLevel.pow(3.0)).toInt()
                MEDIUM_FAST -> nextLevel.pow(3.0).toInt()
                MEDIUM_SLOW -> (1.2 * nextLevel.pow(3.0)
                        - 15.0 * nextLevel.pow(2.0)
                        + 100.0 * nextLevel
                        - 140.0).toInt()
                SLOW -> (1.25 * nextLevel.pow(3.0)).toInt()
            }
        }
    }
}

