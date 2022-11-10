package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status

class FullHeal : Item {
    companion object {
        val FULL_HEAL = FullHeal()
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.currentHP > 0 && (pokemon.status != Status.OK || (pokemon.battleData != null
                && pokemon.battleData!!.battleStatus.contains(Status.CONFUSED)))
    }

    override fun apply(pokemon: Pokemon) {
        if (pokemon.battleData != null && pokemon.battleData!!.battleStatus.contains(Status.CONFUSED))
            pokemon.battleData!!.battleStatus.remove(Status.CONFUSED)
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