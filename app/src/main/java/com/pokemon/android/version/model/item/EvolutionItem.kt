package com.pokemon.android.version.model.item

import com.pokemon.android.version.GameDataService
import com.pokemon.android.version.model.Pokemon

class EvolutionItem(var itemId : Int, var gameDataService: GameDataService) : Item {
    override fun isUsable(pokemon: Pokemon): Boolean {
        if (pokemon.battleData != null)
            return false
        if (pokemon.data.evolutionId != null
            && pokemon.data.evolutionCondition!!.itemId == itemId)
                return pokemon.canEvolve()
        return false
    }

    override fun apply(pokemon: Pokemon) {
    }
}