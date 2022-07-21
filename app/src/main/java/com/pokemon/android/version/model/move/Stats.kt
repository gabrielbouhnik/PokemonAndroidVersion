package com.pokemon.android.version.model.move

import com.pokemon.android.version.model.Pokemon

enum class Stats(var value: String) {
    ATTACK("Attack"),
    DEFENSE("Defense"),
    SPATK("Sp. Atk"),
    SPDEF("Sp. Def"),
    SPEED("Speed"),
    ACCURACY("Accuracy"),
    CRITICAL_RATE("Crit Rate");

    companion object {
        fun updateStat(pokemon: Pokemon, statChangeMove: StatChangeMove) : String {
            val sb = StringBuilder()
            statChangeMove.statsAffected.forEach {
                if (statChangeMove.multiplicator > 1)
                    sb.append("${pokemon.data.name}'s " + it.value + " rose!\n")
                else
                    sb.append("${pokemon.data.name}'s " + it.value + " fell!\n")
                when (it) {
                    ATTACK -> {
                        if (pokemon.battleData!!.attackMultiplicator < 4 && pokemon.battleData!!.attackMultiplicator > 0.25f)
                            pokemon.battleData!!.attackMultiplicator *= statChangeMove.multiplicator
                    }
                    DEFENSE -> {
                        if (pokemon.battleData!!.defenseMultiplicator < 4 && pokemon.battleData!!.defenseMultiplicator > 0.25f)
                            pokemon.battleData!!.defenseMultiplicator *= statChangeMove.multiplicator
                    }
                    SPATK -> {
                        if (pokemon.battleData!!.spAtkMultiplicator < 4 && pokemon.battleData!!.spAtkMultiplicator > 0.25f)
                            pokemon.battleData!!.spAtkMultiplicator *= statChangeMove.multiplicator
                    }
                    SPDEF -> {
                        if (pokemon.battleData!!.spDefMultiplicator < 4 && pokemon.battleData!!.spDefMultiplicator > 0.25f)
                            pokemon.battleData!!.spDefMultiplicator *= statChangeMove.multiplicator
                    }
                    SPEED -> {
                        if (pokemon.battleData!!.speedMultiplicator < 4 && pokemon.battleData!!.speedMultiplicator > 0.25f)
                            pokemon.battleData!!.speedMultiplicator *= statChangeMove.multiplicator
                    }
                    ACCURACY -> {
                        if (pokemon.battleData!!.accuracyMultiplicator < 4 && pokemon.battleData!!.accuracyMultiplicator > 0.25f)
                            pokemon.battleData!!.accuracyMultiplicator *= statChangeMove.multiplicator
                    }
                    CRITICAL_RATE -> {
                        if (pokemon.battleData!!.criticalRate < 4)
                            pokemon.battleData!!.criticalRate *= statChangeMove.multiplicator
                    }
                }
            }
            return sb.toString()
        }

        fun increaseBossStats(pokemon: Pokemon, stats: List<Stats>) {
            stats.forEach {
                when (it) {
                    ATTACK -> {
                        pokemon.battleData!!.attackMultiplicator *= 1.5f
                    }
                    DEFENSE -> {
                        pokemon.battleData!!.defenseMultiplicator *= 1.5f
                    }
                    SPATK -> {
                        pokemon.battleData!!.spAtkMultiplicator *= 1.5f
                    }
                    SPDEF -> {
                        pokemon.battleData!!.spDefMultiplicator *= 1.5f
                    }
                    SPEED -> {
                        pokemon.battleData!!.speedMultiplicator *= 1.5f
                    }
                    ACCURACY -> {
                        pokemon.battleData!!.accuracyMultiplicator *= 1.5f
                    }
                    CRITICAL_RATE -> {
                        pokemon.battleData!!.criticalRate *= 1.5f
                    }
                }
            }
        }
    }
}