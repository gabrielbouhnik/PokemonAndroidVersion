package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status

class HealingStatusItem(val status: List<Status>) : Item {
    companion object {
        val PARALYSE_HEAL = HealingStatusItem(listOf(Status.PARALYSIS))
        val BURN_HEAL = HealingStatusItem(listOf(Status.BURN))
        val ANTIDOTE = HealingStatusItem(listOf(Status.POISON, Status.BADLY_POISON))
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.currentHP > 0 && status.contains(pokemon.status)
    }


    override fun apply(pokemon: Pokemon) {
        if (status.contains(pokemon.status)) {
            pokemon.status = Status.OK
            if (pokemon.battleData != null) {
                if (pokemon.hasAbility(Ability.GUTS))
                    pokemon.battleData!!.attackMultiplicator /= 1.5f
                if (pokemon.hasAbility(Ability.QUICK_FEET))
                    pokemon.battleData!!.speedMultiplicator /= 1.5f
                if (pokemon.hasAbility(Ability.MARVEL_SCALE))
                    pokemon.battleData!!.defenseMultiplicator /= 1.5f
                if (pokemon.hasAbility(Ability.COMPETITIVE))
                    pokemon.battleData!!.spAtkMultiplicator /= 1.5f
            }
        }
    }
}