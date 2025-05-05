package com.pokemon.android.version.model.battle

import com.pokemon.android.version.model.move.StatChange
import com.pokemon.android.version.model.move.Stats

class StatsMultiplier(
    var attackMultiplicator: Float = 1F,
    var defenseMultiplicator: Float = 1F,
    var spAtkMultiplicator: Float = 1F,
    var spDefMultiplicator: Float = 1F,
    var speedMultiplicator: Float = 1F,
    var accuracyMultiplicator: Float = 1F,
    var criticalRate: Float = 1F,
) {
    companion object {
        fun canBeUpdated(statMultiplier: Float, multiplier: Float): Boolean {
            if (statMultiplier <= 0.25 && multiplier < 1)
                return false
            if (statMultiplier >= 4 && multiplier > 1)
                return false
            return true
        }
    }

    fun updateStat(statChange: StatChange): Boolean {
        when (statChange.stat) {
            Stats.ATTACK -> {
                return if (canBeUpdated(attackMultiplicator, statChange.multiplicator)) {
                    attackMultiplicator *= statChange.multiplicator
                    true
                } else {
                    false
                }
            }
            Stats.DEFENSE -> {
                return if (canBeUpdated(defenseMultiplicator, statChange.multiplicator)) {
                    defenseMultiplicator *= statChange.multiplicator
                    true
                } else {
                    false
                }
            }
            Stats.SPATK -> {
                return if (canBeUpdated(spAtkMultiplicator, statChange.multiplicator)) {
                    spAtkMultiplicator *= statChange.multiplicator
                    true
                } else {
                    false
                }
            }
            Stats.SPDEF -> {
                return if (canBeUpdated(spDefMultiplicator, statChange.multiplicator)) {
                    spDefMultiplicator *= statChange.multiplicator
                    true
                } else {
                    false
                }
            }
            Stats.SPEED -> {
                return if (canBeUpdated(speedMultiplicator, statChange.multiplicator)) {
                    speedMultiplicator *= statChange.multiplicator
                    true
                } else {
                    false
                }
            }
            Stats.ACCURACY -> {
                return if (canBeUpdated(accuracyMultiplicator, statChange.multiplicator)) {
                    accuracyMultiplicator *= statChange.multiplicator
                    true
                } else {
                    false
                }
            }
            Stats.CRITICAL_RATE -> {
                return if (criticalRate < 4f) {
                    criticalRate *= statChange.multiplicator
                    true
                } else {
                    false
                }
            }
        }
    }
}