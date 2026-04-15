package com.pokemon.android.version.model.move

import com.pokemon.android.version.model.Ability
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
        fun updateStat(pokemon: Pokemon, statChangeMove: StatChangeMove, target: Target, moldBreaker: Boolean): String {
            var details = ""
            statChangeMove.statsAffected.forEach {
                if (!moldBreaker) {
                    if (target == Target.OPPONENT && it.multiplicator < 1 && pokemon.hasAbility(Ability.CLEAR_BODY)) {
                        details += "${pokemon.data.name}'s Clear Body: ${pokemon.data.name}'s stats cannot be lowered!\n"
                        return@forEach
                    }
                    if (target == Target.OPPONENT && it.stat == ATTACK && pokemon.hasAbility(Ability.HYPER_CUTTER) && statChangeMove.target == Target.OPPONENT) {
                        details += "${pokemon.data.name}'s Hyper Cutter: ${pokemon.data.name}'s " + it.stat.value + " cannot be lowered!\n"
                        return@forEach
                    } else if (it.stat == ACCURACY && pokemon.hasAbility(Ability.KEEN_EYE)) {
                        details += "${pokemon.data.name}'s Keen Eye: ${pokemon.data.name}'s " + it.stat.value + " cannot be lowered!\n"
                        return@forEach
                    } else if (it.stat == DEFENSE && pokemon.hasAbility(Ability.BIG_PECKS)) {
                        details += "${pokemon.data.name}'s Big Pecks: ${pokemon.data.name}'s " + it.stat.value + " cannot be lowered!\n"
                        return@forEach
                    }
                }
                if (it.multiplicator > 1)
                    details += "${pokemon.data.name}'s " + it.stat.value + " rose!\n"
                else {
                    details += "${pokemon.data.name}'s " + it.stat.value + " fell!\n"
                    if (target == Target.OPPONENT && pokemon.hasAbility(Ability.DEFIANT)) {
                        pokemon.battleData!!.statsMultiplier.updateStat(StatChange.ATTACK_ONE_LEVEL_RAISE)
                        details += "${pokemon.data.name}'s Defiant: ${pokemon.data.name}'s attack rose!\n"
                        if (it.stat == ATTACK)
                            return@forEach
                    }
                    if (target == Target.OPPONENT && pokemon.hasAbility(Ability.COMPETITIVE)) {
                        pokemon.battleData!!.statsMultiplier.updateStat(StatChange.SPATK_ONE_LEVEL_RAISE)
                        details += "${pokemon.data.name}'s Competitive: ${pokemon.data.name}'s sp. Atk rose!\n"
                        if (it.stat == SPATK)
                            return@forEach
                    }
                }
                pokemon.battleData!!.statsMultiplier.updateStat(it)
            }
            return details
        }

        fun increaseBossStats(pokemon: Pokemon, stats: List<Stats>) {
            pokemon.battleData!!.statsMultiplier.accuracyMultiplicator *= 1.33f
            pokemon.battleData!!.statsMultiplier.criticalRate *= 1.33f
            stats.forEach {
                when (it) {
                    ATTACK -> {
                        pokemon.battleData!!.statsMultiplier.attackMultiplicator *= 1.5f
                    }
                    DEFENSE -> {
                        pokemon.battleData!!.statsMultiplier.defenseMultiplicator *= 1.5f
                    }
                    SPATK -> {
                        pokemon.battleData!!.statsMultiplier.spAtkMultiplicator *= 1.5f
                    }
                    SPDEF -> {
                        pokemon.battleData!!.statsMultiplier.spDefMultiplicator *= 1.5f
                    }
                    SPEED -> {
                        pokemon.battleData!!.statsMultiplier.speedMultiplicator *= 1.5f
                    }
                    else -> {}
                }
            }
        }
    }
}