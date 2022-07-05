package com.pokemon.android.version.model.item

import com.pokemon.android.version.model.Pokemon

class TMItem(private var moveId: Int) : Item {
    override fun isUsable(pokemon: Pokemon): Boolean {
        return pokemon.data.movesByTM.any { it.move.id == moveId }
                && !pokemon.data.movesByLevel.filter { it.level <= pokemon.level }.map { it.move.id }.contains(moveId)
    }

    override fun apply(pokemon: Pokemon) {
        pokemon.movesLearnedByTM.add(pokemon.data.movesByTM.map { it.move }.first { it.id == moveId })
    }
}