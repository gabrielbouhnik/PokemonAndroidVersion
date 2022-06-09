package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

class ExpItem(var exp : Int) : Item{
    companion object{
        val EXP_CANDY_S = ExpItem(100)
        val EXP_CANDY_M = ExpItem(500)
        val EXP_CANDY_L = ExpItem(1000)
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.battleData == null && pokemon.level < 100
    }

    override fun apply(pokemon: Pokemon) {
        pokemon.gainExp(exp)
    }
}