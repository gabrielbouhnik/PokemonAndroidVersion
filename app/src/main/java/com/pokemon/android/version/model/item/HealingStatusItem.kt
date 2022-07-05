package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status

class HealingStatusItem(val status: Status) : Item {
    companion object {
        val PARALYSE_HEAL = HealingStatusItem(Status.PARALYSIS)
        val BURN_HEAL = HealingStatusItem(Status.BURN)
        val ANTIDOTE = HealingStatusItem(Status.POISON)
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.currentHP > 0 && pokemon.status == status
    }


    override fun apply(pokemon: Pokemon) {
        if (pokemon.status == status)
            pokemon.status = Status.OK
    }
}