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
        fun updateStat(pokemon: Pokemon, statChangeMove: StatChangeMove, target: Target): String {
            var details = ""
            if (target == Target.OPPONENT && statChangeMove.multiplicator < 0 && pokemon.hasAbility(Ability.CLEAR_BODY))
                return "${pokemon.data.name}'s Clear Body: ${pokemon.data.name}'s stats cannot be lowered!\n"
            statChangeMove.statsAffected.forEach {
                if (target == Target.OPPONENT && it == ATTACK && pokemon.hasAbility(Ability.HYPER_CUTTER) && statChangeMove.target == Target.OPPONENT) {
                    details += "${pokemon.data.name}'s Hyper Cutter: ${pokemon.data.name}'s " + it.value + " cannot be lowered!\n"
                    return@forEach
                }
                else if (it == ACCURACY && pokemon.hasAbility(Ability.KEEN_EYE)) {
                    details += "${pokemon.data.name}'s Keen Eye: ${pokemon.data.name}'s " + it.value + " cannot be lowered!\n"
                    return@forEach
                } else if (it == DEFENSE && pokemon.hasAbility(Ability.BIG_PECKS)) {
                    details += "${pokemon.data.name}'s Big Pecks: ${pokemon.data.name}'s " + it.value + " cannot be lowered!\n"
                    return@forEach
                } else if (statChangeMove.multiplicator > 1)
                    details += "${pokemon.data.name}'s " + it.value + " rose!\n"
                else {
                    details += "${pokemon.data.name}'s " + it.value + " fell!\n"
                    if (target == Target.OPPONENT && pokemon.hasAbility(Ability.DEFIANT)) {
                        pokemon.battleData!!.attackMultiplicator *= 1.5f
                        details += "${pokemon.data.name}'s Defiant: ${pokemon.data.name}'s attack rose!\n"
                        if (it == ATTACK)
                            return@forEach
                    }
                }
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
            if (statChangeMove.id == 245){
                details += "${pokemon.data.name}'s Defense fell!\n"
                details += "${pokemon.data.name}'s Sp. Def fell!\n"
                pokemon.battleData!!.spDefMultiplicator *= 0.67f
                pokemon.battleData!!.spDefMultiplicator *= 0.67f
            }
            return details
        }

        fun increaseBossStats(pokemon: Pokemon, stats: List<Stats>) {
            pokemon.battleData!!.accuracyMultiplicator *= 1.33f
            pokemon.battleData!!.criticalRate *= 2f
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
                    else -> {}
                }
            }
        }
    }
}