package com.pokemon.android.version.model.move

import com.pokemon.android.version.entity.move.StatChangeEntity

class StatChange(var stat: Stats, var multiplicator: Float) {
    companion object {
        private const val NORMAL_STATS_ONE_LEVEL_RAISE = 1.5f
        private const val NORMAL_STATS_TWO_LEVEL_RAISE = 2f
        private const val NORMAL_STATS_ONE_LEVEL_DECREASE = 0.67f

        val ATTACK_ONE_LEVEL_RAISE = StatChange(Stats.ATTACK, NORMAL_STATS_ONE_LEVEL_RAISE)
        val ATTACK_TWO_LEVEL_RAISE = StatChange(Stats.ATTACK, NORMAL_STATS_TWO_LEVEL_RAISE)
        val DEFENSE_ONE_LEVEL_RAISE = StatChange(Stats.DEFENSE, NORMAL_STATS_ONE_LEVEL_RAISE)
        val SPATK_ONE_LEVEL_RAISE = StatChange(Stats.SPATK, NORMAL_STATS_ONE_LEVEL_RAISE)
        val SPATK_TWO_LEVEL_RAISE = StatChange(Stats.SPATK, NORMAL_STATS_TWO_LEVEL_RAISE)
        val SPDEF_ONE_LEVEL_RAISE = StatChange(Stats.SPDEF, NORMAL_STATS_ONE_LEVEL_RAISE)
        val SPEED_ONE_LEVEL_RAISE = StatChange(Stats.SPEED, NORMAL_STATS_ONE_LEVEL_RAISE)
        val SPEED_TWO_LEVEL_RAISE = StatChange(Stats.SPEED, NORMAL_STATS_TWO_LEVEL_RAISE)

        val ATTACK_ONE_LEVEL_DECREASE = StatChange(Stats.ATTACK, NORMAL_STATS_ONE_LEVEL_DECREASE)
        val DEFENSE_ONE_LEVEL_DECREASE = StatChange(Stats.DEFENSE, NORMAL_STATS_ONE_LEVEL_DECREASE)

        val ACCURACY_ONE_LEVEL_RAISE = StatChange(Stats.ACCURACY, 1.33f)

        fun of(statChangeEntity: StatChangeEntity): StatChange {
            return StatChange(Stats.valueOf(statChangeEntity.stat), statChangeEntity.multiplicator)
        }
    }
}