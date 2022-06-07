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
    }
}