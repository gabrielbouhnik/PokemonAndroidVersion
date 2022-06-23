package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.move.Move
import com.pokemon.android.version.model.move.pokemon.PokemonMove

class MoveUtils {
    companion object {
        fun getMoveList(pokemon : Pokemon) : ArrayList<PokemonMove>{
            val res = arrayListOf(pokemon.move1)
            if (pokemon.move2 != null)
                res.add(pokemon.move2!!)
            if (pokemon.move3 != null)
                res.add(pokemon.move3!!)
            if (pokemon.move4 != null)
                res.add(pokemon.move4!!)
            return res
        }

        fun getPossibleMoves(pokemon : Pokemon) : List<Move>{
            val possibleMoves : MutableList<Move> =
                pokemon.data.movesByLevel.filter{it.level <= pokemon.level}.map{it.move}.toMutableList()
            if (pokemon.isFromBanner)
                possibleMoves += pokemon.data.bannerMoves.map { it.move }
            val currentMoves : List<Int> = getMoveList(pokemon).map{it.move.id}
            pokemon.movesLearnedByTM.forEach {
                if (!possibleMoves.contains(it))
                    possibleMoves.add(it)
            }
            return possibleMoves.filter{!currentMoves.contains(it.id)}
        }
    }
}