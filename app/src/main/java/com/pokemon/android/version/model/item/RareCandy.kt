package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

class RareCandy : Item {
    companion object{
        val RARE_CANDY = RareCandy()
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.level < pokemon.trainer!!.getMaxLevel()
    }

    override fun apply(pokemon: Pokemon) {
        pokemon.level += 1
    }
}