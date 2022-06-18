package com.pokemon.android.version.model.item

import com.pokemon.android.version.exception.ItemCannotBeUsedException
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status

class FullHeal () : Item {
    companion object{
        val FULL_HEAL = FullHeal()
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.status != Status.OK && pokemon.currentHP > 0
    }

    override fun apply(pokemon: Pokemon) {
        pokemon.status = Status.OK
        if (pokemon.battleData != null && pokemon.battleData!!.battleStatus.contains(Status.CONFUSED))
            pokemon.battleData!!.battleStatus.remove(Status.CONFUSED)
    }
}