package com.pokemon.android.version.model.item

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.model.Pokemon

class EvolutionItem(var itemId : Int    ) : Item {
    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.canEvolve() && pokemon.battleData != null
    }

    override fun apply(pokemon: Pokemon) {
    }
}