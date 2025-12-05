package com.pokemon.android.version.utils

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Type
import com.pokemon.android.version.model.level.PokemonBoss
import com.pokemon.android.version.model.move.*
import com.pokemon.android.version.model.move.pokemon.PokemonMove

class MoveUtils {
    companion object {
        val STRUGGLE = RecoilMove.RecoilMoveBuilder()
            .id(-1)
            .name("Struggle")
            .power(50)
            .type(Type.NORMAL)
            .highCritRate(false)
            .pp(20)
            .recoil(Recoil.QUARTER)
            .characteristics(listOf(MoveCharacteristic.CONTACT))
            .build()

        fun getMoveList(pokemon: Pokemon): ArrayList<PokemonMove> {
            val res = arrayListOf(pokemon.getBattleMove1())
            if (pokemon.getBattleMove2() != null)
                res.add(pokemon.getBattleMove2()!!)
            if (pokemon.getBattleMove3() != null)
                res.add(pokemon.getBattleMove3()!!)
            if (pokemon.getBattleMove4() != null)
                res.add(pokemon.getBattleMove4()!!)
            if (pokemon is PokemonBoss) {
                if (pokemon.move5 != null)
                    res.add(pokemon.move5!!)
                if (pokemon.move6 != null)
                    res.add(pokemon.move6!!)
            }
            return res
        }

        fun getUsableMoves(pokemon: Pokemon): List<PokemonMove> {
            var usableMoves = getMoveList(pokemon).filter { it.pp > 0  && !it.isDisabled()}
            if (pokemon.battleData!!.battleStatus.contains(Status.TAUNTED)) {
                usableMoves = usableMoves.filter { it.move.category != MoveCategory.OTHER}
            }
            if (usableMoves.isEmpty()) {
                return arrayListOf(PokemonMove(STRUGGLE, 1, 0))
            }
            return usableMoves
        }

        fun getPossibleMoves(pokemon: Pokemon): List<Move> {
            val possibleMoves: MutableList<Move> =
                pokemon.data.movesByLevel.filter { it.level <= pokemon.level }.map { it.move }.toMutableList()
            if (pokemon.isFromBanner)
                possibleMoves += pokemon.data.bannerMoves.map { it.move }.filter{!possibleMoves.contains(it)}
            val currentMoves: List<Int> = getMoveList(pokemon).map { it.move.id }
            pokemon.movesLearnedByTM.forEach {
                if (!possibleMoves.contains(it))
                    possibleMoves.add(it)
            }
            return possibleMoves.filter { !currentMoves.contains(it.id) }
        }
    }
}