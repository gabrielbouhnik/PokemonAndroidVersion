package com.pokemon.android.version.model

enum class ExpGaugeType {
    FAST, MEDIUM_FAST, MEDIUM_SLOW, SLOW;

    companion object {
        fun getExpGauge(pokemon: Pokemon): Int {
            when(pokemon.data.expGaugeType) {
                FAST -> return (0.8 * Math.pow(pokemon.level.toDouble(), 3.0)).toInt()
                MEDIUM_FAST -> return Math.pow(pokemon.level.toDouble(), 3.0).toInt()
                MEDIUM_SLOW -> return (1.2 * Math.pow(pokemon.level.toDouble(), 3.0)
                                        - 15.0 * Math.pow(pokemon.level.toDouble(), 2.0)
                                        + 100.0 * pokemon.level.toDouble()
                                        - 140.0).toInt()
                SLOW -> return (1.25 * Math.pow(pokemon.level.toDouble(), 3.0)).toInt()
            }
        }

        fun getExpToNextLevel(pokemon: Pokemon): Int {
            val nextLevel = (pokemon.level + 1).toDouble()
            when(pokemon.data.expGaugeType) {
                FAST -> return (0.8 * Math.pow(nextLevel, 3.0)).toInt()
                MEDIUM_FAST -> return Math.pow(nextLevel, 3.0).toInt()
                MEDIUM_SLOW -> return (1.2 * Math.pow(nextLevel, 3.0)
                        - 15.0 * Math.pow(nextLevel, 2.0)
                        + 100.0 * nextLevel
                        - 140.0).toInt()
                SLOW -> return (1.25 * Math.pow(nextLevel, 3.0)).toInt()
            }
        }
    }
}

