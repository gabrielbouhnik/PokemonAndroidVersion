package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon

class Revive(var isMax: Boolean): Item {
    companion object{
        val REVIVE = Revive(false)
        val MAX_REVIVE = Revive(true)
    }

    override fun apply(pokemon: Pokemon) {
        if (pokemon.currentHP != 0)
            throw ItemCannotBeUsedException()
        if (isMax)
            pokemon.currentHP = pokemon.hp
        else
            pokemon.currentHP = pokemon.hp/2
    }
}