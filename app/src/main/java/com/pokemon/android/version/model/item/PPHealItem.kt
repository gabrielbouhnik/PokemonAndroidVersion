package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon
import com.pokemon.android.version.utils.HealUtils
import com.pokemon.android.version.utils.MoveUtils

class PPHealItem : Item {
    companion object {
        val PP_HEAL = PPHealItem()
    }

    override fun isUsable(pokemon: Pokemon): Boolean {
        return MoveUtils.getMoveList(pokemon).any { it.pp < it.move.pp }
    }

    override fun apply(pokemon: Pokemon) {
        HealUtils.healPP(pokemon)
    }
}