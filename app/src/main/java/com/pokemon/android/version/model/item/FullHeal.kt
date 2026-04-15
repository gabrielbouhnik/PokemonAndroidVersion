package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Ability
import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.model.Status
import com.pokemon.android.version.model.Status.Companion.cureAllStatus
import com.pokemon.android.version.utils.BattleUtils

class FullHeal : Item {
    companion object {
        val FULL_HEAL = FullHeal()
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.currentHP > 0 && (pokemon.status != Status.OK || (pokemon.battleData != null
                && pokemon.battleData!!.battleStatus.contains(Status.CONFUSED)))
    }

    override fun apply(pokemon: Pokemon) {
        cureAllStatus(pokemon)
    }
}