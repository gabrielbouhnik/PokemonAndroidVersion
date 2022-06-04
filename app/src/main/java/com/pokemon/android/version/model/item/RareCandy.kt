package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status

class RareCandy : Item {
    companion object{
        val RARE_CANDY = RareCandy()
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.level < 100
    }

    override fun apply(pokemon: Pokemon) {
        pokemon.level += 1
    }
}