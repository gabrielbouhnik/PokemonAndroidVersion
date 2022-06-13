package com.pokemon.android.version.model.move

import com.pokemon.android.version.model.Pokemon

enum class Stats {
    ATTACK,
    DEFENSE,
    SPATK,
    SPDEF,
    SPEED,
    ACCURACY,
    CRITICAL_RATE;

    companion object{
        fun updateStat(pokemon : Pokemon, statChangeMove: StatChangeMove){
            statChangeMove.statsAffected.forEach {
                when(it){
                    ATTACK ->{pokemon.battleData!!.attackMultiplicator *= statChangeMove.multiplicator}
                    DEFENSE -> {pokemon.battleData!!.defenseMultiplicator *= statChangeMove.multiplicator}
                    SPATK -> {pokemon.battleData!!.spAtkMultiplicator *= statChangeMove.multiplicator}
                    SPDEF -> {pokemon.battleData!!.spDefMultiplicator *= statChangeMove.multiplicator}
                    SPEED -> {pokemon.battleData!!.speedMultiplicator *= statChangeMove.multiplicator}
                    ACCURACY -> {pokemon.battleData!!.accuracyMultiplicator *= statChangeMove.multiplicator}
                    CRITICAL_RATE -> {pokemon.battleData!!.critRate *= statChangeMove.multiplicator}
                }
            }
        }

        fun increaseStat(pokemon : Pokemon, stats : List<Stats>){
            stats.forEach {
                when(it){
                    ATTACK ->{pokemon.battleData!!.attackMultiplicator *= 1.5f}
                    DEFENSE -> {pokemon.battleData!!.defenseMultiplicator *= 1.5f}
                    SPATK -> {pokemon.battleData!!.spAtkMultiplicator *= 1.5f}
                    SPDEF -> {pokemon.battleData!!.spDefMultiplicator *= 1.5f}
                    SPEED -> {pokemon.battleData!!.speedMultiplicator *= 1.5f}
                    ACCURACY -> {pokemon.battleData!!.accuracyMultiplicator *= 1.5f}
                    CRITICAL_RATE -> {pokemon.battleData!!.critRate *= 1.5f}
                }
            }
        }
    }
}