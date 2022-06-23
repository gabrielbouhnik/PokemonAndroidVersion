package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

class TMItem(var moveId : Int) : Item {
    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.data.movesByTM.any { it.move.id == moveId }
    }

    override fun apply(pokemon: Pokemon) {
        pokemon.movesLearnedByTM.add(pokemon.data.movesByTM.map{it.move}.first { it.id == moveId })
    }
}