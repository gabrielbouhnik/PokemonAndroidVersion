package com.pokemon.android.version.utils

import com.pokemon.android.version.model.PokemonData
import com.pokemon.android.version.model.move.Stats
import kotlin.math.roundToInt

class StatUtils {
    companion object{
        fun computeHP(data : PokemonData, level: Int): Int{
            return 10 + level + (data.hp.toFloat() * (level / 50f)).roundToInt()
        }

        fun computeStat(data : PokemonData, level: Int, stat : Stats): Int{
            return when(stat){
                Stats.ATTACK -> 5 + (data.attack.toFloat() * (level / 50f)).roundToInt()
                Stats.DEFENSE -> 5 + (data.defense.toFloat() * (level / 50f)).roundToInt()
                Stats.SPATK -> 5 + (data.spAtk.toFloat() * (level / 50f)).roundToInt()
                Stats.SPDEF -> 5 + (data.spDef.toFloat() * (level / 50f)).roundToInt()
                Stats.SPEED -> 5 + (data.speed.toFloat() * (level / 50f)).roundToInt()
                else -> 1
            }
        }
    }
}