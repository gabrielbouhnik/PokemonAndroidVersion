package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status

class FullHeal () : Item {
    companion object{
        val FULL_HEAL = FullHeal()
    }

    override fun apply(pokemon: Pokemon) {
        if (pokemon.status != Status.OK && pokemon.currentHP > 0)
            pokemon.status = Status.OK
        else
            throw ItemCannotBeUsedException()
    }
}