package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

class EvolutionItem(var id: Int): Item {
    override fun isUsable(pokemon: Pokemon): Boolean {
        for (evolution in pokemon.data.evolutions){
            if (evolution.evolutionCondition.itemId == id){
                return true
            }
        }
        return false
    }

    override fun apply(pokemon: Pokemon) {
    }
}